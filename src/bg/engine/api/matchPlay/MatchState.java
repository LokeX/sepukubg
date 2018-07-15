package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.gameState.GameState;
import bg.engine.api.gameState.navigation.humanMove.HumanMove;
import bg.engine.api.score.MatchBoard;
import bg.engine.match.moves.Layout;
import bg.inUrFace.canvas.move.MoveOutput;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.inUrFace.mouse.MoveInput;

import static bg.Main.*;
import static bg.util.ThreadUtil.runWhenNotified;

public class MatchState {

  private Layout matchLayout;
  private MatchBoard matchBoard;
  private GameState gameState;
  private boolean autoCompleteGame = false;

  public MatchState() {

    matchBoard = new MatchBoard(settings.getScoreToWin());
    new ScenarioOutput(scenarios).outputSelectedScenario();
    getActionButton().setShowPleaseWaitButton(false);
    getActionButton().setText("Start Match");
    getActionButton().setHideActionButton(false);
    getLayoutEditor().startEditor();
  }

  MatchState (Layout matchLayout) {

    this.matchLayout = matchLayout;
    matchBoard = new MatchBoard(settings.getScoreToWin());
  }

  public MatchBoard getMatchBoard() {

    return matchBoard;
  }

  public GameState getGameState() {

    return gameState;
  }

  public HumanMove getHumanMove () {

    return
     gameIsPlaying()
      ? gameState.getHumanMove()
      : null;
  }

  public boolean getAutoCompleteGame () {

    return autoCompleteGame;
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    this.autoCompleteGame = autoComplete;
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
      matchBoard.matchOver();
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
      matchBoard.addGameScore(getGameState().getGameScore());
    } else if (settings.isAutomatedEndTurn() || autoCompleteGame) {
      getMoveInputListener().setAcceptMoveInput(false);
      gameState.endHumanMove();
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
      gameState.startHumanMove();
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

  void newTurn() {

    gameState.newTurn();
    Main.sound.playSoundEffect("wuerfelbecher");
    getGameState().getSearch().searchRolledMoves();
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

    matchBoard.mergeScores();
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
    if (matchBoard.matchOver()) {
      engineApi.matchState = new MatchState();
    } else if (engineApi.gameOver()) {
      newGame();
    } else {
      newTurn();
    }
  }

}
