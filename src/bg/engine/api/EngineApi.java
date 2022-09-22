package bg.engine.api;

import bg.Settings;
import bg.engine.api.matchPlay.ActionState;
import bg.engine.api.matchPlay.GameState;
import bg.engine.api.matchPlay.MatchCube;
import bg.engine.api.matchPlay.MatchPlay;
import bg.engine.api.moveInput.HumanInput;
import bg.engine.api.score.MatchBoard;
import bg.engine.api.score.ScoreBoard;
import bg.engine.coreLogic.Game;
import bg.engine.coreLogic.Turn;
import bg.engine.coreLogic.moves.EvaluatedMove;

import java.util.List;

import static bg.Main.engineApi;

public class EngineApi {

  private Settings settings = new Settings();
  private Scenarios scenarios = new Scenarios();
  private HumanInput humanInput = new HumanInput();
  private GameInfoHTML gameInfoHTML = new GameInfoHTML();
  public MatchPlay matchPlay = new MatchPlay(this);
  private ActionState actionState =  new ActionState(this);

  public void newMatch () {
  
    matchPlay = new MatchPlay(this);
    engineApi.getScenarios().setEditing(false);
    matchPlay.getMoveOutput().setOutputLayout(
      engineApi.getScenarios().getSelectedScenariosLayout()
    );
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

  public ActionState getActionState () {

    return actionState;
  }

  public int[] getUsedDicePattern () {

    return
      matchPlay != null
      ? matchPlay.getUsedDicePattern()
      : null;
  }

  public Game getGame () {

    if (matchPlay != null && matchPlay.getGameState() != null) {
      return matchPlay.getGameState();
    }
    return null;
  }

  public HumanInput getHumanInput () {

    return humanInput.getHumanInput(this);
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

  int getPlayedMoveNr () {

    return getSelectedTurn().getPlayedMoveNr();
  }

  public int getNrOfTurns () {

    return
      matchPlay.getGameState() != null
        ? matchPlay.getGameState().nrOfTurns()
        : 0;
  }

  public Turn getSelectedTurn() {

    return getNrOfTurns() > 0
      ? matchPlay.getGameState().selectedTurn()
      : null;
  }

  public Turn getLatestTurn () {

    return
      matchPlay.getGameState() != null
        ? matchPlay.getGameState().lastTurn()
        : null;
  }

  public EvaluatedMove getSelectedMove() {

    return getSelectedTurn().getMoveByNr(getGameState().getMoveNr());
  }

  public int getPlayerOnRollsID () {

    return getLatestTurn().getPlayerOnRollsID();
  }

  public MatchCube getMatchCube () {

    return matchPlay.getMatchCube();
  }

  public List<String> getMoveBonuses () {

    return
      getNrOfTurns() > 0
        ? getSelectedTurn()
            .getMoveBonuses(getSelectedMove())
            .getMoveBonusList(
              settings.getBonusDisplayMode()
            )
        : null;
  }

  public StateEdit getInput () {

    return new StateEdit();
  }

  public boolean humanTurnSelected() {

    return
      matchPlay
        .getGameState()
        .humanTurnSelected();
  }

  public boolean gameIsPlaying () {

    return
      matchPlay
        .gameIsPlaying();
  }

 public MoveLayoutOutput getMoveOutput () {

    return
      matchPlay != null
      ? matchPlay.getMoveOutput()
      : null;
 }

  public ScoreBoard getScoreBoard() {

    return
      matchPlay.getScoreBoard();
//      scoreBoard.getScoreBoard(matchPlay);
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
      matchPlay.getGameState() == null
      || matchPlay.getGameState().gameOver();
  }

}
