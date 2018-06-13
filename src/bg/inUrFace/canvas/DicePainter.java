package bg.inUrFace.canvas;

import static bg.Main.matchApi;
import static bg.Main.mouse;
import static bg.Main.win;

import java.awt.*;
import javax.swing.ImageIcon;

public class DicePainter implements Paintable {

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
      boolean rescaled = false;

      if (newDieWidth != oldDieWidth || newDieHeight != oldDieHeight || oldDiceLength != dice.length) {
        rescaled = true;
        dieWidth = newDieWidth;
        dieHeight = newDieHeight;

        dieFaceIcons[dieFace] = new ImageIcon(dieFaces[dieFace]
          .getImage()
//          .getScaledInstance(dieWidth, dieHeight, Image.SCALE_DEFAULT)
        );
      }
//      if (oldDiceLength != dice.length || rescaled) {
        oldDiceLength = dice.length;
        centerX = d.leftPlayAreaOffsetX+(d.leftPlayAreaWidth-(((int)(dieWidth*1.2))*dice.length))/2;
        centerY = (int)((d.frameOffsetY+(d.boardInnerHeight/2))*0.95);
//      }
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

  public DicePainter () {

    for (int a = 0; a < dieFaces.length; a++) {
      dieFaces[a] = new ImageIcon(
        this.getClass().getResource("Dice/" + Integer.toString(a + 1) + ".gif")
      );
      dieFaceIcons[a] = new ImageIcon(dieFaces[a].getImage());
    }
  }

  private void makeDice () {

    dice = matchApi.getSelectedTurn().getDice();
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

    if (matchApi != null && matchApi.getSelectedTurn() != null) {
      makeDice();

      boolean pointsAreInput =
        matchApi
          .turnsPlayerIsHuman(matchApi.getLatestTurn())
          && mouse.getMoveInputListener().acceptInput();
      int nrOfLegalPartMoves = matchApi.getSelectedMove().getNrOfPartMoves();
      int nrOfPartMoves = 0;
      int[] shades = new int[dice.length];
      int[] legalMovePoints = matchApi.getSelectedMove().getMovePoints();
      int[] movePoints = !pointsAreInput ? legalMovePoints :
              dice.length == 2 && nrOfLegalPartMoves == 1 ? legalMovePoints :
              mouse.getMoveInputListener().getMoveInput().getMovePoints();

      if (movePoints.length != dice.length * 2) {
        movePoints = new int[dice.length*2];
      }
      for (int a = 0; a < movePoints.length; a += 2) {
        if (movePoints[a] >= 0 && movePoints[a+1] >= 0) {
          nrOfPartMoves++;
          if (matchApi.getSelectedTurn().getPlayerOnRollsID() == 0) {
            shades[a/2] = movePoints[a] - movePoints[a+1];
          } else {
            shades[a/2] = movePoints[a+1] - movePoints[a];
          }
        } else if (matchApi.getSelectedMove().isIllegal()) {
          shades[a/2] = 1;
        }
      }
      if (!matchApi.getSelectedMove().isIllegal()) {
        if (dice.length == 4) {
          for (int a = 0; a < nrOfPartMoves-(shades.length-nrOfLegalPartMoves); a++) {
            shades[a] = 1;
          }
        } else if (nrOfPartMoves == 1) {

          int usedDie = shades[0] > 0 ? 0 : 1;
          int otherDie = usedDie == 0 ? 1 : 0;

          if (shades[usedDie] != dice[usedDie] && shades[usedDie] <= dice[otherDie]) {
            shades[otherDie] = shades[usedDie];
            shades[usedDie] = 0;
          }
          if (nrOfLegalPartMoves == 1) {
            if (pointsAreInput) {

              int temp = shades[0];

              shades[0] = shades[1];
              shades[1] = temp;
            } else for (int a = 0; a < shades.length; a++) {
              shades[a] = 1;
            }
          }
        }
      }
      g.setColor(new Color(0,0,0,125));
      for (int a = 0; a < dice.length; a++) {
        dieIcons[a].paintDie(g);
        if (shades[a] > 0) {
          dieIcons[a].paintShade(g);
        }
      }
    }
  }

}
