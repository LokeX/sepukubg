package bg.inUrFace.canvas;

import static bg.Main.win;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import static bg.Main.sepuku;

public class MatchScorePainter implements Paintable {

  @Override
  public void paint(Graphics g) {

    BoardDim d = win.canvas.getDimensions();
    String toScore = Integer.toString(sepuku.getScoreBoard().getPlayToScore());
    String whiteScore = sepuku.getScoreBoard().getWhiteMatchScore();
    String blackScore = sepuku.getScoreBoard().getBlackMatchScore();
    
    g.setFont(new Font("Ariel", Font.BOLD, (int)(26*d.factor)));

    g.setColor(Color.yellow);
    g.drawString(whiteScore,
      (int)(d.topLeftBearOffOffsetX*(1.25-(0.13*(whiteScore.length()-1)))),
      (int)(d.bottomLeftBearOffOffsetY*1.08));

    g.setColor(Color.red);
    g.drawString(blackScore,
      (int)(d.topLeftBearOffOffsetX*(1.25-(0.13*(blackScore.length()-1)))),
      (int)((d.topLeftBearOffOffsetY+d.bearOffHeight)*0.97));

    g.setColor(Color.PINK);
    g.drawString(toScore,
      (int)(d.bottomRightBearOffOffsetX*(1.015-(0.008*(toScore.length()-1)))),
      (int)((d.diePocketOffsetY)*1.16));
  }

}
