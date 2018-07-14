package bg.engine.api.gameState.humanMove;

import bg.engine.api.gameState.navigation.Navigation;
import bg.engine.match.moves.Layout;

import java.util.List;
import java.util.stream.Stream;

import static bg.Main.getSettings;

public class HumanMove implements HumanMoveApi {

  private MoveSelector moveSelector;
  private Navigation navigation;

  private List<Layout> autoMoveLayout () {

    return
      moveSelector.isIllegal()
        ? List.of(moveSelector.getParentMoveLayout())
        : List.of(moveSelector.getBestMove());
  }

  private boolean settingIsAutoCompletion () {

    return
      getSettings().isAutoCompleteMoves()
      || getSettings().isAutoCompletePartMoves();
  }

  private boolean isAutoSelectable(MoveSelector moveSelector) {

    return
      moveSelector.isIllegal()
        || moveSelector.getNrOfMoves() == 1
        && settingIsAutoCompletion();
  }

  public void setMoveSelection(Navigation navigation) {

    if (navigation != null) {
      this.navigation = navigation;
      moveSelector = new MoveSelector(navigation.selectedTurn());
      if (isAutoSelectable(moveSelector)) {
        outputLayouts(autoMoveLayout());
        moveSelector = null;
      } else if (getSettings().isAutoCompletePartMoves()){
        outputLayouts(initialSelection());
      }
    } else {
      moveSelector = null;
    }
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
        outputLayouts(
          moveSelector.getMoveLayouts()
        );
      }
    }
  }

  public List<Layout> getMoveLayouts () {

    return moveSelector.getMoveLayouts();
  }

  private void outputLayouts (List<Layout> layouts) {

    navigation.setOutputLayouts(layouts);
  }

  private List<Layout> initialSelection () {

    moveSelector.initialSelection();
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

    return moveSelector.getPlayerID();
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
