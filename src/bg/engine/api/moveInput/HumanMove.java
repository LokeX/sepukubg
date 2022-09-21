package bg.engine.api.moveInput;

import bg.engine.api.MoveLayoutOutput;
import bg.engine.api.matchPlay.GameState;
import bg.engine.api.matchPlay.MatchPlay;

import static bg.Main.getSettings;
import static bg.util.ThreadUtil.runWhenNotified;

public class HumanMove {

  private MatchPlay matchState;
  private MoveSelection moveSelection;

  public HumanMove (MatchPlay matchState) {

    this.matchState = matchState;
  }

  public void setPlayedMoveToSelectedMove() {

    gameState()
      .selectedTurn()
      .setPlayedMoveNr(getSelectedMoveNr());
  }

  private void setMovePoints () {

    gameState()
      .selectedTurn()
      .getMoveByNr(getSelectedMoveNr())
      .setMovePoints(moveSelection.getMovePoints());
  }

  private void setSelectedMove () {

    gameState().setMoveNr(getSelectedMoveNr());
  }

  public void playMove () {

    if (hasSelectedMove()) {
      setSelectedMove();
      setPlayedMoveToSelectedMove();
      setMovePoints();
      endMove();
      System.out.println("Played selected move: "+matchState.getGameState().getMoveNr());
    }
  }

  private boolean hasSelectedMove () {

    return
      moveSelection != null
      && moveSelection.isUniqueMove();
  }

  public int getSelectedMoveNr () {

    return
      hasSelectedMove()
      ? moveSelection
        .getUniqueEvaluatedMoveNr()
      : -1;
  }

  public MoveSelection getMoveSelection() {

    return moveSelection;
  }

  private boolean autoCompleteMove () {

    return
      moveSelection.getNrOfMoves() == 1
      && getSettings().isAutoCompleteMoves();
  }

  private boolean autoSelectPartMoves() {

    return
      getSettings().isAutoCompletePartMoves();
  }

  private GameState gameState () {

    return
      matchState.getGameState();
  }

  private MoveLayoutOutput moveOutput () {

    return
      matchState
        .getMoveOutput();
  }

  private void startMoveSelection () {

    moveSelection = new MoveSelection(
      gameState().selectedTurn()
    );
  }

  public void startMove () {

    System.out.println("Starting human move:");
    startMoveSelection();
    System.out.println("Human movePoints:");
    moveSelection.printMovePoints();
    if (moveSelection.noLegalMove()) {
      System.out.println("Human move is illegal:");
      moveSelection = null;
    } else if (autoSelectPartMoves()){
      System.out.println("AutoSelecting partMoves:");
      autoSelectPoints();
      outputMoveLayouts();
    }
  }

  public void endMove () {

    moveSelection = null;
  }

  public boolean inputReady () {

    return
      moveSelection != null
      && !matchState
          .getMoveOutput()
          .hasOutput();
  }

  private void outputMoveLayouts () {

    if (moveSelection.endOfInput()) {
      matchState
        .getMoveOutput()
        .setEndOfOutputNotifier(
          runWhenNotified(matchState::endTurn)
        );
    }
    matchState
      .getMoveOutput()
      .setOutputLayouts(
        moveSelection.getMovePointLayouts()
      );
  }

  private void autoSelectPoints() {

    System.out.println("movePoints before autoSelect: ");
    moveSelection.printMovePoints();
    moveSelection.autoSelectPoints();
    System.out.println("movePoints after autoSelect: ");
    moveSelection.printMovePoints();
  }

  public void pointClicked (int clickedPoint) {

    System.out.println("InputReady: "+inputReady());
    if (inputReady()) {
      System.out.println("Received inputPoint: "+clickedPoint);
      moveSelection.inputPoint(clickedPoint);
      if (clickedPoint != -1 && autoSelectPartMoves()) {
        autoSelectPoints();
      }
      outputMoveLayouts();
    }
  }

}
