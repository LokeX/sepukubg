package bg.api;

import bg.engine.moves.MovePointsInput;
import java.awt.event.MouseEvent;

import static bg.Main.*;

public class MoveInputNew implements Moveable {

  private MovePointsInput pointInput;
  private boolean acceptMoveInput = false;

  public boolean isAcceptingInput () {

    return acceptMoveInput;
  }

  public int getPlayerID () {

    return pointInput.getPlayerID();
  }

  public void pointClicked (MouseEvent e, int clickedPoint) {

    if (isAcceptingInput()) {
      if (e.getButton() == MouseEvent.BUTTON3) {
        undoPointInput();
      } else {
        inputPoint(clickedPoint);
      }
    }
  }

  private void undoPointInput () {

    getActionButton().setHideActionButton(true);
    pointInput.deleteLatestInput();
  }

  private void inputPoint (int point) {

    pointInput.input(point);
  }

  void setPointInput (MovePointsInput pointInput) {

    this.pointInput = pointInput;
  }

  void initialAutoMove () {

    pointInput.initialAutoMove();
  }

  void setAcceptInput (boolean accept) {

    acceptMoveInput = accept;
  }

}