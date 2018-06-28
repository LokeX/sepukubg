package bg.engine.api.gamePlay;

import bg.engine.match.Game;
import bg.engine.match.Turn;
import bg.engine.match.moves.EvaluatedMove;
import bg.engine.match.moves.Layout;

import static bg.Main.getActionButton;
import static bg.Main.settings;

public class Navigation extends Game {

  int selectedTurnNr = 0;
  int selectedMoveNr = 0;

  Navigation (Layout matchLayout) {

    super(matchLayout);
  }

  public Turn selectedTurn () {

    return getTurnByNr(selectedTurnNr);
  }

  int playedMoveNr () {

    return selectedTurn().getPlayedMoveNr();
  }

  boolean humanTurn (Turn turn) {

    return
      settings
        .playerIsHuman(
          turn.getPlayerOnRollsID()
        );
  }

  private EvaluatedMove selectedMove () {

    return selectedTurn().getMoveByNr(selectedMoveNr);
  }

  public Turn selectNextTurn () {

    getActionButton().setHideActionButton(false);
    if (++selectedTurnNr == nrOfTurns()) {
      return getTurnByNr(--selectedTurnNr);
    } else {
      selectedMoveNr = playedMoveNr();
      return getTurnByNr((selectedTurnNr));
    }
  }

  public Turn selectPreviousTurn () {

    getActionButton().setHideActionButton(false);
    if (--selectedTurnNr == -1) {
      return getTurnByNr(++selectedTurnNr);
    } else {
      selectedMoveNr = playedMoveNr();
      return getTurnByNr((selectedTurnNr));
    }
  }

  public Turn selectNextHumanTurn() {

    int turnCount = selectedTurnNr;

    getActionButton().setHideActionButton(false);
    while (++turnCount < nrOfTurns()) {
      if (humanTurn(getTurnByNr(turnCount))) {
        selectedTurnNr = turnCount;
        return getTurnByNr(selectedTurnNr);
      }
    }
    return getTurnByNr(selectedTurnNr);
  }

  public Turn selectPreviousHumanTurn() {

    int turnCount = selectedTurnNr;

    getActionButton().setHideActionButton(false);
    while (--turnCount >= 0) {
      if (humanTurn(getTurnByNr(turnCount))) {
        selectedTurnNr = turnCount;
        return getTurnByNr(selectedTurnNr);
      }
    }
    return getTurnByNr(selectedTurnNr);
  }

  public Turn selectLatestHumanTurn() {

    getActionButton().setHideActionButton(false);
    if (nrOfTurns() > 1) {
      if (humanTurn(lastTurn())) {
        selectedTurnNr = lastTurnNr();
        return lastTurn();
      } else if (humanTurn(getTurnByNr(nrOfTurns()-2))) {
        selectedTurnNr = nrOfTurns()-2;
        return selectedTurn();
      }
    }
    return selectedTurn();
  }

  public EvaluatedMove selectNextMove () {

    getActionButton().setHideActionButton(false);
    if (++selectedMoveNr < selectedTurn().getNrOfMoves()) {
      return selectedMove();
    } else {
      selectedMoveNr = 0;
      return selectedMove();
    }
  }

  public EvaluatedMove selectPreviousMove () {

    getActionButton().setHideActionButton(false);
    if (--selectedMoveNr >= 0) {
      return selectedMove();
    } else {
      selectedMoveNr = selectedTurn().getNrOfMoves()-1;
      return selectedMove();
    }
  }

}
