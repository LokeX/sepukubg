package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.gamePlay.GameState;
import bg.engine.api.navigation.moveInput.HumanMove;
import bg.engine.api.score.MatchBoard;
import bg.engine.coreLogic.moves.Layout;
import bg.inUrFace.canvas.move.MoveOutput;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.inUrFace.mouse.MoveInput;

import static bg.Main.*;
import static bg.util.ThreadUtil.runWhenNotified;

public class MatchState {

  private Layout matchLayout;
  private MatchBoard matchBoard;
  private GameState gameState;
  private ActionState actionState;
  private boolean autoCompleteGame = false;

  public MatchState() {

    matchBoard = new MatchBoard(settings.getScoreToWin());
    actionState = new ActionState(this);
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

  public ActionState getActionState() {

    return actionState;
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

  public boolean playerIsHuman () {

    return
      !autoCompleteGame
      && gameIsPlaying()
      && gameState.humanTurnSelected();
  }

  boolean lastTurnSelected () {

    return
      gameState.lastTurnSelected();
  }

  boolean matchOver () {

    return
      matchBoard.matchOver()
      || engineApi.getMatchCube().cubeWasRejected();
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

    System.out.println("Showing move:");
    moveOutput.printMovePoints();
    System.out.println();
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
      System.out.println("Get human inputPoint");
      getMouse().setMoveInput(new MoveInput());
      getMouse().setAcceptMoveInput(true);
      gameState.startHumanMove();
      if (getSettings().isAutoCompletePartMoves()) {

        //new inputPoint output auto-move
//        engineApi.humanMove.initialSelection();

        //old inputPoint output auto-move
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

    System.out.println("Computer move");
//    gameState.startComputerMove();
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

    gameState.endHumanMove();
    gameState.newTurn();
    Main.sound.playSoundEffect("wuerfelbecher");
    getGameState().getSearch().searchRolledMoves();
    getActionButton().setHideActionButton(true);
    getActionButton().setShowPleaseWaitButton(false);
    move();
  }

//  void newTurnNew() {
//
//    gameState.newTurn();
//    Main.sound.playSoundEffect("wuerfelbecher");
//    getGameState().getSearch().searchRolledMoves();
//    getActionButton().setHideActionButton(true);
//    getActionButton().setShowPleaseWaitButton(false);
//    gameState.moveNew();
//  }

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

  void newGameNew () {

    matchBoard.mergeScores();
    win.canvas.setDisplayedLayout(new Layout(matchLayout));
    gameState = new GameState(matchLayout);
    newTurn();
  }

  private boolean startMatch () {

    return
      actionState.nextPlay().equals("Start match");
  }

  private boolean newMatch () {

    return
      actionState.nextPlay().equals("New match");
  }

  public void actionButtonClicked () {

    System.out.println("Next action: "+actionState.nextPlay());
    if (gameIsPlaying()) {
      engineApi.getMatchCube().computerHandlesCube();
      if (engineApi.getMatchCube().cubeWasRejected()) {
        endTurn();
        return;
      }
    }
    if (startMatch() || newMatch()) {
      engineApi.matchState = new MatchState();
    } else if (!gameIsPlaying() || gameOver()) {
      newGame();
    } else {
      newTurn();
    }
    System.out.println("Next action: "+actionState.nextPlay());
  }

}
