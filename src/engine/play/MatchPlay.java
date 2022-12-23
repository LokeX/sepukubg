package engine.play;

import engine.api.Settings;
import engine.api.SepukuPlay;
import engine.core.Turn;
import engine.core.moves.EvaluatedMove;
import engine.play.gamePlay.GamePlayInfo;
import engine.play.gamePlay.GamePlay;
import engine.play.humanMove.HumanMove;
import engine.play.score.MatchBoard;
import engine.play.score.ScoreBoard;
import engine.core.moves.Layout;

import java.util.List;

import static util.ThreadUtil.runWhenNotified;

public class MatchPlay {

  private Navigation navigation;
  private UsedDice usedDice;
  private GamePlayInfo gamePlayInfo;
  private ScoreBoard scoreBoard;
  private MatchCube matchCube;
  private SepukuPlay sepukuPlay;
  private Layout scenario;
  private MatchBoard matchBoard;
  private GamePlay gamePlay;
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
    gamePlayInfo = new GamePlayInfo(this);
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

  public GamePlayInfo getGameInfo() {

    return gamePlayInfo.getGameData();
  }

  public Search search() {

    return search;
  }

  public MatchBoard matchBoard() {

    return matchBoard;
  }

  public GamePlay gamePlay() {

    return gamePlay;
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
      gamePlay != null
      && gamePlay.nrOfTurns() > 0;
  }
  
  public Turn latestTurn() {
    
    return
      gameIsPlaying()
      ? gamePlay().lastTurn()
      : null;
  }
  
  public List<String> moveBonuses() {
    
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
      gamePlay()
        .humanTurnSelected();
  }
  
  public int playerOnRollsID() {
    
    return latestTurn().getPlayerOnRollsID();
  }
  
  public int playedMoveNr() {
    
    return selectedTurn().getPlayedMoveNr();
  }
  
  public int nrOfTurns() {
    
    return
      gameIsPlaying()
      ? gamePlay().nrOfTurns()
      : 0;
  }
  
  public Turn selectedTurn() {
    
    return
      gameIsPlaying()
      ? gamePlay().selectedTurn()
      : null;
  }
  
  public EvaluatedMove selectedMove() {
    
    return
      gameIsPlaying()
      ? gamePlay().selectedMove()
      : null;
  }
  
  public int[] usedDicePattern() {

    return
      usedDice.usedDicePattern();
  }
  
  public boolean playerIsHuman () {

    return
      !autoCompleteGame
      && gameIsPlaying()
      && gamePlay.humanTurnSelected();
  }

  public boolean lastTurnSelected () {

    return
      gameIsPlaying()
      && gamePlay.lastTurnSelected();
  }

  public boolean matchOver () {

    return
      matchBoard.matchOver();
  }

  public boolean gameOver () {

    return
      gameIsPlaying()
      && gamePlay.gameOver();
  }

  public boolean cubeWasRejected () {
    
    if (gameIsPlaying()) {
      matchCube.computerHandlesCube();
      return matchCube.cubeWasRejected();
    }
    return false;
  }
  
  public boolean cubeOffered () {
    
    return
      matchCube.cubeIsOffered();
  }
  
  public boolean cubeRejected () {
    
    return
      matchCube.cubeWasRejected();
  }
  
  private boolean isLegalHumanMove () {
    
    return
      playerIsHuman() && !selectedMove().isIllegal();
  }
  
  public void endTurn () {

    if (isLegalHumanMove()) {
      humanMove.setPlayedMoveToSelectedMove();
    }
    matchCube.setCubeIsOffered(false);
    if (gameOver()) {
      autoCompleteGame = false;
      matchBoard.addGameScore(gamePlay().getGameScore());
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
      gamePlay
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
    gamePlay.newTurn();
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
    autoCompleteGame = false;
    initScenario();
    getMoveOutput().setOutputLayout(scenario);
    sepukuPlay.scenarios().setEditing(false);
    gamePlay = new GamePlay(this);
    startNewTurn();
  }
  
  private boolean computerOffersCube () {
    
    return
      gameIsPlaying() && cubeOffered() && !cubeRejected();
  }
  
  private void humanHandlesCube () {
    
    gamePlay().gameCube().setCubeWasRejected(true);
  }
  
  private boolean isNewGame () {
    
    return
      !cubeOffered() && (!gameIsPlaying() || gameOver());
  }
  
  private boolean isNewTurn () {
    
    return
      !cubeOffered() && !cubeRejected();
  }
  
  public void nextMatchPlay () {
  
    matchCube.computerHandlesCube();
    if (computerOffersCube()) {
      humanHandlesCube();
    } else if (isNewGame()) {
      startNewGame();
    } else if (isNewTurn()){
      startNewTurn();
    } else {
      endTurn();
    }
  }
  
}
