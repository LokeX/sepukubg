package bg.inUrFace.canvas;

import static bg.Main.win;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import static bg.Main.engineApi;

public class MatchScorePainter implements Paintable {

  private boolean latestTurnSelected () {

    return
      engineApi.getGame() != null &&
      engineApi.getSelectedTurn() == engineApi.getGame().getLatestTurn();
  }

  @Override
  public void paint(Graphics g) {

    if (engineApi != null) {
      BoardDim d = win.canvas.getDimensions();
      String toScore = Integer.toString(engineApi.getScoreBoard().getPlayToScore());
//      int whiteMatchScore = api.getScoreBoard().getWhiteMatchScore();
//      int blackMatchScore = api.getScoreBoard().getBlackMatchScore();
//
//      if (api.getGame() != null && latestTurnIsSelected() && api.getGame().gameOver()) {
//        whiteMatchScore += api.getScoreBoard().getWhiteGameScore();
//        blackMatchScore += api.getScoreBoard().getBlackGameScore();
//      }

      int whiteMatchScore;
      int blackMatchScore;

      if (engineApi.getGame() != null && latestTurnSelected() && engineApi.getGame().gameOver()) {
        whiteMatchScore = engineApi.getScoreBoard().getMatchPlusGameScore()[0];
        blackMatchScore = engineApi.getScoreBoard().getMatchPlusGameScore()[1];
      } else {
        whiteMatchScore = engineApi.getScoreBoard().getWhiteMatchScore();
        blackMatchScore = engineApi.getScoreBoard().getBlackMatchScore();
      }

      String whiteScore = Integer.toString(whiteMatchScore);
      String blackScore = Integer.toString(blackMatchScore);

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

}
