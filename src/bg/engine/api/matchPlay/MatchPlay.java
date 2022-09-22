package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.Settings;
import bg.engine.api.EngineApi;
import bg.engine.api.MoveOutput;
import bg.engine.api.moveInput.HumanMove;
import bg.engine.api.score.MatchBoard;
import bg.engine.api.score.ScoreBoard;
import bg.engine.coreLogic.moves.Layout;
import java.util.Arrays;

import static bg.util.ThreadUtil.runWhenNotified;

public class MatchPlay {

  private GameInfo gameInfo;
  private ScoreBoard scoreBoard;
  private MatchCube matchCube;
  private EngineApi engineApi;
  private Layout scenario;
  private MatchBoard matchBoard;
  private GameState gameState;
  private Search search;
  private HumanMove humanMove;
  private MoveOutput moveOutput;
  private boolean autoCompleteGame = false;

  public MatchPlay(EngineApi engineApi) {

    this.engineApi = engineApi;
    matchCube = new MatchCube(engineApi);
    matchBoard = new MatchBoard(settings().getScoreToWin());
    scoreBoard = new ScoreBoard(this);
    search = new Search(this);
    humanMove = new HumanMove(this);
    moveOutput = new MoveOutput();
    gameInfo = new GameInfo(this);
  }
  
  public Settings settings () {
    
    return
      engineApi.getSettings();
  }

  public MoveOutput getMoveOutput() {

    return
      moveOutput;
  }

  public ScoreBoard getScoreBoard () {

    return scoreBoard;
  }

  public MatchCube getMatchCube () {

    return matchCube;
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
      ? humanMove.getMoveSelection().dicePattern()
      : computerUsedDicePattern();
  }

  private int[] computerUsedDicePattern () {

    int[] dicePattern = null;

    if (gameIsPlaying()) {
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

  public boolean gameOver () {

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

    System.out.println("Ending turn");
    if (playerIsHuman()) {
      humanMove.setPlayedMoveToSelectedMove();
    }
    if (gameOver()) {
      System.out.println("Game Over: adding gameScore");
      autoCompleteGame = false;
      matchBoard.addGameScore(getGameState().getGameScore());
    } else if (settings().isAutomatedEndTurn() || autoCompleteGame) {
      humanMove.endMove();
      actionButtonClicked();
    }
  }

  public void humanMove() {

    System.out.println("In matchState.humanMove");
    humanMove.startMove();
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

    if (!gameState.selectedMove().isIllegal()) {
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
    move();
  }
  
  private void initScenario () {

    scenario = new Layout(engineApi.getScenarios().getMatchLayout());
    scenario.setPlayerID(settings().getGameStartMode());
    scenario.setUseWhiteBot(settings().getWhiteBotOpponent());
    scenario.setUseBlackBot(settings().getBlackBotOpponent());
  }

  public void startGame() {

    System.out.println("Starting game");
    if (gameIsPlaying()) {
      matchBoard.mergeScores();
    }
    initScenario();
    getMoveOutput().setOutputLayout(scenario);
    engineApi.getScenarios().setEditing(false);
    gameState = new GameState(scenario);
    newTurn();
  }

  private boolean isNewMatch () {

    return
      engineApi
        .getPlayState()
        .nextPlayTitle()
        .equals("New match");
  }
  
  public boolean cubeWasRejected () {
  
    if (gameIsPlaying()) {
      matchCube.computerHandlesCube();
      if (matchCube.cubeWasRejected()) {
        endTurn();
        return true;
      }
    }
    
    return false;
  }

  public void actionButtonClicked () {

    if (gameIsPlaying()) {
      matchCube.computerHandlesCube();
      if (matchCube.cubeWasRejected()) {
        endTurn();
        return;
      }
    }
    if (isNewMatch()) {
      engineApi.newMatch();
    } else if (!gameIsPlaying() || gameOver()) {
      startGame();
    } else {
      newTurn();
    }
  }

}
