package inUrFace.canvas.listeners;

import inUrFace.canvas.BoardDim;
import util.Batch;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static sepuku.WinApp.*;

public class MoveInputListener extends MouseAdapter {

  public Batch[] getClickPoints() {

    BoardDim d = win.canvas.getDimensions();
    Batch[] regularPoints = win.mouse.getRegularClickPoints();
    Batch[] points = new Batch[26];
    Color pointsColor = new Color(56, 75, 174, 150);

    if (sepukuPlay.humanMove().getPlayerID() == 1) {
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
    if (sepukuPlay.humanMove().getPlayerID() == 1) {
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

    if (humanIsMoving()) {
      inputPoint(mouseEvent);
    }
  }

  private void inputPoint (MouseEvent mouseEvent) {

    if (clickedPoint() != -1 || rightButtonClicked(mouseEvent)) {
      sepukuPlay
        .humanMove()
        .pointClicked(
          rightButtonClicked(mouseEvent)
            ? -1
            : clickedPoint()
        );
    }
  }

  private boolean humanIsMoving() {

    return
      sepukuPlay != null
      &&
      sepukuPlay
        .humanMove()
        .inputActive();
  }

}