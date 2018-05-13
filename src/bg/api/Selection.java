package bg.api;

import bg.engine.Match;
import bg.engine.Turn;
import bg.engine.moves.EvaluatedMove;

import static bg.Main.getActionButton;
import static bg.Main.settings;

public class Selection extends Match {

  protected int selectedTurn = 0;
  protected int selectedMove = 0;

  public Selection() {

  }

  public Selection(MatchApi matchApi) {

    super(matchApi);
    selectedTurn = matchApi.selectedTurn;
    selectedMove = matchApi.selectedMove;
  }

  public boolean turnsPlayerIsHuman (Turn turn) {

    return settings.playerIsHuman(turn.getPlayerOnRollsID());
  }

  public Turn getTurnByNr (int turnNr) {

    return getGame().getTurnByNr(turnNr);
  }

  public int getPlayedMoveNr () {

    return getSelectedTurn().getPlayedMoveNr();
  }

  public int getNrOfTurns () {

    return getGame() != null ? getGame().getNrOfTurns() : 0;
  }

  public Turn getSelectedTurn() {

    return getNrOfTurns() > 0 ? getTurnByNr(selectedTurn) : null;
  }

  public int getSelectedTurnNr () {

    return selectedTurn;
  }

  public int getLatestTurnNr () {

    return getLatestTurn().getTurnNr();
  }

  public EvaluatedMove getPlayedMove () {

    return getGame().getLatestTurn().getPlayedMove();
  }

  public EvaluatedMove getSelectedMove() {

    return getSelectedTurn().getMoveByNr(selectedMove);
  }

  public void setSelectedMove (int moveNr) {

    selectedMove = moveNr;
  }

  public void setPlayedMove (int moveNr) {

    getSelectedTurn().setPlayedMoveNr(moveNr);
  }

  public int getSelectedMoveNr () {

    return selectedMove;
  }

  public Turn selectNextTurn () {

    getActionButton().setHideActionButton(false);
    if (++selectedTurn == game.getNrOfTurns()) {
      return game.getTurnByNr(--selectedTurn);
    } else {
      selectedMove = getSelectedTurn().getPlayedMoveNr();
      return game.getTurnByNr((selectedTurn));
    }
  }

  public Turn selectPreviousTurn () {

    getActionButton().setHideActionButton(false);
    if (--selectedTurn == -1) {
      return game.getTurnByNr(++selectedTurn);
    } else {
      selectedMove = getSelectedTurn().getPlayedMoveNr();
      return game.getTurnByNr((selectedTurn));
    }
  }

  public Turn selectNextHumanTurn() {

    int turnCount = selectedTurn;

    getActionButton().setHideActionButton(false);
    while (++turnCount < game.getNrOfTurns()) {
      if (turnsPlayerIsHuman(game.getTurnByNr(turnCount))) {
        selectedTurn = turnCount;
        return game.getTurnByNr(selectedTurn);
      }
    }
    return game.getTurnByNr(selectedTurn);
  }

  public Turn selectPreviousHumanTurn() {

    int turnCount = selectedTurn;

    getActionButton().setHideActionButton(false);
    while (--turnCount >= 0) {
      if (turnsPlayerIsHuman(game.getTurnByNr(turnCount))) {
        selectedTurn = turnCount;
        return game.getTurnByNr(selectedTurn);
      }
    }
    return game.getTurnByNr(selectedTurn);
  }

  public Turn selectLatestHumanTurn() {

    getActionButton().setHideActionButton(false);
    if (game.getNrOfTurns() > 1) {
      if (turnsPlayerIsHuman(getLatestTurn())) {
        selectedTurn = game.getLatestTurnNr();
        return getLatestTurn();
      } else if (turnsPlayerIsHuman(game.getTurnByNr(game.getNrOfTurns()-2))) {
        selectedTurn = game.getNrOfTurns()-2;
        return game.getTurnByNr(game.getNrOfTurns()-2);
      }
    }
    return getSelectedTurn();
  }

  public EvaluatedMove selectNextMove () {

    getActionButton().setHideActionButton(false);
    if (++selectedMove < getSelectedTurn().getNrOfMoves()) {
      return getSelectedTurn().getMoveByNr(selectedMove);
    } else {
      selectedMove = 0;
      return getSelectedTurn().getMoveByNr(selectedMove);
    }
  }

  public EvaluatedMove selectPreviousMove () {

    getActionButton().setHideActionButton(false);
    if (--selectedMove >= 0) {
      return getSelectedTurn().getMoveByNr(selectedMove);
    } else {
      selectedMove = getSelectedTurn().getNrOfMoves()-1;
      return getSelectedTurn().getMoveByNr(selectedMove);
    }
  }

}
