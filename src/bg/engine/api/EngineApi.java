package bg.engine.api;

import bg.engine.api.gameState.GameState;
import bg.engine.api.matchPlay.MatchCube;
import bg.engine.api.matchPlay.MatchState;
import bg.engine.api.gameState.humanMove.HumanMoveApi;
import bg.engine.api.score.MatchBoard;
import bg.engine.api.score.ScoreBoard;
import bg.engine.match.Game;
import bg.engine.match.Turn;
import bg.engine.match.moves.EvaluatedMove;

import java.util.List;

import static bg.Main.*;

public class EngineApi {

  private GameDataHTML gameDataHTML = new GameDataHTML();
  public MatchState matchState = new MatchState();
  private ScoreBoard scoreBoard = new ScoreBoard();
  private DisplayLayouts displayLayouts = new DisplayLayouts();

  public Game getGame () {

    if (matchState != null && matchState.getGameState() != null) {
      return matchState.getGameState();
    }
    return null;
  }

  public GameDataHTML getGameDataHTML () {

    return
      gameDataHTML
        .getGameDataHTML(
          getGameState()
            .getGameData()
        );
  }

  public DisplayLayoutsApi getDisplayLayouts () {

    return displayLayouts;
  }

  public MatchState getMatchState() {

    return matchState;
  }

  public GameState getGameState () {

    return matchState.getGameState();
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

  public HumanMoveApi getHumanMove () {

    return
      matchState != null && matchState.gameIsPlaying()
        ? matchState.getGameState().getHumanMove()
        : null;
  }

  public Input getInput () {

    return new Input();
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

  public ScoreBoard getScoreBoard() {

    return scoreBoard.getScoreBoard(matchState);
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
