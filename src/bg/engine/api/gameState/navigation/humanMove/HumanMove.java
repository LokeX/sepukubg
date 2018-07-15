package bg.engine.api.gameState.navigation.humanMove;

import bg.engine.api.gameState.navigation.Navigation;
import bg.engine.match.moves.Layout;

import java.util.List;
import java.util.stream.Stream;

import static bg.Main.getSettings;

public class HumanMove implements HumanMoveApi {

  private MoveSelector moveSelector;
  private Navigation navigation;

  public HumanMove (Navigation navigation) {

    this.navigation = navigation;
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
    if (moveSelector.isIllegal()) {
      moveSelector = null;
    } else if (isAutoSelectable()){
      moveSelector.initialSelection();
    }
  }

  public void endMoveSelection () {

    moveSelector = null;
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
      if (clickedPoint == -1) {
        moveSelector.deleteLatestInput();
      } else {
        moveSelector.input(clickedPoint);
      }
      if (moveSelector.inputIsLegal()) {
        if (moveSelector.isUniqueMove()) {
          selectUniqueMove();
        }
      }
    }
  }

  public List<Layout> getMoveLayouts () {

    return moveSelector.getMoveLayouts();
  }

  public boolean endOfInput () {

    return
      moveSelector == null
      || moveSelector.endOfInput();
  }

  public boolean inputReady() {

    return
      moveSelector != null;
  }

  public String getMovePointsString () {

    return
      moveSelector
        .getMatchingMoveLayout()
        .getMovePointsString();
  }

  public int getPlayerID () {

    return
      moveSelector
        .getPlayerID();
  }

  public boolean isEndingPoint () {

    return
      moveSelector
        .positionIsEndingPoint();
  }

  public Stream<Integer> getEndingPoints () {

    return
      moveSelector
        .validEndingPoints();
  }

}
