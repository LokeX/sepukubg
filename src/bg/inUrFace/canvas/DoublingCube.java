package bg.inUrFace.canvas;

import bg.engine.coreLogic.Cube;
import bg.util.Batch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.Main.engineApi;
import static bg.Main.settings;
import static bg.Main.win;

public class DoublingCube extends MouseAdapter implements Paintable {

  ImageIcon cubeScab = new ImageIcon(this.getClass().getResource("cube/double.gif"));
  ImageIcon cubeIcon;
  int height;
  int width;

  public boolean isVisible () {

    return
      engineApi != null &&
      !engineApi.getMatchBoard().isCrawfordGame() &&
      settings.getScoreToWin() > 1 &&
      engineApi.getLatestTurn() != null;
  }

  private Batch[] getClickPoints () {

    BoardDim d = win.canvas.getDimensions();
    Batch[] clickPoints = new Batch[3];

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

  public void paint(Graphics g) {

    Cube cube;

    if (engineApi != null && engineApi.getSelectedTurn() != null) {
      if (engineApi.getSelectedTurn() != engineApi.getGame().lastTurn()) {
        cube = engineApi.getGame().getGameCube();
      } else {
        cube = engineApi.getSelectedTurn().getTurnCube();
      }
      if (isVisible()) {

        BoardDim d = win.canvas.getDimensions();
        int offsetX = (int)(d.topLeftBearOffOffsetX*1.05);
        int offsetY;
        int textOffsetY;

        if (cube.getOwner() == -1) {
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
          cubeIcon = new ImageIcon(cubeScab.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        g.drawImage(cubeIcon.getImage(), offsetX, offsetY, null);
        g.setColor(Color.red);
        g.setFont(new Font("Ariel", Font.BOLD, (int)(14.0*d.factor)));

        String s = Integer.toString(cube.getValue());

        g.drawString(s, (int)(offsetX*(1.31-(0.095*(s.length()-1)))), textOffsetY);
      }
    }
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (engineApi != null && engineApi.getGame() != null && !engineApi.getGame().gameOver() && getClickedPoint(e) >= 0) {
      System.out.println("DoublingCube clicked");
      engineApi.getMatchCube().humanHandlesCube();
    }
  }

}
