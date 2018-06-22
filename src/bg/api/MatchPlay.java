package bg.api;

import bg.Main;
import bg.engine.Game;
import bg.engine.ScoreBoard;
import bg.engine.moves.Layout;
import bg.inUrFace.canvas.move.MoveOutput;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.inUrFace.mouse.MoveInput;

import java.util.ArrayList;

import static bg.Main.*;
import static bg.util.ThreadUtil.runWhenNotified;

public class MatchPlay {

  Game game;
  Layout matchLayout;
  ScoreBoard scoreBoard;
  boolean autoCompleteGame = false;

  MatchPlay() {

    scoreBoard = new ScoreBoard(settings.getScoreToWin());
    new ScenarioOutput(scenarios).outputSelectedScenario();
    getActionButton().setShowPleaseWaitButton(false);
    getActionButton().setText("Start Match");
    getActionButton().setHideActionButton(false);
    getLayoutEditor().startEditor();
  }

  public ScoreBoard getScoreBoard () {

    return scoreBoard;
  }

  public void endTurn () {

    getActionButton().setShowPleaseWaitButton(false);
    getActionButton().setHideActionButton(false);
    if (game.gameOver()) {
      autoCompleteGame = false;
      scoreBoard.writeGameScore(game);
    } else if (settings.isAutomatedEndTurn() || autoCompleteGame) {
      getMoveInputListener().setAcceptMoveInput(false);
      engineApi.humanMove.inputMovePoints(null);
      actionButtonClicked();
    }
  }

  public void showMove(int showMoveStartPoint) {

    MoveOutput moveOutput = new MoveOutput(engineApi.getSelectedMove());

    moveOutput.writeMove();
    moveOutput.showMove(
      showMoveStartPoint,
      engineApi.getSelectedMove().getMovePoints().length-1,
      runWhenNotified(this::endTurn)
    );
  }

  private boolean noMoveAutoCompletion () {

    return
      !getSettings().isAutoCompleteMoves() &&
      !getSettings().isAutoCompletePartMoves();
  }

  public void humanMove() {

    getActionButton().setHideActionButton(true);
    if (engineApi.getSelectedTurn().getNrOfMoves() > 1 || noMoveAutoCompletion()) {
      System.out.println("Get human input");
      getMouse().setMoveInput(new MoveInput());
      getMouse().setAcceptMoveInput(true);
      engineApi.humanMove.inputSelectedTurn();
      if (getSettings().isAutoCompletePartMoves()) {

        //new input output auto-move
//        engineApi.humanMove.initialSelection();

        //old input output auto-move
        getMouse().getMoveInput().initialAutoMove(runWhenNotified(() -> {
          if (getMouse().getMoveInput().endOfInputReached()) {
            endTurn();
          }
        }));
      }
    } else {
      showMove(0);
    }
  }

  private void computerMove () {

    showMove(0);
  }

  private void noMove () {

    new MoveOutput(engineApi.getSelectedMove()).outputMove();
    endTurn();
  }

  void move() {

    if (!engineApi.getSelectedMove().isIllegal()) {
      if (!autoCompleteGame && engineApi.turnsPlayerIsHuman(engineApi.getLatestTurn())) {
        humanMove();
      } else {
        computerMove();
      }
    } else {
      noMove();
    }
  }

  public void humanMoveNew () {

    engineApi
      .humanMove
      .inputSelectedTurn();
    getActionButton()
      .setHideActionButton(
        engineApi
          .humanMove
          .inputReady()
      );
  }

  private boolean playerIsHuman () {

    return
      !autoCompleteGame
      && engineApi
        .turnsPlayerIsHuman(
          engineApi.getLatestTurn()
        );
  }

  private void computerMoveNew () {

    engineApi.layoutOutput.outputLayouts(
      new ArrayList<>(
        engineApi
          .getSelectedMove()
          .getMoveLayouts()
      ),
      runWhenNotified(this::endTurn)
    );
  }

  void moveNew () {

    if (playerIsHuman()) {
      humanMoveNew();
    } else {
      computerMoveNew();
    }
  }

  private void rollAndMove () {

    if (game.getNrOfTurns() == 0) {
      game.nextTurn(0, 0);
    } else {
      game.nextTurn(
        engineApi.selection.selectedTurn,
        engineApi.selection.selectedMove
      );
    }
    engineApi.selection.selectedTurn = game.getLatestTurnNr();
    engineApi.selection.selectedMove = 0;
    Main.sound.playSoundEffect("wuerfelbecher");
    engineApi.getSearch().searchRolledMoves();
    getActionButton().setHideActionButton(true);
    getActionButton().setShowPleaseWaitButton(false);
    move();
  }

  private void initMatch () {

    Main.getLayoutEditor().endEdit();
    matchLayout = new Layout(win.canvas.getDisplayedLayout());
    matchLayout.setPlayerID(settings.getGameStartMode());
    matchLayout.setUseWhiteBot(settings.getWhiteBotOpponent());
    matchLayout.setUseBlackBot(settings.getBlackBotOpponent());
  }

  private void initGame () {

    scoreBoard.writeMatchScore();
    win.canvas.setDisplayedLayout(new Layout(matchLayout));
  }

  private void startNewGame() {

    game = new Game(matchLayout);
    rollAndMove();
  }

  private void newGame () {

    if (!engineApi.gameIsPlaying()) {
      initMatch();
    } else {
      initGame();
    }
    startNewGame ();
  }

  public void actionButtonClicked () {

    if (engineApi.gameIsPlaying()) {
      engineApi.getMatchCube().computerHandlesCube();
      if (engineApi.getMatchCube().cubeWasRejected()) {
        endTurn();
        return;
      }
    }
    if (scoreBoard.matchOver()) {
      engineApi.matchPlay = new MatchPlay();
    } else if (engineApi.gameOver()) {
      newGame();
    } else {
      rollAndMove();
    }
  }

}
