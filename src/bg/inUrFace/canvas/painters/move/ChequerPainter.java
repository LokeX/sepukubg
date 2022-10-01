package bg.inUrFace.canvas.painters.move;

import bg.inUrFace.canvas.painters.Paintable;

import java.awt.*;

import static bg.Main.sepuku;
import static bg.Main.getCanvas;

public class ChequerPainter implements Paintable {

  private int playerID () {

    return
      sepuku
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
      sepuku != null
      && sepuku.getHumanMove().humanInputActive()
      && sepuku.getMatchPlay().playerIsHuman()
      && sepuku.getHumanMove().endingPointIsNext()
      && mousePosition() != null;
  }

  public void paint (Graphics graphics) {

    if (mustPaint()) {
      drawChequer(graphics, mousePosition());
    }
  }

}
