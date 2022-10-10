package engine.api;

import engine.core.Cube;
import engine.play.*;
import engine.play.humanMove.HumanMove;
import engine.play.score.MatchBoard;
import engine.play.score.ScoreBoard;
import java.util.List;

public class SepukuPlay {

  private Settings settings = new Settings();
  private Scenarios scenarios = new Scenarios();
  private ScenarioInfoHTML scenarioInfoHTML = new ScenarioInfoHTML(this);
  private GameInfoHTML gameInfoHTML = new GameInfoHTML();
  private MatchPlay matchPlay = new MatchPlay(this);
  private StateOfPlay stateOfPlay =  new StateOfPlay(this);
  private StateEdit stateEdit = new StateEdit(this);

  public Cube gameCube () {
    
    return
      matchPlay.gameState().gameCube();
  }
  
  public Cube turnCube () {
    
    return 
      matchPlay.selectedTurn().turnCube();
  }
  
  public int nrOfTurns () {
    
    return
      gameIsPlaying()
      ? matchPlay.gameState().nrOfTurns()
      : 0;
  }
  
  public int[] dice () {
    
    return
      matchPlay.selectedTurn().getDice();
  }
  
  public List<String> moveBonuses () {
    
    return
      matchPlay.getMoveBonuses();
  }
  
  public Navigation navigation () {
    
    return
      matchPlay.navigation();
  }
  
  public ScenarioInfoHTML getScenarioInfoHTML () {
    
    return
      scenarioInfoHTML;
  }
  
  public Search search() {
    
    return
      matchPlay.search();
  }
  
  public HumanMove humanMove() {
   
   return
     matchPlay.humanMove();
 }
 
  public Scenarios scenarios() {

    return scenarios;
  }
  
  public Settings settings() {
    
    return settings;
  }
  
  public void setSettings (Settings settings) {
    
    this.settings = settings;
  }

  public StateOfPlay stateOfPlay () {

    return stateOfPlay;
  }

  public int[] getUsedDicePattern () {

    return
      gameIsPlaying()
      ? matchPlay.getUsedDicePattern()
      : null;
  }

  public GameInfoHTML getGameInfoHTML() {

    return
      gameIsPlaying()
      ? gameInfoHTML.getGameDataHTML(
          matchPlay().getGameInfo()
        )
      : null;
  }

  MatchPlay matchPlay() {

    return matchPlay;
  }

  public MatchCube matchCube() {

    return matchPlay.matchCube();
  }

  public StateEdit stateEdit() {

    return
      stateEdit;
  }

  public boolean gameIsPlaying () {

    return
      matchPlay
        .gameIsPlaying();
  }

 public MoveOutput moveOutput() {

    return
      matchPlay != null
      ? matchPlay.getMoveOutput()
      : null;
 }

  public ScoreBoard scoreBoard() {

    return
      matchPlay.scoreBoard();
  }

  public MatchBoard matchBoard() {

    return matchPlay.matchBoard();
  }

  public boolean autoCompleteGame() {

    return matchPlay.autoCompleteGame();
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
      stateOfPlay()
        .nextPlayTitle()
        .equals("New match");
  }
  
  public void newMatch() {
    
    matchPlay = new MatchPlay(this);
    scenarios().setEditing(true);
    moveOutput().setOutputLayout(
      scenarios().getSelectedScenariosLayout()
    );
  }
  
  private void nextPlay () {
    
    if (isNewMatch()) {
      newMatch();
    } else {
      matchPlay.nextMatchPlay();
    }
  }
  
  public void execNextPlay () {
    
    new Thread(this::nextPlay).start();
  }
  
}
