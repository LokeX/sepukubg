package bg.engine.coreLogic.score;

import bg.engine.coreLogic.Cube;
import bg.engine.coreLogic.Game;
import bg.engine.coreLogic.Turn;
import bg.engine.coreLogic.moves.Layout;

public class GameScore {

  private Turn finalTurn;
  private Cube cube;

  private int baseScore;
  private int totalScore;
  private int winnerID;

  public GameScore (Game game) {

    finalTurn = game.lastTurn();
    cube = game.getGameCube();
    calcScore();
  }

  private Layout flippedLayout () {

    return
      finalTurn
        .getPlayedMove()
        .getFlippedLayout();
  }

  private void calcScore () {

    int rearPos = -1;
    int nrOfBearOffs = -1;

    winnerID = finalTurn.getPlayerOnRollsID();
    if (cube.cubeWasRejected()) {
      winnerID = winnerID == 0 ? 1 : 0;
    } else {
      Layout flippedLayout = flippedLayout();
      rearPos = flippedLayout.rearPos();
      nrOfBearOffs = flippedLayout.getPoint()[0];
    }
    if (rearPos < 0) {
      baseScore = 1;
    } else if (rearPos > 18) {
      baseScore = 3;
    } else if (rearPos > 6 || nrOfBearOffs == 0) {
      baseScore = 2;
    } else {
      baseScore = 1;
    }
    totalScore = baseScore * (cube.getOwner() >= 0 ? cube.getValue() : 1);
  }

  int getBaseScore () {

    return baseScore;
  }

  public int getTotalScore() {

    return totalScore;
  }

  public int getWinnerID () {

    return winnerID;
  }

}
