package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.Search;
import bg.engine.api.gamePlay.GameState;
import bg.engine.match.moves.Layout;
import bg.inUrFace.canvas.move.MoveOutput;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.inUrFace.mouse.MoveInput;

import java.util.ArrayList;

import static bg.Main.*;
import static bg.util.ThreadUtil.runWhenNotified;

public class MatchState {

  Layout matchLayout;
  ScoreBoard scoreBoard;
  GameState gameState;
  HumanMove humanMove = new HumanMove();
  boolean autoCompleteGame = false;

  public MatchState() {

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

  public GameState getGameState() {

    return gameState;
  }

  public HumanMove getHumanMove () {

    return humanMove;
  }

  public boolean getAutoCompleteGame () {

    return autoCompleteGame;
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    this.autoCompleteGame = autoComplete;
  }

  public Search getSearch () {

    return new Search(gameState);
  }

  public boolean gameIsPlaying () {

    return
      gameState != null
      && gameState.nrOfTurns() > 0;
  }

  boolean playerIsHuman () {

    return
      !autoCompleteGame
      && gameState.humanTurnSelected();
  }

  boolean lastTurnSelected () {

    return
      gameState.lastTurnSelected();
  }

  boolean matchOver () {

    return
      scoreBoard.matchOver();
  }

  boolean gameOver () {

    return
      gameIsPlaying()
      && gameState.gameOver();
  }

  public boolean playedMoveSelected () {

    return
      gameIsPlaying()
      && gameState.playedMoveSelected();
  }

  public void endTurn () {

    getActionButton().setShowPleaseWaitButton(false);
    getActionButton().setHideActionButton(false);
    if (gameOver()) {
      autoCompleteGame = false;
      scoreBoard.writeGameScore(getGameState());
    } else if (settings.isAutomatedEndTurn() || autoCompleteGame) {
      getMoveInputListener().setAcceptMoveInput(false);
      humanMove.setMoveInput(null);
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
      humanMove.setMoveInput(gameState);
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

  public void move() {

    if (!engineApi.getSelectedMove().isIllegal()) {
      if (!autoCompleteGame && gameState.humanTurnSelected()) {
        humanMove();
      } else {
        computerMove();
      }
    } else {
      noMove();
    }
  }

  boolean humanIsMoving () {

    return
      !humanMove.endOfInput();
  }

  public void humanMoveNew () {

    humanMove
      .setMoveInput(
        gameState
      );
    getActionButton()
      .setHideActionButton(
        humanMove.inputReady()
      );
  }

  private void computerMoveNew () {

    engineApi.displayLayouts(
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

  void newTurn() {

    gameState.newTurn();
    Main.sound.playSoundEffect("wuerfelbecher");
    getSearch().searchRolledMoves();
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

    gameState = new GameState(matchLayout);
    newTurn();
  }

  void newGame () {

    if (!gameIsPlaying()) {
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
      engineApi.matchState = new MatchState();
    } else if (engineApi.gameOver()) {
      newGame();
    } else {
      newTurn();
    }
  }

}