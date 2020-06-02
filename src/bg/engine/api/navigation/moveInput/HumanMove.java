package bg.engine.api.navigation.moveInput;

import bg.engine.api.navigation.Navigation;

import static bg.Main.getSettings;

public class HumanMove {

  private MoveSelector moveSelector;
  private Navigation navigation;

  public HumanMove (Navigation navigation) {

    this.navigation = navigation;
  }

  public MoveSelector getMoveSelector () {

    return moveSelector;
  }

  private boolean autoCompleteMove () {

    return
      moveSelector.getNrOfMoves() == 1
      && getSettings().isAutoCompleteMoves();
  }

  private boolean isAutoSelectable () {

    return
      getSettings().isAutoCompletePartMoves()
      || autoCompleteMove();
  }

  public void startMoveSelection () {

    moveSelector = new MoveSelector(navigation.selectedTurn());
    if (moveSelector.noLegalMove()) {
      moveSelector = null;
    } else if (isAutoSelectable()){
      moveSelector.initialSelection();
    }
  }

  public void endMoveSelection () {

    moveSelector = null;
  }

  public boolean inputReady() {

    return
      moveSelector != null;
  }

  private void selectUniqueMove () {

    navigation.setMoveNr(
      moveSelector.getUniqueEvaluatedMoveNr()
    );
    navigation
      .selectedMove()
      .setMovePoints(moveSelector.movePoints);
  }

  public void pointClicked (int clickedPoint) {

    if (inputReady()) {
      moveSelector.input(clickedPoint);
      if (moveSelector.inputIsLegal()) {
        if (moveSelector.isUniqueMove()) {
          selectUniqueMove();
        }
      }
    }
  }

}
