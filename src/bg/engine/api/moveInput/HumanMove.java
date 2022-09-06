package bg.engine.api.moveInput;

import bg.engine.api.gamePlay.GameState;

import static bg.Main.getSettings;

public class HumanMove {

  private MoveSelection moveSelection;
  private GameState gameState;

  public HumanMove (GameState gameState) {

    this.gameState = gameState;
  }

  public int selectedMoveNr() {

    return
      moveSelection.noLegalMove() || autoCompleteMove()
      ? 0
      : moveSelection.isUniqueMove()
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

  public void startMoveSelection () {

    moveSelection = new MoveSelection(
      gameState.selectedTurn()
    );
    if (moveSelection.noLegalMove() || autoCompleteMove()) {
      moveSelection = null;
    } else if (autoSelectPartMoves()){
      moveSelection.autoSelect();
    }
  }

  public void endMoveSelection () {

    moveSelection = null;
  }

  public boolean inputReady() {

    return
      moveSelection != null;
  }

  private void selectUniqueMove () {

    gameState.setMoveNr(
      moveSelection.getUniqueEvaluatedMoveNr()
    );
    gameState
      .selectedMove()
      .setMovePoints(moveSelection.movePoints);
  }

  public void pointClicked (int clickedPoint) {

    if (inputReady()) {
      System.out.println("Received inputPoint: "+clickedPoint);
      moveSelection.inputPoint(clickedPoint);
      if (clickedPoint != -1 && autoSelectPartMoves()) {
        moveSelection.autoSelect();
      }
    }
  }

}
