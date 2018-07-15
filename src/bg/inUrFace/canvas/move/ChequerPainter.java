package bg.inUrFace.canvas.move;

import bg.engine.api.gameState.navigation.humanMove.HumanMoveApi;
import bg.inUrFace.canvas.Paintable;

import java.awt.*;

import static bg.Main.engineApi;
import static bg.Main.getCanvas;

public class ChequerPainter implements Paintable {

  private int playerID () {

    return
      humanMove()
        .getPlayerID();
  }

  private HumanMoveApi humanMove () {

    return engineApi.getHumanMove();
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

  private void drawChequer (Graphics graphics, Point point) {

    graphics.setColor(playerColor());
    graphics.fillOval(
      position(point.getX()),
      position(point.getY()),
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
      humanMove() != null
      && humanMove().inputReady()
      && humanMove().isEndingPoint()
      && mousePosition() != null;
  }

  public void paint (Graphics graphics) {

    if (mustPaint()) {
      drawChequer(graphics, mousePosition());
    }
  }

}
