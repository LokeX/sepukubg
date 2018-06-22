package bg.api;

import bg.engine.*;
import bg.engine.moves.EvaluatedMove;

import java.util.List;

import static bg.Main.*;

public class EngineApi {

  private TurnInfoForm turnInfoForm = new TurnInfoForm();
  Selection selection = new Selection();
  HumanMove humanMove = new HumanMove();
  LayoutOutput layoutOutput = new LayoutOutput();
  MatchPlay matchPlay = new MatchPlay();

  public Game getGame () {

    return matchPlay.game;
  }

  public MatchPlay getMatchPlay () {

    return matchPlay;
  }

  public Selection getSelection () {

    return selection;
  }

  public boolean turnsPlayerIsHuman (Turn turn) {

    return settings.playerIsHuman(turn.getPlayerOnRollsID());
  }

  Turn getTurnByNr (int turnNr) {

    return matchPlay.game.getTurnByNr(turnNr);
  }

  int getPlayedMoveNr () {

    return getSelectedTurn().getPlayedMoveNr();
  }

  public int getNrOfTurns () {

    return
      matchPlay.game != null
        ? matchPlay.game.getNrOfTurns()
        : 0;
  }

  public Turn getSelectedTurn() {

    return getNrOfTurns() > 0 ? getTurnByNr(selection.selectedTurn) : null;
  }

  public Turn getLatestTurn () {

    return matchPlay.game != null ? matchPlay.game.getLatestTurn() : null;
  }
  public int getSelectedTurnNr () {

    return selection.selectedTurn;
  }

  public int getLatestTurnNr () {

    return getLatestTurn().getTurnNr();
  }

  public EvaluatedMove getSelectedMove() {

    return getSelectedTurn().getMoveByNr(selection.selectedMove);
  }

  public void setSelectedMove (int moveNr) {

    selection.selectedMove = moveNr;
  }

  public int getSelectedMoveNr () {

    return selection.selectedMove;
  }
  public int getPlayerOnRollsID () {

    return getLatestTurn().getPlayerOnRollsID();
  }

  Search getSearch () {

    return new Search(this);
  }

  public MatchCube getMatchCube () {

    return new MatchCube(this);
  }

  public String getTurnInfoFormatString () {

    return
      turnInfoForm
        .HTMLFormattedInfoString();
  }

  public boolean humanInputReady () {

    return
      humanMove.inputReady();
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

  public HumanMoveApi getMoveInput () {

    return
      humanMove.inputReady()
        ? humanMove
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

    return matchPlay.scoreBoard.matchOver();
  }

  public boolean gameIsPlaying () {

    return matchPlay.game != null && getNrOfTurns() > 0;
  }

  public ScoreBoard getScoreBoard () {

    return matchPlay.scoreBoard;
  }

  public boolean getAutoCompleteGame () {

    return matchPlay.autoCompleteGame;
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    matchPlay.autoCompleteGame = autoComplete;
  }

  public boolean gameOver () {

    return matchPlay.game == null || matchPlay.game.gameOver();
  }

}
