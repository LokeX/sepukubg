package bg.api;

import bg.engine.moves.MoveSelect;
import bg.engine.moves.Layout;

import java.awt.event.MouseEvent;
import java.util.List;

import static bg.Main.engineApi;
import static bg.Main.getSettings;
import static bg.util.ThreadUtil.runWhenNotified;

public class HumanMove implements HumanMoveApi {

  private MoveSelect moveSelect;

  public int getPlayerID () {

    return moveSelect.getPlayerID();
  }

  public void pointClicked (MouseEvent mouseEvent, int clickedPoint) {

    if (inputReady()) {
      if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
        moveSelect.deleteLatestInput();
      } else {
        moveSelect.input(clickedPoint);
      }
      if (moveSelect.inputIsLegal()) {
        engineApi.layoutOutput.outputLayouts(
          moveSelect.getOutputLayouts()
        );
      }
    }
  }

  int getSelectedMoveNr () {

    return moveSelect.getUniqueEvaluatedMoveNr();
  }

  private boolean isAutoCompletable (MoveSelect moveSelect) {

    return
      moveSelect.getNrOfMoves() == 1
      && (getSettings().isAutoCompleteMoves()
      || getSettings().isAutoCompletePartMoves());
  }

  void inputMovePoints (MoveSelect moveSelect) {

    if (moveSelect == null) {
      this.moveSelect = null;
    } else if (moveSelect.isIllegal() || isAutoCompletable(moveSelect)) {
      engineApi.layoutOutput.outputLayout(
        moveSelect.isIllegal()
          ? moveSelect.getParentMoveLayout()
          : moveSelect.getBestMove()
      );
      this.moveSelect = null;
    } else {
      this.moveSelect = moveSelect;
      if (getSettings().isAutoCompletePartMoves()) {
        initialSelection();
      }
    }
  }

  void inputSelectedTurn () {

    inputMovePoints(
      engineApi
        .getSelectedTurn()
        .getMoveSelection()
    );
  }

  private void initialSelection () {

    engineApi.layoutOutput.outputLayouts(
      autoSelectLayouts(),
      runWhenNotified(() -> {
        if (moveSelect.endOfInput()) {
          engineApi.matchPlay.endTurn();
        }
      })
    );
  }

  private List<Layout> autoSelectLayouts() {

    moveSelect.initialSelection();
    return
      moveSelect.getOutputLayouts();
  }

  boolean endOfInput () {

    return moveSelect.endOfInput();
  }

  private boolean isSelectedTurn () {

    return
      moveSelect
        .hasIdenticalTurnNrTo(
          engineApi.getSelectedTurnNr()
        );
  }

  public boolean inputReady() {

    return
      moveSelect != null && isSelectedTurn();
  }

  String getMovePointsString () {

    return
      moveSelect
        .getMatchingMoveLayout()
        .getMovePointsString();
  }

}