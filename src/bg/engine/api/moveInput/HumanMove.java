package bg.engine.api.moveInput;

import bg.engine.api.MoveLayoutOutput;
import bg.engine.api.matchPlay.GameState;
import bg.engine.api.matchPlay.MatchPlay;
import bg.engine.coreLogic.moves.Layout;
import bg.engine.coreLogic.moves.MoveLayout;

import static bg.Main.getSettings;
import static bg.util.ThreadUtil.runWhenNotified;

public class HumanMove {

  private MatchPlay matchState;
  private MoveSelection moveSelection;

  public HumanMove (MatchPlay matchState) {

    this.matchState = matchState;
  }

  public void playMove () {

    if (getSelectedMove() != null) {
      gameState().setMoveNr(
        getSelectedMoveNr()
      );
      gameState()
        .selectedTurn()
        .getMoveByNr(getSelectedMoveNr())
        .setMovePoints(moveSelection.getMovePoints());
      endMove();
    }
  }

  public MoveLayout getSelectedMove () {

    return
      moveSelection != null && getSelectedMoveNr() != -1
      ? moveSelection.getMoveLayout(getSelectedMoveNr())
      : null;
  }

  public int getSelectedMoveNr () {

    return
      moveSelection != null && moveSelection.isUniqueMove()
      ? moveSelection.getUniqueEvaluatedMoveNr()
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

  private void setInitialOutputLayout() {

    moveOutput()
      .setOutputLayout(
        new Layout(
          moveSelection
            .getParentMoveLayout()
        )
      );
  }

  private void startMoveSelection () {

    moveSelection = new MoveSelection(
      gameState().selectedTurn()
    );
  }

  public void startMove () {

    System.out.println("Starting human move:");
    startMoveSelection();
//    setInitialOutputLayout();
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
