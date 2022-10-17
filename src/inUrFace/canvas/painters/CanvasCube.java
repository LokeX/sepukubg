package inUrFace.canvas.painters;

import engine.core.Cube;
import inUrFace.canvas.BoardDim;
import util.Batch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.win;

public class CanvasCube extends MouseAdapter implements Paintable {

  ImageIcon cubeScab = new ImageIcon(
    this.getClass().getResource("cube/double.gif")
  );
  ImageIcon cubeIcon;
  int height;
  int width;

  public boolean isVisible () {

    return
      sepukuPlay != null
      && sepukuPlay.gameIsPlaying()
      && !sepukuPlay.matchBoard().isCrawfordGame()
      && sepukuPlay.settings().getScoreToWin() > 1;
  }

  private Batch[] getClickPoints () {

    BoardDim d = win.canvas.getDimensions();
    Batch[] clickPoints = new Batch[4];

    clickPoints[0] = new Batch(
      d.bottomLeftBearOffOffsetX,
      d.bottomLeftBearOffOffsetY,
      d.bearOffWidth,
      d.bearOffHeight
    );
    clickPoints[1] = new Batch(
      d.topLeftBearOffOffsetX,
      d.topLeftBearOffOffsetY,
      d.bearOffWidth,
      d.bearOffHeight
    );
    clickPoints[2] = new Batch(
      d.topLeftBearOffOffsetX,
      d.diePocketOffsetY,
      d.bearOffWidth,
      d.diePocketHeight
    );
    clickPoints[3] = new Batch(
      (int) (d.rightPlayAreaOffsetX*1.03),
      d.diePocketOffsetY,
      d.bearOffWidth,
      d.diePocketHeight
    );
    return clickPoints;
  }

  private int getClickedPoint (MouseEvent e) {

    Batch[] clickPoints = getClickPoints();

    for (int a = 0; a < clickPoints.length; a++) {
      if (clickPoints[a].mouseOnBatch(e)) {
        return a;
      }
    }
    return -1;
  }
  
  private boolean cubeOffered () {
    
    return
      sepukuPlay.matchCube().cubeIsOffered();
  }
  
  public void paint(Graphics g) {
    
    if (isVisible()) {

      Cube cube = sepukuPlay.turnCube();
      BoardDim d = win.canvas.getDimensions();
      int offsetX = (int)(d.topLeftBearOffOffsetX*1.05);
      int offsetY;
      int textOffsetY;

      if (cubeOffered()) {
        offsetX = (int) (d.rightPlayAreaOffsetX*1.03);
        offsetY = (int)(d.diePocketOffsetY*1.07);
        textOffsetY =  (int)(offsetY*1.07);
      } else if (cube.getOwner() == -1) {
        offsetY = (int)(d.diePocketOffsetY*1.07);
        textOffsetY =  (int)(offsetY*1.07);
      } else if (cube.getOwner() == 0) {
        offsetY = (int)((d.bottomLeftBearOffOffsetY+d.bearOffHeight)*0.94);
        textOffsetY =  (int)(offsetY*1.04);
      } else {
        offsetY = (int)(d.topLeftBearOffOffsetY*1.07);
        textOffsetY =  (int)(offsetY*1.27);
      }

      int newHeight = (int)(cubeScab.getIconHeight()*(d.factor/1.7));
      int newWidth  = (int)(cubeScab.getIconWidth()*(d.factor/1.7));

      if (cubeIcon == null || newWidth != width || newHeight != height) {
        height = newHeight;
        width = newWidth;
        cubeIcon = new ImageIcon(
          cubeScab.getImage().getScaledInstance(
            width,
            height,
            Image.SCALE_SMOOTH
          )
        );
      }
      g.drawImage(cubeIcon.getImage(), offsetX, offsetY, null);
      g.setColor(Color.red);
      g.setFont(new Font("Ariel", Font.BOLD, (int)(14.0*d.factor)));

      String cubeValue = Integer.toString(cube.getValue());

      g.drawString(
        cubeValue,
        (int)(offsetX*(cubeOffered() ? 1.027 : 1.29-(0.095*(cubeValue.length()-1)))),
        textOffsetY
      );
    }
  }
  
  private boolean gameIsPlaying () {
    
    return
      sepukuPlay.gameIsPlaying();
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (isVisible()) {
      int clickedPoint = getClickedPoint(e);
      
      if (gameIsPlaying() && clickedPoint >= 0) {
        System.out.println("Cube clicked");
        if (cubeOffered() && clickedPoint == 3) {
          System.out.println("human accepts cube");
          sepukuPlay.matchCube().humanAcceptsCube();
        } else {
          System.out.println("human handles cube");
          sepukuPlay.matchCube().humanHandlesCube();
        }
      }
    }
  }

}