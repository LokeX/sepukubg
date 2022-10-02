package engine.api;

import engine.play.humanMove.HumanMove;
import engine.play.score.MatchBoard;
import engine.play.score.ScoreBoard;
import engine.core.Game;
import engine.play.GameState;
import engine.play.MatchCube;
import engine.play.PlayMatch;
import engine.play.MoveOutput;

public class PlaySepuku {

  private Settings settings = new Settings();
  private Scenarios scenarios = new Scenarios();
  private ScenarioInfoHTML scenarioInfoHTML = new ScenarioInfoHTML(this);
  private GameInfoHTML gameInfoHTML = new GameInfoHTML();
  private PlayMatch playMatch = new PlayMatch(this);
  private StateOfPlay stateOfPlay =  new StateOfPlay(this);
  private StateEdit stateEdit = new StateEdit(this);

  public ScenarioInfoHTML getScenarioInfoHTML () {
    
    return
      scenarioInfoHTML;
  }
  
  public HumanMove getHumanMove () {
   
   return
     playMatch.getHumanMove();
 }
 
  public Scenarios getScenarios () {

    return scenarios;
  }
  
  public Settings getSettings () {
    
    return settings;
  }
  
  public void setSettings (Settings settings) {
    
    this.settings = settings;
  }

  public StateOfPlay getPlayState() {

    return stateOfPlay;
  }

  public int[] getUsedDicePattern () {

    return
      playMatch != null
      ? playMatch.getUsedDicePattern()
      : null;
  }

  public Game getGame () {

    return
      playMatch.gameIsPlaying()
      ? playMatch.getGameState()
      : null;
    
  }

  public GameInfoHTML getGameInfoHTML() {

    return
      gameIsPlaying()
      ? gameInfoHTML.getGameDataHTML(
          getMatchPlay().getGameInfo()
        )
      : null;
  }

  public PlayMatch getMatchPlay() {

    return playMatch;
  }

  public GameState getGameState () {

    return playMatch.getGameState();
  }

  public MatchCube getMatchCube () {

    return playMatch.getMatchCube();
  }

  public StateEdit getInput () {

    return
      stateEdit;
  }

  public boolean gameIsPlaying () {

    return
      playMatch
        .gameIsPlaying();
  }

 public MoveOutput getMoveOutput () {

    return
      playMatch != null
      ? playMatch.getMoveOutput()
      : null;
 }

  public ScoreBoard getScoreBoard() {

    return
      playMatch.getScoreBoard();
  }

  public MatchBoard getMatchBoard() {

    return playMatch.getMatchBoard();
  }

  public boolean getAutoCompleteGame () {

    return playMatch.getAutoCompleteGame();
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    playMatch.setAutoCompleteGame(autoComplete);
  }

  public boolean gameOver () {

    return
      playMatch.gameOver();
  }

  private boolean isNewMatch () {
    
    return
      getPlayState()
        .nextPlayTitle()
        .equals("New match");
  }
  
  public void startNewMatch() {
    
    playMatch = new PlayMatch(this);
    getScenarios().setEditing(true);
    getMoveOutput().setOutputLayout(
      getScenarios().getSelectedScenariosLayout()
    );
  }
  
  private void nextPlay () {
    
    if (isNewMatch()) {
      startNewMatch();
    } else {
      playMatch.nextMatchPlay();
    }
  }
  
  public void execNextPlay () {
    
    new Thread(this::nextPlay).start();
  }
  
}
