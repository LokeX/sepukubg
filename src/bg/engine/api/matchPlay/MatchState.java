package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.gamePlay.GameInfo;
import bg.engine.api.gamePlay.GameState;
import bg.engine.api.gamePlay.Search;
import bg.engine.api.moveInput.HumanMove;
import bg.engine.api.MoveLayoutOutput;
import bg.engine.api.score.MatchBoard;
import bg.engine.coreLogic.moves.Layout;
import bg.inUrFace.canvas.scenario.ScenarioOutput;

import java.util.Arrays;

import static bg.Main.*;
import static bg.util.ThreadUtil.runWhenNotified;

public class MatchState {

  private GameInfo gameInfo;
  private Layout matchLayout;
  private MatchBoard matchBoard;
  private GameState gameState;
  private ActionState actionState;
  private Search search;
  private HumanMove humanMove;
  private MoveLayoutOutput moveOutput;
  private boolean autoCompleteGame = false;

  public MatchState() {

    matchBoard = new MatchBoard(settings.getScoreToWin());
    actionState = new ActionState(this);
    search = new Search(this);
    humanMove = new HumanMove(this);
    moveOutput = new MoveLayoutOutput();
    gameInfo = new GameInfo(this);
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

  public MoveLayoutOutput getMoveOutput() {

    return
      moveOutput;
  }

  public GameInfo getGameInfo() {

    return gameInfo.getGameData();
  }

  public Search getSearch() {

    return search;
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
      humanMove;
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

  public int[] getUsedDicePattern () {

    return
      playerIsHuman() && humanMove.getMoveSelection() != null
      ? humanMove.getMoveSelection().diePattern()
      : computerUsedDicePattern();
  }

  private int[] computerUsedDicePattern () {

    int[] dicePattern = null;

    if (gameState != null && gameState.selectedTurn() != null) {
      dicePattern = new int[gameState.selectedTurn().getDice().length];
      Arrays.fill(dicePattern,1);
    }
    return dicePattern;
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
      humanMove.endMove();
      actionButtonClicked();
    }
  }

  public void humanMove() {

    System.out.println("In matchState.humanMove");
    humanMove.startMove();
    getActionButton().setHideActionButton(true);
  }

  private void computerMove () {

    System.out.println("Computer move");
    moveOutput.setEndOfOutputNotifier(
      runWhenNotified(this::endTurn)
    );
    moveOutput.setOutputLayouts(
      gameState
        .selectedMove()
        .getMovePointLayouts()
        .stream()
        .map(Layout::new)
        .toList()
    );
  }

  private void noMove () {

    System.out.println("No move");
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

  public void newTurn() {

    if (humanMove.inputReady()) {
      humanMove.playMove();
    }
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
