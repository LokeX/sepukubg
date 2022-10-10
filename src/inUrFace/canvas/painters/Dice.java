package inUrFace.canvas.painters;

import inUrFace.canvas.BoardDim;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.win;

import java.awt.*;
import javax.swing.ImageIcon;

public class Dice implements Paintable {

  private int[] dice;
  private DieIcon[] dieIcons = new DieIcon[4];
  private ImageIcon[] dieFaces = new ImageIcon[6];
  private ImageIcon[] dieFaceIcons = new ImageIcon[6];
  private int dieWidth;
  private int dieHeight;

  class DieIcon {

    public int dieFace;
    public int diePosition;
    private int centerX;
    private int centerY;
    private int oldDiceLength;

    public void paintDie (Graphics g) {

      BoardDim d = win.canvas.getDimensions();
      int newDieWidth = (int)(dieFaces[dieFace].getIconWidth()*(d.factor/1.65));
      int newDieHeight = (int)(dieFaces[dieFace].getIconHeight()*(d.factor/1.65));
      int oldDieWidth = (dieFaceIcons[dieFace].getIconWidth());
      int oldDieHeight = (dieFaceIcons[dieFace].getIconHeight());

      if (newDieWidth != oldDieWidth || newDieHeight != oldDieHeight || oldDiceLength != dice.length) {
        dieWidth = newDieWidth;
        dieHeight = newDieHeight;
        dieFaceIcons[dieFace] = new ImageIcon(dieFaces[dieFace].getImage());
      }
      oldDiceLength = dice.length;
      centerX = d.leftPlayAreaOffsetX+(d.leftPlayAreaWidth-(((int)(dieWidth*1.2))*dice.length))/2;
      centerY = (int)((d.frameOffsetY+(d.boardInnerHeight/2))*0.95);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR
      );
      g2.drawImage(dieFaceIcons[dieFace].getImage(),
        centerX+(diePosition*(int)(dieWidth*1.2)),
        centerY, dieWidth, dieHeight, null);
    }

    public void paintShade (Graphics g) {

      g.fillRect(centerX+(diePosition*(int)(dieWidth*1.2)),centerY, dieHeight, dieWidth);
    }

    public void setDieFace(int dieFace) {

      this.dieFace = dieFace;
    }

    public void setDiePosition(int diePosition) {

      this.diePosition = diePosition;
    }

  }

  public Dice() {

    for (int a = 0; a < dieFaces.length; a++) {
      dieFaces[a] = new ImageIcon(
        getClass().getResource("dice/" + (a + 1) + ".gif")
      );
      dieFaceIcons[a] = new ImageIcon(dieFaces[a].getImage());
    }
  }

  private void makeDice () {

    dice = sepukuPlay.dice();
    for (int a = 0; a < dice.length; a++) {
      if (dieIcons[a] == null) {
        dieIcons[a] = new DieIcon();
      }
      dieIcons[a].setDieFace(dice[a]-1);
      dieIcons[a].setDiePosition(a);
    }
  }

  @Override
  public void paint (Graphics g) {

    if (sepukuPlay.gameIsPlaying()) {
      makeDice();
      g.setColor(new Color(0,0,0,125));
      for (int a = 0; a < dice.length; a++) {
        dieIcons[a].paintDie(g);
        if (sepukuPlay.getUsedDicePattern()[a] > 0) {
          dieIcons[a].paintShade(g);
        }
      }
    }
  }

}