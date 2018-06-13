package bg.api;

import bg.engine.moves.InputPoints;
import bg.engine.moves.Layout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static bg.Main.matchApi;
import static bg.util.ListUtil.listInRange;

public class MoveInputNew implements MoveInputApi {

  private InputPoints inputPoints;

  public int getPlayerID () {

    return inputPoints.getPlayerID();
  }

  public void pointClicked (MouseEvent mouseEvent, int clickedPoint) {

    int storedPosition = inputPoints.getPosition();

    if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
      inputPoints.deleteLatestInput();
    } else if (!inputPoints.endOfInput()){
      inputPoints.input(clickedPoint);
    }
    matchApi.moveOutput.outputLayouts(outputLayouts(storedPosition));
  }

  private List<Layout> outputLayouts (int storedPosition) {

    if (storedPosition > inputPoints.getPosition()) {
      return
        filteredLayouts(
          inputPoints.getPosition(),
          storedPosition
        );
    } else if (storedPosition < inputPoints.getPosition()) {
      return
        filteredLayouts(
          storedPosition,
          inputPoints.getPosition()
        );
    }
    return new ArrayList<>();
  }

  private List<Layout> filteredLayouts (int startPosition, int endPosition) {

    return listInRange(
      inputPoints.getMoveLayoutsAsLayouts(),
      startPosition,
      endPosition
    );
  }

  int getSelectedMoveNr () {

    return inputPoints.getUniqueEvaluatedMoveNr();
  }

  void setInputPoints (InputPoints inputPoints) {

    this.inputPoints = inputPoints;
  }

  List<Layout> initialAutoMove () {

    inputPoints.initialAutoMove();
    return
      filteredLayouts(0, inputPoints.getPosition());
  }

  boolean inputPointsReady () {

    return inputPoints != null;
  }

  String getMovePointsString () {

    return inputPoints.getMovePointsString();
  }

}