package bg.inUrFace.canvas;

import static bg.Main.getDisplayedLayout;
import static bg.Main.win;

import bg.engine.core.moves.Layout;
import java.awt.*;

public class LayoutPainter implements Paintable {
  
  private Color yellowColor = new Color (220,213,34);
  private Color redColor = new Color (178,18,18);
  private Color[] colors = new Color[] {
    yellowColor,
    redColor,
  };

  @Override
  public void paint (Graphics g) {

    BoardDim d = win.canvas.getDimensions();
    Layout layout = getDisplayedLayout();
    int[][] playerPoints = new int[][] {
      layout.getWhitePoints(),
      layout.getBlackPoints(),
    };
    int whitePip = layout.getWhitePip();
    int blackPip = layout.getBlackPip();
    int bottomRightOffsetX =
      d.rightPlayAreaOffsetX+d.rightPlayAreaWidth;
    int chequerBottomOffsetY =
      d.boardOffsetY+d.boardHeight-d.frameHeight-d.chequerSize;
    int x,y;

    g.setColor(Color.RED);
    g.drawString(
      Integer.toString(blackPip),
      d.topRightBearOffOffsetX,
      d.topRightBearOffOffsetY - (int) (d.chequerSize * 0.72)
    );
    g.setColor(Color.YELLOW);
    g.drawString(
      Integer.toString(whitePip),
      d.topRightBearOffOffsetX,
      d.topRightBearOffOffsetY + d.boardHeight + (int) (d.chequerSize * 1.10) - (d.frameHeight * 2)
    );
    for (int player = 0; player < 2; player++) {
      for (int pointNr = 1; pointNr < 25; pointNr++) {
        if (playerPoints[player][pointNr] > 0) {
          for (int checker = 0; checker < (Math.min(playerPoints[player][pointNr], 5)); checker++) {
            g.setColor(colors[player]);
            String str =
              (playerPoints[player][pointNr] < 10 ? " " : "")
              + playerPoints[player][pointNr];
            if (pointNr < 13) {
              x = bottomRightOffsetX - (pointNr > 6 ? (int) (d.barWidth * 1.1) : 0) - (pointNr * d.chequerTotalSpace);
              y = chequerBottomOffsetY - (checker * d.chequerSize);
              g.fillOval(x, y, d.chequerSize, d.chequerSize);
              g.setColor(Color.BLACK);
              g.drawOval(x, y, d.chequerSize, d.chequerSize);
              if (checker == 4 && playerPoints[player][pointNr] > 5) {
                x = (int) (bottomRightOffsetX * 1.011) - (pointNr > 6 ? (int) (d.barWidth * 1.1) : 0) - (pointNr * d.chequerTotalSpace);
                y = (int) ((d.frameOffsetY + d.boardInnerHeight - (checker * d.chequerSize)) * 0.975);
                g.drawString(str, x, y);
              }
            } else {
              x = (int) (d.leftPlayAreaOffsetX * 1.065) +
                (pointNr > 18 ? (int) (d.barWidth * 1.1) : 0) +
                ((pointNr - 13) * d.chequerTotalSpace);
    
              y = d.frameOffsetY + (checker * d.chequerSize);
              g.fillOval(x, y, d.chequerSize, d.chequerSize);
              g.setColor(Color.BLACK);
              x = (int) (d.leftPlayAreaOffsetX * 1.065) +
                (pointNr > 18 ? (int) (d.barWidth * 1.1) : 0) +
                ((pointNr - 13) * d.chequerTotalSpace);
              y = d.frameOffsetY + (checker * d.chequerSize);
              g.drawOval(x, y, d.chequerSize, d.chequerSize);
              if (checker == 4 && playerPoints[player][pointNr] > 5) {
    
                x = (int) (d.leftPlayAreaOffsetX * 1.142) +
                  (pointNr > 18 ? (int) (d.barWidth * 1.1) : 0) +
                  ((pointNr - 13) * d.chequerTotalSpace);
    
                y = (int) ((d.frameOffsetY + ((checker + 1) * d.chequerSize)) * 0.965);
                g.drawString(str, x, y);
              }
            }
          }
        }
      }
    }
    for (int player = 0; player < 2; player++) {
      g.setColor(colors[player]);
      for (int a = 0; a < (Math.min(playerPoints[player][25], 6)); a++) {
        g.fillOval(
          d.leftPlayAreaOffsetX+d.leftPlayAreaWidth+5,
          d.frameOffsetY+(int)(d.boardInnerHeight/2.5)-(a*d.chequerSize),
          d.chequerSize,
          d.chequerSize
        );
        if (a == 0 && playerPoints[player][25] > 6) {
          g.setColor(Color.black);
          g.drawString(
            Integer.toString(playerPoints[player][25]),
            (int)((d.leftPlayAreaOffsetX+d.leftPlayAreaWidth+(d.barWidth/2))*0.989),
            (int)((d.frameOffsetY+(d.boardInnerHeight/2))*0.945)
          );
          g.setColor(Color.yellow);
        }
      }
      for (int a = 0; a < playerPoints[player][0]; a++) {
        g.fill3DRect(
          d.bottomRightBearOffOffsetX + 3,
          d.bottomRightBearOffOffsetY + (a * (int) (11.3 * d.factor)),
          (int) (30.0 * d.factor),
          (int) (9 * d.factor), true
        );
      }
    }
  }

}
//  public void paint (Graphics g) {
//
//    BoardDim d = win.canvas.getDimensions();
//    int bottomRightOffsetX = d.rightPlayAreaOffsetX+d.rightPlayAreaWidth;
//    int chequerBottomOffsetY = d.boardOffsetY+d.boardHeight-d.frameHeight-d.chequerSize;
//    Layout layout = getDisplayedLayout();
//    int[] point = layout.point;
//    int whitePip = layout.getWhitePip();
//    int blackPip = layout.getBlackPip();
//    int x,y;
//
//    g.setColor(Color.RED);
//    g.drawString(
//      Integer.toString(blackPip),
//      d.topRightBearOffOffsetX,
//      d.topRightBearOffOffsetY - (int) (d.chequerSize * 0.72)
//    );
//    g.setColor(Color.YELLOW);
//    g.drawString(
//      Integer.toString(whitePip),
//      d.topRightBearOffOffsetX,
//      d.topRightBearOffOffsetY + d.boardHeight + (int) (d.chequerSize * 1.10) - (d.frameHeight * 2)
//    );
//    for (int a = 1; a < 25; a++) {
//      if (point[a] > 0) {
//        for (int b = 0; b < (point[a] > 5 ? 5 : point[a]); b++) {
//          g.setColor(colorYellow);
//          String str = (point[a] < 10 ? " " : "") + Integer.toString(point[a]);
//          if (a < 13) {
//            x = bottomRightOffsetX - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
//            y = chequerBottomOffsetY - (b * d.chequerSize);
//            g.fillOval(x, y, d.chequerSize, d.chequerSize);
//            g.setColor(Color.BLACK);
//            g.drawOval(x, y, d.chequerSize, d.chequerSize);
//            if (b == 4 && point[a] > 5) {
//              x = (int)(bottomRightOffsetX*1.011) - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
//              y = (int)((d.frameOffsetY+d.boardInnerHeight - (b * d.chequerSize))*0.975);
//              g.drawString(str, x, y);
//            }
//          } else {
//
//            x = (int)(d.leftPlayAreaOffsetX*1.065) +
//              (a > 18 ? (int)(d.barWidth*1.1) : 0) +
//              ((a - 13) * d.chequerTotalSpace);
//
//            y = d.frameOffsetY + (b * d.chequerSize);
//            g.fillOval(x, y, d.chequerSize, d.chequerSize);
//            g.setColor(Color.BLACK);
//            x = (int)(d.leftPlayAreaOffsetX*1.065) +
//              (a > 18 ? (int)(d.barWidth*1.1) : 0) +
//              ((a - 13) * d.chequerTotalSpace);
//            y = d.frameOffsetY + (b * d.chequerSize);
//            g.drawOval(x, y, d.chequerSize, d.chequerSize);
//            if (b == 4 && point[a] > 5) {
//
//              x = (int)(d.leftPlayAreaOffsetX*1.142) +
//                (a > 18 ? (int)(d.barWidth*1.1) : 0) +
//                ((a - 13) * d.chequerTotalSpace);
//
//              y = (int)((d.frameOffsetY + ((b+1) * d.chequerSize))*0.965);
//              g.drawString(str, x, y);
//            }
//          }
//        }
//      }
//      if (point[a + 26] > 0) {
//        for (int b = 0; b < (point[a + 26] > 5 ? 5 : point[a + 26]); b++) {
//          g.setColor(colorRed);
//          String str = (point[a + 26] < 10 ? " " : "") + Integer.toString(point[a + 26]);
//          if (a < 13) {
//            x = bottomRightOffsetX - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
//            y = chequerBottomOffsetY - (b * d.chequerSize);
//            g.fillOval(x, y, d.chequerSize, d.chequerSize);
//            g.setColor(Color.BLACK);
//            g.drawOval(x, y, d.chequerSize, d.chequerSize);
//            if (b == 4 && point[a + 26] > 5) {
//              x = bottomRightOffsetX+ 7 - (a > 6 ? (int)(d.barWidth*1.1) : 0) - (a * d.chequerTotalSpace);
//              y = (int)((d.frameOffsetY+d.boardInnerHeight - (b * d.chequerSize))*0.975);
//              g.drawString(str, x, y);
//            }
//          } else {
//            x = (int)(d.leftPlayAreaOffsetX*1.065) +
//              (a > 18 ? (int)(d.barWidth*1.1) : 0) +
//              ((a - 13) * d.chequerTotalSpace);
//            y = d.frameOffsetY + (b * d.chequerSize);
//            g.fillOval(x, y, d.chequerSize, d.chequerSize);
//            g.setColor(Color.BLACK);
//            g.drawOval(x, y, d.chequerSize, d.chequerSize);
//            if (b == 4 && point[a + 26] > 5) {
//              x = (int)(d.leftPlayAreaOffsetX*1.142) +
//                (a > 18 ? (int)(d.barWidth*1.1) : 0) +
//                ((a - 13) * d.chequerTotalSpace);
//              y = (int)((d.frameOffsetY + ((b+1) * d.chequerSize))*0.965);
//              g.drawString(str, x, y);
//            }
//          }
//        }
//      }
//    }
//    g.setColor(colorYellow);
//    for (int a = 0; a < (point[25] > 6 ? 6 : point[25]); a++) {
//      g.fillOval(
//        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth+5,
//        d.frameOffsetY+(int)(d.boardInnerHeight/2.5) - (a * d.chequerSize),
//        d.chequerSize,
//        d.chequerSize
//      );
//      if (a == 0 && point[25] > 6) {
//        g.setColor(Color.black);
//        g.drawString(
//          Integer.toString(point[25]),
//          (int)((d.leftPlayAreaOffsetX+d.leftPlayAreaWidth + (d.barWidth/2))*0.989),
//          (int)((d.frameOffsetY+(d.boardInnerHeight/2))*0.945)
//        );
//        g.setColor(Color.yellow);
//      }
//    }
//    for (int a = 0; a < point[0]; a++) {
//      g.fill3DRect(
//        d.bottomRightBearOffOffsetX+3,
//        d.bottomRightBearOffOffsetY + (a * (int)(11.3*d.factor)),
//        (int)(30.0*d.factor),
//        (int)(9*d.factor), true
//      );
//    }
//    g.setColor(colorRed);
//    for (int a = 0; a < (point[26] > 6 ? 6 : point[26]); a++) {
//      g.fillOval(
//        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth+5,
//        d.frameOffsetY+(d.boardInnerHeight/2) + (a * d.chequerSize),
//        d.chequerSize,
//        d.chequerSize
//      );
//      if (a == 0 && point[26] > 6) {
//        g.setColor(Color.black);
//        g.drawString(
//          Integer.toString(point[26]),
//          (int)((d.leftPlayAreaOffsetX+d.leftPlayAreaWidth + (d.barWidth/2))*0.989),
//          (int)(((d.frameOffsetY+(d.boardInnerHeight/2)))*1.09)
//        );
//        g.setColor(Color.red);
//      }
//    }
//    for (int a = 0; a < point[51]; a++) {
//      g.fill3DRect(
//        d.topRightBearOffOffsetX +3,
//        d.topRightBearOffOffsetY + (a * (int)(11.3*d.factor)),
//        (int)(30.0*d.factor),
//        (int)(9.0*d.factor),
//        true
//      );
//    }
//  }
//
//}
