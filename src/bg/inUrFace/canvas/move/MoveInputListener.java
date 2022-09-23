package bg.inUrFace.canvas.move;

import bg.inUrFace.canvas.BoardDim;
import bg.util.Batch;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.Main.*;

public class MoveInputListener extends MouseAdapter {

  public Batch[] getClickPoints() {

    BoardDim d = win.canvas.getDimensions();
    Batch[] regularPoints = mouse.getRegularClickPoints();
    Batch[] points = new Batch[26];
    Color pointsColor = new Color(56, 75, 174, 150);

    if (sepuku.getHumanMove().getPlayerID() == 1) {
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
    if (sepuku.getHumanMove().getPlayerID() == 1) {
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

  private boolean rightButtonClicked (MouseEvent mouseEvent) {

    return mouseEvent.getButton() == MouseEvent.BUTTON3;
  }

  @Override
  public void mouseClicked (MouseEvent mouseEvent) {

    System.out.println();
    System.out.println("Mouse clicked");
    if (humanIsMoving()) {
      inputPoint(mouseEvent);
      System.out.println("Accepted inputPoint");
    }
  }

  private void inputPoint (MouseEvent mouseEvent) {

    if (clickedPoint() != -1 || rightButtonClicked(mouseEvent)) {
      sepuku
        .getHumanMove()
        .pointClicked(
          rightButtonClicked(mouseEvent)
            ? -1
            : clickedPoint()
        );
    }
  }

  private boolean humanIsMoving() {

    return
      sepuku != null
      &&
      sepuku
        .getHumanMove()
        .humanInputActive();
  }

}
