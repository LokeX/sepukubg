package bg.inUrFace.mouse;

import bg.api.Moveable;
import bg.inUrFace.canvas.BoardDim;
import bg.util.Batch;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.Main.getActionButton;
import static bg.Main.matchApi;
import static bg.Main.win;

public class MoveInputApi extends MouseAdapter {

//  private Moveable moveable;
  private MoveInput moveInput;

  private Batch[] getClickPoints() {

    BoardDim d = win.canvas.getDimensions();
    Batch[] regularPoints = MouseApi.getRegularClickPoints();
    Batch[] points = new Batch[26];
    Color pointsColor = new Color(56, 75, 174, 150);

    if (matchApi.getMoveInputNew().getPlayerID() == 1) {
      points[0] = new Batch(
        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth,
        d.frameOffsetY+d.boardInnerHeight/2,
        d.barWidth,
        d.boardInnerHeight/2
      );
    } else {
      points[0] = new Batch(
        d.bottomRightBearOffOffsetX,
        d.bottomRightBearOffOffsetY,
        d.chequerSize,
        d.bearOffHeight
      );
    }
    points[0].setComponent(win.canvas);
    points[0].setBackgroundColor(pointsColor);
    for (int a = 0; a < regularPoints.length; a++) {
      points[a+1] = regularPoints[a];
      points[a+1].setBackgroundColor(pointsColor);
    }
    if (matchApi.getMoveInputNew().getPlayerID() == 1) {
      points[25] = new Batch(
        d.topRightBearOffOffsetX,
        d.topRightBearOffOffsetY,
        d.chequerSize,
        d.bearOffHeight
      );
    } else {
      points[25] = new Batch(
        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth,
        d.frameOffsetY,
        d.barWidth,
        d.boardInnerHeight/2
      );
    }
    points[25].setBackgroundColor(pointsColor);
    points[25].setComponent(win.canvas);
    return points;
  }

  private int clickedPoint () {

    Batch[] clickPoints = getClickPoints();

    for (int a = 0; a < clickPoints.length; a++) {
      if (clickPoints[a].mouseOnBatch()) {
        return a;
      }
    }
    return -1;
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (isAcceptingInput()) {
      if (e.getButton() == MouseEvent.BUTTON3) {
        getActionButton().setHideActionButton(true);
        moveInput.undoPointSelection();
      } else {
        moveInput.pointClicked();
      }
      if (matchApi.getMoveInputNew().isAcceptingInput()) {
        matchApi.getMoveInputNew().pointClicked(e, clickedPoint());
      }
    }
  }

  public MoveInput getMoveInput () {

    return moveInput;
  }

  void setMoveInput (MoveInput moveInput) {

    this.moveInput = moveInput;
  }

  public void setAcceptMoveInput (boolean acceptMoveInput) {

    //Old code begin:
    if (acceptMoveInput) {
      moveInput = new MoveInput();
    } else {
      moveInput = null;
    }
    //:Old code end

  }

  public boolean isAcceptingInput() {

    return moveInput != null && matchApi != null && matchApi.getMoveInputNew() != null;
  }

}
