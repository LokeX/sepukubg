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
  }
  
  public Layout getScenario () {
    
    return scenario;
  }
  
  public Settings settings () {
    
    return
      sepukuPlay.getSettings();
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
  
  public Turn getLatestTurn () {
    
    return
      gameIsPlaying()
      ? getGameState().lastTurn()
      : null;
  }
  
  public List<String> getMoveBonuses () {
    
    return
      gameIsPlaying()
      ? getSelectedTurn()
        .getMoveBonuses(selectedMove())
        .getMoveBonusList(
          sepukuPlay
            .getSettings()
            .getBonusDisplayMode()
        )
      : null;
  }
  
  public boolean humanTurnSelected() {
    
    return
      getGameState()
        .humanTurnSelected();
  }
  
  public int getPlayerOnRollsID () {
    
    return getLatestTurn().getPlayerOnRollsID();
  }
  
  public int getPlayedMoveNr () {
    
    return getSelectedTurn().getPlayedMoveNr();
  }
  
  public int getNrOfTurns () {
    
    return
      gameIsPlaying()
      ? getGameState().nrOfTurns()
      : 0;
  }
  
  public Turn getSelectedTurn() {
    
    return
      gameIsPlaying()
      ? getGameState().selectedTurn()
      : null;
  }
  
  public EvaluatedMove selectedMove() {
    
    return
      gameIsPlaying()
      ? getSelectedTurn()
        .getMoveByNr(getGameState()
        .getMoveNr())
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
      matchBoard.addGameScore(getGameState().getGameScore());
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
    getSearch().searchRolledMoves();
    move();
  }
  
  private void initScenario () {

    scenario = new Layout(sepukuPlay.getScenarios().getMatchLayout());
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
    sepukuPlay.getScenarios().setEditing(false);
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
