package engine.api;

import engine.play.humanMove.HumanMove;
import engine.play.score.MatchBoard;
import engine.play.score.ScoreBoard;
import engine.core.Game;
import engine.play.GameState;
import engine.play.MatchCube;
import engine.play.MatchPlay;
import engine.play.MoveOutput;

public class SepukuPlay {

  private Settings settings = new Settings();
  private Scenarios scenarios = new Scenarios();
  private ScenarioInfoHTML scenarioInfoHTML = new ScenarioInfoHTML(this);
  private GameInfoHTML gameInfoHTML = new GameInfoHTML();
  private MatchPlay matchPlay = new MatchPlay(this);
  private StateOfPlay stateOfPlay =  new StateOfPlay(this);
  private StateEdit stateEdit = new StateEdit(this);

  public ScenarioInfoHTML getScenarioInfoHTML () {
    
    return
      scenarioInfoHTML;
  }
  
  public HumanMove getHumanMove () {
   
   return
     matchPlay.getHumanMove();
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
      matchPlay != null
      ? matchPlay.getUsedDicePattern()
      : null;
  }

  public Game getGame () {

    return
      matchPlay.gameIsPlaying()
      ? matchPlay.getGameState()
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

  public MatchPlay getMatchPlay() {

    return matchPlay;
  }

  public GameState getGameState () {

    return matchPlay.getGameState();
  }

  public MatchCube getMatchCube () {

    return matchPlay.getMatchCube();
  }

  public StateEdit getInput () {

    return
      stateEdit;
  }

  public boolean gameIsPlaying () {

    return
      matchPlay
        .gameIsPlaying();
  }

 public MoveOutput getMoveOutput () {

    return
      matchPlay != null
      ? matchPlay.getMoveOutput()
      : null;
 }

  public ScoreBoard getScoreBoard() {

    return
      matchPlay.getScoreBoard();
  }

  public MatchBoard getMatchBoard() {

    return matchPlay.getMatchBoard();
  }

  public boolean getAutoCompleteGame () {

    return matchPlay.getAutoCompleteGame();
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    matchPlay.setAutoCompleteGame(autoComplete);
  }

  public boolean gameOver () {

    return
      matchPlay.gameOver();
  }

  private boolean isNewMatch () {
    
    return
      getPlayState()
        .nextPlayTitle()
        .equals("New match");
  }
  
  public void startNewMatch() {
    
    matchPlay = new MatchPlay(this);
    getScenarios().setEditing(true);
    getMoveOutput().setOutputLayout(
      getScenarios().getSelectedScenariosLayout()
    );
  }
  
  private void nextPlay () {
    
    if (isNewMatch()) {
      startNewMatch();
    } else {
      matchPlay.nextMatchPlay();
    }
  }
  
  public void execNextPlay () {
    
    new Thread(this::nextPlay).start();
  }
  
}
