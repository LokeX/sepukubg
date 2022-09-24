package bg.engine.play;

import bg.engine.api.Settings;
import bg.engine.api.Sepuku;
import bg.engine.core.Turn;
import bg.engine.core.moves.EvaluatedMove;
import bg.engine.play.humanMove.HumanMove;
import bg.engine.play.score.MatchBoard;
import bg.engine.play.score.ScoreBoard;
import bg.engine.core.moves.Layout;

import java.util.Arrays;
import java.util.List;

import static bg.util.ThreadUtil.runWhenNotified;

public class MatchPlay {

  private GameInfo gameInfo;
  private ScoreBoard scoreBoard;
  private MatchCube matchCube;
  private Sepuku sepuku;
  private Layout scenario;
  private MatchBoard matchBoard;
  private GameState gameState;
  private Search search;
  private HumanMove humanMove;
  private MoveOutput moveOutput;
  private boolean autoCompleteGame = false;

  public MatchPlay(Sepuku sepuku) {

    this.sepuku = sepuku;
    matchCube = new MatchCube(this);
    matchBoard = new MatchBoard(settings().getScoreToWin());
    scoreBoard = new ScoreBoard(this);
    search = new Search(this);
    humanMove = new HumanMove(this);
    moveOutput = new MoveOutput();
    gameInfo = new GameInfo(this);
  }
  
  public Layout getScenario () {
    
    return scenario;
  }
  
  public Settings settings () {
    
    return
      sepuku.getSettings();
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
        .getMoveBonuses(getSelectedMove())
        .getMoveBonusList(
          sepuku
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
  
  public EvaluatedMove getSelectedMove() {
    
    return
      gameIsPlaying()
      ? getSelectedTurn()
        .getMoveByNr(getGameState()
        .getMoveNr())
      : null;
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

  public boolean lastTurnSelected () {

    return
      gameIsPlaying()
      && gameState.lastTurnSelected();
  }

  public boolean matchOver () {

    return
      matchBoard.matchOver()
      || sepuku.getMatchCube().cubeWasRejected();
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
      if (matchCube.cubeWasRejected()) {
        endTurn();
        return true;
      }
    }
    return false;
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
      sepuku.execNextPlay();
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
    getSearch().searchRolledMoves();
    move();
  }
  
  private void initScenario () {

    scenario = new Layout(sepuku.getScenarios().getMatchLayout());
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
    sepuku.getScenarios().setEditing(false);
    gameState = new GameState(this);
    newTurn();
  }
  
}
