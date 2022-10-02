package inUrFace.canvas.painters.move;

import inUrFace.canvas.painters.Paintable;

import java.awt.*;

import static sepuku.App.playSepuku;
import static sepuku.App.getCanvas;

public class ChequerPainter implements Paintable {

  private int playerID () {

    return
      playSepuku
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
      playSepuku != null
      && playSepuku.getHumanMove().humanInputActive()
      && playSepuku.getMatchPlay().playerIsHuman()
      && playSepuku.getHumanMove().endingPointIsNext()
      && mousePosition() != null;
  }

  public void paint (Graphics graphics) {

    if (mustPaint()) {
      drawChequer(graphics, mousePosition());
    }
  }

}
