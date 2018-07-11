package bg.engine.api.gameState.humanMove;

import bg.engine.api.gameState.Navigation;
import bg.engine.match.moves.Layout;

import java.util.List;
import java.util.stream.Stream;

import static bg.Main.getSettings;

public class HumanMove implements HumanMoveApi {

  private MoveSelect moveSelect;
  private Navigation navigation;

  private List<Layout> autoMoveLayout () {

    return
      moveSelect.isIllegal()
        ? List.of(moveSelect.getParentMoveLayout())
        : List.of(moveSelect.getBestMove());
  }

  private boolean settingIsAutoCompletion () {

    return
      getSettings().isAutoCompleteMoves()
      || getSettings().isAutoCompletePartMoves();
  }

  private boolean isAutoCompletable (MoveSelect moveSelect) {

    return
      moveSelect.isIllegal()
        || moveSelect.getNrOfMoves() == 1
        && settingIsAutoCompletion();
  }

  public void setMoveSelection(Navigation navigation) {

    if (navigation != null) {
      this.navigation = navigation;
      moveSelect = new MoveSelect(navigation.selectedTurn());
      if (isAutoCompletable(moveSelect)) {
        outputLayouts(autoMoveLayout());
        moveSelect = null;
      } else if (getSettings().isAutoCompletePartMoves()){
        outputLayouts(initialSelection());
      }
    } else {
      moveSelect = null;
    }
  }

  private void selectUniqueMove () {

    navigation.setMoveNr(
      moveSelect.getUniqueEvaluatedMoveNr()
    );
    navigation
      .selectedMove()
      .setMovePoints(moveSelect.movePoints);
  }

  public void pointClicked (int clickedPoint) {

    if (inputReady()) {
      if (clickedPoint == -1) {
        moveSelect.deleteLatestInput();
      } else {
        moveSelect.input(clickedPoint);
      }
      if (moveSelect.inputIsLegal()) {
        if (moveSelect.isUniqueMove()) {
          selectUniqueMove();
        }
        outputLayouts(
          moveSelect.getMoveLayouts()
        );
      }
    }
  }

  private void outputLayouts (List<Layout> layouts) {

    navigation.setOutputLayouts(layouts);
  }

  private List<Layout> initialSelection () {

    moveSelect.initialSelection();
    return moveSelect.getMoveLayouts();
  }

  public boolean endOfInput () {

    return
      moveSelect == null
        || moveSelect.endOfInput();
  }

  public boolean inputReady() {

    return
      moveSelect != null;
  }

  public String getMovePointsString () {

    return
      moveSelect
        .getMatchingMoveLayout()
        .getMovePointsString();
  }

  public int getPlayerID () {

    return moveSelect.getPlayerID();
  }

  public boolean isEndingPoint () {

    return
      moveSelect
        .positionIsEndingPoint();
  }

  public Stream<Integer> getEndingPoints () {

    return
      moveSelect
        .validEndingPoints();
  }

}
