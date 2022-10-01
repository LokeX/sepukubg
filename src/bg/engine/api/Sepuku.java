package bg.engine.api;

import bg.engine.play.*;
import bg.engine.play.humanMove.HumanMove;
import bg.engine.play.score.MatchBoard;
import bg.engine.play.score.ScoreBoard;
import bg.engine.core.Game;

public class Sepuku {

  private Settings settings = new Settings();
  private Scenarios scenarios = new Scenarios();
  private ScenarioInfoHTML scenarioInfoHTML = new ScenarioInfoHTML(this);
  private GameInfoHTML gameInfoHTML = new GameInfoHTML();
  private MatchPlay matchPlay = new MatchPlay(this);
  private PlayState playState =  new PlayState(this);
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

  public PlayState getPlayState() {

    return playState;
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
