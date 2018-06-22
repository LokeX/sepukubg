package bg.api;

import bg.engine.Game;
import bg.engine.Turn;
import bg.engine.moves.EvaluatedMove;

import static bg.Main.getActionButton;
import static bg.Main.engineApi;

public class Selection {

  int selectedTurn = 0;
  int selectedMove = 0;

  private Game game () {

    return engineApi.getGame();
  }

  public Turn selectNextTurn () {

    getActionButton().setHideActionButton(false);
    if (++selectedTurn == engineApi.getGame().getNrOfTurns()) {
      return game().getTurnByNr(--selectedTurn);
    } else {
      selectedMove = engineApi.getSelectedTurn().getPlayedMoveNr();
      return game().getTurnByNr((selectedTurn));
    }
  }

  public Turn selectPreviousTurn () {

    getActionButton().setHideActionButton(false);
    if (--selectedTurn == -1) {
      return game().getTurnByNr(++selectedTurn);
    } else {
      selectedMove = engineApi.getSelectedTurn().getPlayedMoveNr();
      return game().getTurnByNr((selectedTurn));
    }
  }

  public Turn selectNextHumanTurn() {

    int turnCount = selectedTurn;

    getActionButton().setHideActionButton(false);
    while (++turnCount < game().getNrOfTurns()) {
      if (engineApi.turnsPlayerIsHuman(game().getTurnByNr(turnCount))) {
        selectedTurn = turnCount;
        return game().getTurnByNr(selectedTurn);
      }
    }
    return game().getTurnByNr(selectedTurn);
  }

  public Turn selectPreviousHumanTurn() {

    int turnCount = selectedTurn;

    getActionButton().setHideActionButton(false);
    while (--turnCount >= 0) {
      if (engineApi.turnsPlayerIsHuman(game().getTurnByNr(turnCount))) {
        selectedTurn = turnCount;
        return game().getTurnByNr(selectedTurn);
      }
    }
    return game().getTurnByNr(selectedTurn);
  }

  public Turn selectLatestHumanTurn() {

    getActionButton().setHideActionButton(false);
    if (game().getNrOfTurns() > 1) {
      if (engineApi.turnsPlayerIsHuman(engineApi.getLatestTurn())) {
        selectedTurn = game().getLatestTurnNr();
        return engineApi.getLatestTurn();
      } else if (engineApi.turnsPlayerIsHuman(game().getTurnByNr(game().getNrOfTurns()-2))) {
        selectedTurn = game().getNrOfTurns()-2;
        return game().getTurnByNr(game().getNrOfTurns()-2);
      }
    }
    return engineApi.getSelectedTurn();
  }

  public EvaluatedMove selectNextMove () {

    getActionButton().setHideActionButton(false);
    if (++selectedMove < engineApi.getSelectedTurn().getNrOfMoves()) {
      return engineApi.getSelectedTurn().getMoveByNr(selectedMove);
    } else {
      selectedMove = 0;
      return engineApi.getSelectedTurn().getMoveByNr(selectedMove);
    }
  }

  public EvaluatedMove selectPreviousMove () {

    getActionButton().setHideActionButton(false);
    if (--selectedMove >= 0) {
      return engineApi.getSelectedTurn().getMoveByNr(selectedMove);
    } else {
      selectedMove = engineApi.getSelectedTurn().getNrOfMoves()-1;
      return engineApi.getSelectedTurn().getMoveByNr(selectedMove);
    }
  }

}
