package inUrFace.canvas.painters.move;

import inUrFace.canvas.painters.Paintable;

import java.awt.*;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.getCanvas;

public class ChequerPainter implements Paintable {

  private int playerID () {

    return
      sepukuPlay
        .getHumanMove()
        .getPlayerID();
  }

  private int chequerSize () {

    return
      getCanvas()
        .getDimensions()
        .chequerSize;
  }

  private int position (double mousePosition) {

    return (int) mousePosition - (chequerSize() / 2);
  }

  private Color playerColor () {

    return
      playerID() == 0
        ? Color.yellow
        : Color.red;
  }

  private void drawChequer (Graphics graphics, Point mousePoint) {

    graphics.setColor(playerColor());
    graphics.fillOval(
      position(mousePoint.getX()),
      position(mousePoint.getY()),
      chequerSize(),
      chequerSize()
    );
  }

  private Point mousePosition () {

    return
      getCanvas()
        .getMousePosition();
  }

  private boolean mustPaint () {

    return
      sepukuPlay != null
      && sepukuPlay.getHumanMove().humanInputActive()
      && sepukuPlay.getMatchPlay().playerIsHuman()
      && sepukuPlay.getHumanMove().endingPointIsNext()
      && mousePosition() != null;
  }

  public void paint (Graphics graphics) {

    if (mustPaint()) {
      drawChequer(graphics, mousePosition());
    }
  }

}