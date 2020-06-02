/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bg.inUrFace.canvas;

import static bg.Main.getDisplayedLayout;
import static bg.Main.win;

import bg.engine.coreLogic.moves.Layout;

import java.awt.*;

public class LayoutPainter implements Paintable {

  @Override
  public void paint (Graphics g) {

    BoardDim d = win.canvas.getDimensions();
    int bottomRightOffsetX = d.rightPlayAreaOffsetX+d.rightPlayAreaWidth;
    int chequerBottomOffsetY = d.boardOffsetY+d.boardHeight-d.frameHeight-d.chequerSize;
    Color colorRed = new Color (178,18,18);
    Color colorYellow = new Color (220,213,34);
    Layout layout = new Layout(getDisplayedLayout());
    int[] point = layout.point;
    Integer whitePip;
    Integer blackPip;
    int x,y;

    whitePip = layout.calcPip().getPip();
    blackPip = layout.getOpponentPip();
    g.setColor(Color.RED);
    g.drawString(
      blackPip.toString(),
      d.topRightBearOffOffsetX,
      d.topRightBearOffOffsetY - (int) (d.chequerSize * 0.72)
    );
    g.setColor(Color.YELLOW);
    g.drawString(
      whitePip.toString(),
      d.topRightBearOffOffsetX,
      d.topRightBearOffOffsetY + d.boardHeight + (int) (d.chequerSize * 1.10) - (d.frameHeight * 2)
    );
    for (int a = 1; a < 25; a++) {
      if (point[a] > 0) {
        for (int b = 0; b < (point[a] > 5 ? 5 : point[a]); b++) {
          g.setColor(colorYellow);
          String str = (point[a] < 10 ? " " : "") + Integer.toString(point[a]);
          if (a < 13) {
            x = bottomRightOffsetX - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
            y = chequerBottomOffsetY - (b * d.chequerSize);
            g.fillOval(x, y, d.chequerSize, d.chequerSize);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d.chequerSize, d.chequerSize);
            if (b == 4 && point[a] > 5) {
              x = (int)(bottomRightOffsetX*1.011) - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
              y = (int)((d.frameOffsetY+d.boardInnerHeight - (b * d.chequerSize))*0.975);
              g.drawString(str, x, y);
            }
          } else {

            x = (int)(d.leftPlayAreaOffsetX*1.065) +
              (a > 18 ? (int)(d.barWidth*1.1) : 0) +
              ((a - 13) * d.chequerTotalSpace);

            y = d.frameOffsetY + (b * d.chequerSize);
            g.fillOval(x, y, d.chequerSize, d.chequerSize);
            g.setColor(Color.BLACK);
            x = (int)(d.leftPlayAreaOffsetX*1.065) +
              (a > 18 ? (int)(d.barWidth*1.1) : 0) +
              ((a - 13) * d.chequerTotalSpace);
            y = d.frameOffsetY + (b * d.chequerSize);
            g.drawOval(x, y, d.chequerSize, d.chequerSize);
            if (b == 4 && point[a] > 5) {

              x = (int)(d.leftPlayAreaOffsetX*1.142) +
                (a > 18 ? (int)(d.barWidth*1.1) : 0) +
                ((a - 13) * d.chequerTotalSpace);

              y = (int)((d.frameOffsetY + ((b+1) * d.chequerSize))*0.965);
              g.drawString(str, x, y);
            }
          }
        }
      }
      if (point[a + 26] > 0) {
        for (int b = 0; b < (point[a + 26] > 5 ? 5 : point[a + 26]); b++) {
          g.setColor(colorRed);
          String str = (point[a + 26] < 10 ? " " : "") + Integer.toString(point[a + 26]);
          if (a < 13) {
            x = bottomRightOffsetX - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
            y = chequerBottomOffsetY - (b * d.chequerSize);
            g.fillOval(x, y, d.chequerSize, d.chequerSize);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d.chequerSize, d.chequerSize);
            if (b == 4 && point[a + 26] > 5) {
              x = bottomRightOffsetX+ 7 - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
              y = (int)((d.frameOffsetY+d.boardInnerHeight - (b * d.chequerSize))*0.975);
              g.drawString(str, x, y);
            }
          } else {
            x = (int)(d.leftPlayAreaOffsetX*1.065) +
              (a > 18 ? (int)(d.barWidth*1.1) : 0) +
              ((a - 13) * d.chequerTotalSpace);
            y = d.frameOffsetY + (b * d.chequerSize);
            g.fillOval(x, y, d.chequerSize, d.chequerSize);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d.chequerSize, d.chequerSize);
            if (b == 4 && point[a + 26] > 5) {
              x = (int)(d.leftPlayAreaOffsetX*1.142) +
                (a > 18 ? (int)(d.barWidth*1.1) : 0) +
                ((a - 13) * d.chequerTotalSpace);
              y = (int)((d.frameOffsetY + ((b+1) * d.chequerSize))*0.965);
              g.drawString(str, x, y);
            }
          }
        }
      }
    }
    g.setColor(colorYellow);
    for (int a = 0; a < (point[25] > 6 ? 6 : point[25]); a++) {
      g.fillOval(
        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth+5,
        d.frameOffsetY+(int)(d.boardInnerHeight/2.5) - (a * d.chequerSize),
        d.chequerSize,
        d.chequerSize
      );
      if (a == 0 && point[25] > 6) {
        g.setColor(Color.black);
        g.drawString(
          Integer.toString(point[25]),
          (int)((d.leftPlayAreaOffsetX+d.leftPlayAreaWidth + (d.barWidth/2))*0.989),
          (int)((d.frameOffsetY+(d.boardInnerHeight/2))*0.945)
        );
        g.setColor(Color.yellow);
      }
    }
    for (int a = 0; a < point[0]; a++) {
      g.fill3DRect(
        d.bottomRightBearOffOffsetX+3,
        d.bottomRightBearOffOffsetY + (a * (int)(11.3*d.factor)),
        (int)(30.0*d.factor),
        (int)(9*d.factor), true
      );
    }
    g.setColor(colorRed);
    for (int a = 0; a < (point[26] > 6 ? 6 : point[26]); a++) {
      g.fillOval(
        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth+5,
        d.frameOffsetY+(d.boardInnerHeight/2) + (a * d.chequerSize),
        d.chequerSize,
        d.chequerSize
      );
      if (a == 0 && point[26] > 6) {
        g.setColor(Color.black);
        g.drawString(
          Integer.toString(point[26]),
          (int)((d.leftPlayAreaOffsetX+d.leftPlayAreaWidth + (d.barWidth/2))*0.989),
          (int)(((d.frameOffsetY+(d.boardInnerHeight/2)))*1.09)
        );
        g.setColor(Color.red);
      }
    }
    for (int a = 0; a < point[51]; a++) {
      g.fill3DRect(
        d.topRightBearOffOffsetX +3,
        d.topRightBearOffOffsetY + (a * (int)(11.3*d.factor)),
        (int)(30.0*d.factor),
        (int)(9.0*d.factor),
        true
      );
    }
  }

}
