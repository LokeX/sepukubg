package engine.play;

import engine.api.Settings;
import engine.api.SepukuPlay;
import engine.core.Turn;
import engine.core.moves.EvaluatedMove;
import engine.play.game.GameInfo;
import engine.play.game.GameState;
import engine.play.humanMove.HumanMove;
import engine.play.score.MatchBoard;
import engine.play.score.ScoreBoard;
import engine.core.moves.Layout;

import java.util.List;

import static util.ThreadUtil.runWhenNotified;

public class MatchPlay {

  private Navigation navigation;
  private UsedDice usedDice;
  private GameInfo gameInfo;
  private ScoreBoard scoreBoard;
  private MatchCube matchCube;
  private SepukuPlay sepukuPlay;
  private Layout scenario;
  private MatchBoard matchBoard;
  private GameState gameState;
  private Search search;
  private HumanMove humanMove;
  private MoveOutput moveOutput;
  private boolean autoCompleteGame = false;

  public MatchPlay(SepukuPlay sepukuPlay) {

    this.sepukuPlay = sepukuPlay;
    matchCube = new MatchCube(this);
    matchBoard = new MatchBoard(settings().getScoreToWin());
    scoreBoard = new ScoreBoard(this);
    search = new Search(this);
    humanMove = new HumanMove(this);
    moveOutput = new MoveOutput();
    gameInfo = new GameInfo(this);
    usedDice = new UsedDice(this);
    navigation = new Navigation(this);
  }
  
  public Navigation navigation () {
    
    return
      navigation;
  }
  
  public Layout getScenario () {
    
    return scenario;
  }
  
  public Settings settings () {
    
    return
      sepukuPlay.settings();
  }

  public MoveOutput getMoveOutput() {

    return
      moveOutput;
  }

  public ScoreBoard scoreBoard() {

    return scoreBoard;
  }

  public MatchCube matchCube() {

    return matchCube;
  }

  public GameInfo getGameInfo() {

    return gameInfo.getGameData();
  }

  public Search search() {

    return search;
  }

  public MatchBoard matchBoard() {

    return matchBoard;
  }

  public GameState gameState() {

    return gameState;
  }

  public HumanMove humanMove() {

    return
      humanMove;
  }

  public boolean autoCompleteGame() {

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
  
  public Turn getLatestTurn () {
    
    return
      gameIsPlaying()
      ? gameState().lastTurn()
      : null;
  }
  
  public List<String> getMoveBonuses () {
    
    return
      gameIsPlaying()
      ? selectedTurn()
        .getMoveBonuses(selectedMove())
        .getMoveBonusList(
          sepukuPlay
            .settings()
            .getBonusDisplayMode()
        )
      : null;
  }
  
  public boolean humanTurnSelected() {
    
    return
      gameState()
        .humanTurnSelected();
  }
  
  public int getPlayerOnRollsID () {
    
    return getLatestTurn().getPlayerOnRollsID();
  }
  
  public int getPlayedMoveNr () {
    
    return selectedTurn().getPlayedMoveNr();
  }
  
  public int getNrOfTurns () {
    
    return
      gameIsPlaying()
      ? gameState().nrOfTurns()
      : 0;
  }
  
  public Turn selectedTurn() {
    
    return
      gameIsPlaying()
      ? gameState().selectedTurn()
      : null;
  }
  
  public EvaluatedMove selectedMove() {
    
    return
      gameIsPlaying()
      ? gameState().selectedMove()
      : null;
  }
  
  public int[] getUsedDicePattern () {

    return
      usedDice.getUsedDicePattern();
  }
  
  public boolean playerIsHuman () {

    return
      !autoCompleteGame
      && gameIsPlaying()
      && gameState.humanTurnSelected();
  }

  public boolean lastTurnSelected () {

    return
      gameIsPlaying()
      && gameState.lastTurnSelected();
  }

  public boolean matchOver () {

    return
      matchBoard.matchOver();
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
  
  public boolean cubeWasRejected () {
    
    if (gameIsPlaying()) {
      matchCube.computerHandlesCube();
      return matchCube.cubeWasRejected();
    }
    return false;
  }
  
  private boolean isLegalHumanMove () {
    
    return
      playerIsHuman() && !selectedMove().isIllegal();
  }
  
  public void endTurn () {

    if (isLegalHumanMove()) {
      humanMove.setPlayedMoveToSelectedMove();
    }
    if (gameOver()) {
      autoCompleteGame = false;
      matchBoard.addGameScore(gameState().getGameScore());
    } else if (settings().isAutomatedEndTurn() || autoCompleteGame) {
      humanMove.endMove();
      sepukuPlay.execNextPlay();
    }
  }

  private void computerMove () {

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

  public void move() {

    if (selectedMove().isIllegal()) {
      endTurn();
    } else if (playerIsHuman() && !autoCompleteGame) {
      humanMove.startMove();
    } else {
      computerMove();
    }
  }

  public void startNewTurn() {

    if (isLegalHumanMove()) {
      humanMove.playMove();
    }
    gameState.newTurn();
    search().searchRolledMoves();
    move();
  }
  
  private void initScenario () {

    scenario = new Layout(sepukuPlay.scenarios().getMatchLayout());
    scenario.setPlayerID(settings().getGameStartMode());
    scenario.setUseWhiteBot(settings().getWhiteBotOpponent());
    scenario.setUseBlackBot(settings().getBlackBotOpponent());
  }
  
  public void startNewGame() {

    if (gameIsPlaying()) {
      matchBoard.mergeScores();
    }
    initScenario();
    getMoveOutput().setOutputLayout(scenario);
    sepukuPlay.scenarios().setEditing(false);
    gameState = new GameState(this);
    startNewTurn();
  }
  
  public void nextMatchPlay () {
  
    if (!gameIsPlaying() || gameOver()) {
      startNewGame();
    } else if (!cubeWasRejected()){
      startNewTurn();
    } else {
      endTurn();
    }
  }
  
}
