package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.EngineApi;
import bg.engine.api.moveInput.HumanMove;
import bg.engine.api.MoveLayoutOutput;
import bg.engine.api.score.MatchBoard;
import bg.engine.api.score.ScoreBoard;
import bg.engine.coreLogic.moves.Layout;

import java.util.Arrays;

import static bg.Main.*;
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
  private MoveLayoutOutput moveOutput;
  private boolean autoCompleteGame = false;

  public MatchPlay(EngineApi engineApi) {

    this.engineApi = engineApi;
    matchCube = new MatchCube(engineApi);
    matchBoard = new MatchBoard(settings.getScoreToWin());
    scoreBoard = new ScoreBoard(this);
    search = new Search(this);
    humanMove = new HumanMove(this);
    moveOutput = new MoveLayoutOutput();
    gameInfo = new GameInfo(this);
  }

  public MoveLayoutOutput getMoveOutput() {

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

    if (playerIsHuman()) {
      humanMove.setPlayedMoveToSelectedMove();
    }
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
    scenario.setPlayerID(settings.getGameStartMode());
    scenario.setUseWhiteBot(settings.getWhiteBotOpponent());
    scenario.setUseBlackBot(settings.getBlackBotOpponent());
  }

  private void startGame() {

    System.out.println("Starting game");
    if (gameIsPlaying()) {
      matchBoard.mergeScores();
    }
    initScenario();
    engineApi.getScenarios().setEditing(false);
    gameState = new GameState(scenario);
    newTurn();
  }

  private boolean newMatch () {

    return
      engineApi
        .getActionState()
        .nextPlayTitle()
        .equals("New match");
  }

  public void actionButtonClicked () {

    if (gameIsPlaying()) {
      matchCube.computerHandlesCube();
      if (matchCube.cubeWasRejected()) {
        endTurn();
        return;
      }
    }
    if (newMatch()) {
      engineApi.getScenarios().setEditing(true);
      engineApi.matchPlay = new MatchPlay(engineApi);
    } else if (!gameIsPlaying() || gameOver()) {
      startGame();
    } else {
      newTurn();
    }
  }

}
