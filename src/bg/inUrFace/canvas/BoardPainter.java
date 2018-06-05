/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bg.inUrFace.canvas;

import static bg.Main.matchApi;
import static bg.Main.win;

import java.awt.*;

public class BoardPainter implements Paintable {

  @Override
  public void paint(Graphics g) {

    int[] startNrs = new int[]{13, 12};
    BoardDim d = win.canvas.getDimensions();
    Graphics2D g2D = (Graphics2D)g;

    g2D.setStroke(new BasicStroke(4));

    g.setColor(new Color(94, 58, 1));
    g.fillRect(
      0,
      0,
      win.canvas.getWidth(),
      win.canvas.getHeight()
    );
    g.setColor(Color.black);
    g.fill3DRect(
      d.boardOffsetX,
      d.boardOffsetY,
      d.boardWidth,
      d.boardHeight,
      true
    );
    g.setColor(new Color(7, 100, 9));
    g.fill3DRect(
      d.topLeftBearOffOffsetX,
      d.topLeftBearOffOffsetY,
      d.bearOffWidth,
      d.bearOffHeight,
      false
    );
    g.fill3DRect(
      d.bottomLeftBearOffOffsetX,
      d.bottomLeftBearOffOffsetY,
      d.bearOffWidth,
      d.bearOffHeight,
      false
    );
    g.fill3DRect(
      d.leftPlayAreaOffsetX,
      d.leftPlayAreaOffsetY,
      d.leftPlayAreaWidth,
      d.leftPlayAreaHeight,
      false
    );
    g.fill3DRect(
      d.rightPlayAreaOffsetX,
      d.rightPlayAreaOffsetY,
      d.rightPlayAreaWidth,
      d.rightPlayAreaHeight,
      false
    );
    g.fill3DRect(
      d.topRightBearOffOffsetX,
      d.topRightBearOffOffsetY,
      d.bearOffWidth,
      d.bearOffHeight,
      false
    );
    g.fill3DRect(d.
      bottomRightBearOffOffsetX,
      d.bottomRightBearOffOffsetY,
      d.bearOffWidth,
      d.bearOffHeight,
      false
    );

    g.fill3DRect(
      d.bottomLeftBearOffOffsetX,
      d.diePocketOffsetY,
      d.bearOffWidth,
      d.diePocketHeight,
      false
    );
    g.fill3DRect(
      d.bottomRightBearOffOffsetX,
      d.diePocketOffsetY,
      d.bearOffWidth,
      d.diePocketHeight,
      false
    );
    g.setFont(new Font("Ariel", Font.BOLD, (int) (20.0 * d.factor)));
    g.setColor(Color.WHITE);

    if (matchApi != null) {

      int t;
      String s;
      int xOffset;
      int yOffset;

      for (int x = 0; x < 12; x++) {
        for (int y = 0; y < 2; y++) {

          t = startNrs[y] + (x - (x * (y == 0 ? 0 : 2)));
          s = (t < 10 ? " " : "") + Integer.toString(t);
          xOffset = (int) (d.leftPlayAreaOffsetX * 1.15) +
            ((d.chequerTotalSpace * x) + (x > 5 ? (int) (48.0 * d.factor) : 0));
          yOffset = (int) ((float) d.boardOffsetY / 1.25) +
            (y * ((int) ((d.boardOffsetY+d.boardHeight) * 0.97)));

          g.drawString(s, xOffset, yOffset);
        }
      }
    }

    Color[] colors;

    for (int b = 0; b < 6; b++) {
      for (int a = 0; a < (int) (160 * d.factor); a++) {
        colors = new Color[]{Color.white, Color.black};
        g.setColor(colors[b % 2]);
        g2D.drawLine(
          d.leftTrianglesOffsetX + (d.chequerTotalSpace * b) + (a / 9),
          d.frameOffsetY + a,
          d.leftTrianglesOffsetX + (int) (d.chequerTotalSpace * (b + 0.81)) - (a / 9),
          d.frameOffsetY + a
        );
        g2D.drawLine(
          d.rightTrianglesOffsetX + (d.chequerTotalSpace * b) + (a / 9),
          d.frameOffsetY + a,
          d.rightTrianglesOffsetX + (int) (d.chequerTotalSpace * (b + 0.81)) - (a / 9),
          d.frameOffsetY + a
        );
        colors = new Color[]{Color.black, Color.white};
        g.setColor(colors[b % 2]);
        g2D.drawLine(
          d.leftTrianglesOffsetX + (d.chequerTotalSpace * b) + (a / 9),
          d.frameOffsetY + d.boardInnerHeight - a,
          d.leftTrianglesOffsetX + (int) (d.chequerTotalSpace * (b + 0.81)) - (a / 9),
          d.frameOffsetY + d.boardInnerHeight - a
        );
        g2D.drawLine(
          d.rightTrianglesOffsetX + (d.chequerTotalSpace * b) + (a / 9),
          d.frameOffsetY + d.boardInnerHeight - a,
          d.rightTrianglesOffsetX + (int) (d.chequerTotalSpace * (b + 0.81)) - (a / 9),
          d.frameOffsetY + d.boardInnerHeight - a
        );
      }
    }
    g2D.setStroke(new BasicStroke(1));
  }

}
