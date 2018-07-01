package bg.engine.api;

import bg.engine.api.gamePlay.GameState;
import bg.engine.api.matchPlay.MatchCube;
import bg.engine.api.matchPlay.MatchState;
import bg.engine.api.matchPlay.ScoreBoard;
import bg.engine.api.score.MatchBoard;
import bg.engine.api.score.ScorePresent;
import bg.engine.match.Game;
import bg.engine.match.Turn;
import bg.engine.match.moves.EvaluatedMove;
import bg.engine.match.moves.Layout;

import java.util.List;

import static bg.Main.*;

public class EngineApi {

  private TurnInfo turnInfo = new TurnInfo();
  private DisplayLayouts displayLayouts = new DisplayLayouts();
  public MatchState matchState = new MatchState();

  public Game getGame () {

    if (matchState != null && matchState.getGameState() != null) {
      return matchState.getGameState();
    }
    return null;
  }

  public void displayLayouts (List<Layout> layouts, Object notifier) {

    displayLayouts.outputLayouts(layouts, notifier);
  }

  public MatchState getMatchState() {

    return matchState;
  }

  public GameState getGameState () {

    return matchState.getGameState();
  }

  Turn getTurnByNr (int turnNr) {

    return getGameState().getTurnByNr(turnNr);
  }

  int getPlayedMoveNr () {

    return getSelectedTurn().getPlayedMoveNr();
  }

  public int getNrOfTurns () {

    return
      matchState.getGameState() != null
        ? matchState.getGameState().nrOfTurns()
        : 0;
  }

  public Turn getSelectedTurn() {

    return getNrOfTurns() > 0
      ? matchState.getGameState().selectedTurn()
      : null;
  }

  public Turn getLatestTurn () {

    return
      matchState.getGameState() != null
        ? matchState.getGameState().lastTurn()
        : null;
  }
  public int getSelectedTurnNr () {

    return getGameState().getTurnNr();
  }

  public int getLatestTurnNr () {

    return getLatestTurn().getTurnNr();
  }

  public EvaluatedMove getSelectedMove() {

    return getSelectedTurn().getMoveByNr(getGameState().getMoveNr());
  }

  public void setSelectedMove (int moveNr) {

    getGameState().setMoveNr(moveNr);
  }

  public int getSelectedMoveNr () {

    return getGameState().getMoveNr();
  }
  public int getPlayerOnRollsID () {

    return getLatestTurn().getPlayerOnRollsID();
  }

  public MatchCube getMatchCube () {

    return new MatchCube(this);
  }

  public String getTurnInfoFormatString () {

    return
      turnInfo
        .HTMLFormattedInfoString();
  }

  public boolean humanInputReady () {

    return
      matchState.getHumanMove().inputReady();
  }

  public List<String> getMoveBonuses () {

    return
      getNrOfTurns() > 0
      ? getSelectedTurn()
          .getMoveBonuses(
            getSelectedMove()
          )
          .getMoveBonusList(
            settings
              .getBonusDisplayMode()
          )
      : null;
  }

  public HumanMoveApi getHumanMove () {

    return
      matchState.getHumanMove().inputReady()
        ? matchState.getHumanMove()
        : null;
  }

  public Input getInput () {

    return new Input();
  }

  public boolean playerIsComputer () {

    return
      getSettings()
        .playerStatus[
          getLatestTurn()
            .getPlayerOnRollsID()
        ] == settings.COMPUTER;
  }

  public String getPlayerDescription () {

    return
      getSelectedTurn()
        .getPlayerTitle()
          + (playerIsComputer()
            ? "[Computer]"
            : "[Human]");
  }

  public boolean matchOver () {

    return matchState.getMatchBoard().matchOver();
  }

  public boolean humanTurnSelected() {

    return
      matchState
        .getGameState()
        .humanTurnSelected();
  }

  public boolean gameIsPlaying () {

    return
      matchState
        .gameIsPlaying();
  }

  public ScorePresent getScorePresent () {

    return new ScorePresent(getMatchBoard(), getGameState());
  }

  public MatchBoard getMatchBoard() {

    return matchState.getMatchBoard();
  }

  public boolean getAutoCompleteGame () {

    return matchState.getAutoCompleteGame();
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    matchState.setAutoCompleteGame(autoComplete);
  }

  public boolean gameOver () {

    return
      matchState.getGameState() == null
      || matchState.getGameState().gameOver();
  }

}
