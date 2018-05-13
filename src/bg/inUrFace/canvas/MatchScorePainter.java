package bg.inUrFace.canvas;

import static bg.Main.win;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import static bg.Main.matchApi;

public class MatchScorePainter implements Paintable {

  private boolean latestTurnSelected () {

    return
      matchApi.getGame() != null &&
      matchApi.getSelectedTurn() == matchApi.getGame().getLatestTurn();
  }

  @Override
  public void paint(Graphics g) {

    if (matchApi != null) {
      BoardDim d = win.canvas.getDimensions();
      String toScore = Integer.toString(matchApi.getScoreBoard().getPlayToScore());
//      int whiteMatchScore = api.getScoreBoard().getWhiteMatchScore();
//      int blackMatchScore = api.getScoreBoard().getBlackMatchScore();
//
//      if (api.getGame() != null && latestTurnSelected() && api.getGame().gameOver()) {
//        whiteMatchScore += api.getScoreBoard().getWhiteGameScore();
//        blackMatchScore += api.getScoreBoard().getBlackGameScore();
//      }

      int whiteMatchScore;
      int blackMatchScore;

      if (matchApi.getGame() != null && latestTurnSelected() && matchApi.getGame().gameOver()) {
        whiteMatchScore = matchApi.getScoreBoard().getMatchPlusGameScore()[0];
        blackMatchScore = matchApi.getScoreBoard().getMatchPlusGameScore()[1];
      } else {
        whiteMatchScore = matchApi.getScoreBoard().getWhiteMatchScore();
        blackMatchScore = matchApi.getScoreBoard().getBlackMatchScore();
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
