package bg.inUrFace.canvas.move;

import bg.inUrFace.canvas.BoardDim;
import bg.inUrFace.canvas.Paintable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import static bg.Main.*;

public class MoveEffectsPainter implements Paintable {

  @Override
  public void paint(Graphics g) {

    if (mouse != null && getMoveInput() != null) {
      paintEndingPoints(g);
      paintMovingPiece(g);
    }
  }

  private void paintEndingPoints (Graphics g) {

    Integer[] endingPoints = getMoveInput().getEndingPoints();

    if (endingPoints != null) {
      for (Integer endingPoint : endingPoints) {
        if (endingPoint >= 0) {
          getMoveInput().getPoints()[endingPoint].drawBatch(g);
        }
      }
    }
  }

  private void paintMovingPiece (Graphics g) {

    if ((getMoveInput().getInputPoint() + 2) % 2 != 0) {

      Point p = getCanvas().getMousePosition();
      BoardDim d = getCanvas().getDimensions();

      if (p != null) {

        int mx = (int) p.getX();
        int my = (int) p.getY();

        if (getMoveInput().getPlayerID() == 0) {
          g.setColor(Color.yellow);
        } else {
          g.setColor(Color.red);
        }
        g.fillOval(
          mx - (d.chequerSize/2),
          my - (d.chequerSize/2),
          d.chequerSize,
          d.chequerSize
        );
      }
    }
  }

}
