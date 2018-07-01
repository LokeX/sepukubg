package bg.engine.api.gamePlay;

import bg.engine.match.moves.Layout;
import bg.engine.match.score.GameScore;

public class GameState extends Navigation {

  public GameState (Layout matchLayout) {

    super(matchLayout);
  }

  public int getTurnNr () {

    return selectedTurnNr;
  }

  public int getMoveNr () {

    return selectedMoveNr;
  }

  public void setMoveNr (int moveNr) {

    selectedMoveNr = moveNr;
  }

  public boolean playedMoveSelected() {

    return
      selectedMoveNr == playedMoveNr();
  }

  public boolean humanTurnSelected () {

    return
      humanTurn(
        selectedTurn()
      );
  }

  public boolean lastTurnSelected() {

    return
      selectedTurnNr == lastTurnNr();
  }

  public void newTurn () {

    if (nrOfTurns() == 0) {
      nextTurn(0, 0);
    } else {
      selectedTurn()
        .setPlayedMoveNr(
          selectedMoveNr
        );
      nextTurn(
        selectedTurnNr,
        selectedMoveNr
      );
    }
    selectedTurnNr = lastTurnNr();
    selectedMoveNr = 0;
  }

}
