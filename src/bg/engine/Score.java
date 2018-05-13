
package bg.engine;

import bg.engine.moves.Layout;

public class Score {

  public class PlayerData {

    public int backgammons = 0;
    public int gammons = 0;
    public int singlePoint = 0;

  }

  public class MatchBoard extends PlayerData {

    protected int matchScore = 0;
  }

  protected int playToScore = 7;
  protected int crawford = 0;
  public MatchBoard[] matchBoard = new MatchBoard[2];

  public Score (int playToScore) {

    this.playToScore = playToScore;
    for (int a = 0; a < matchBoard.length; a++) {
      matchBoard[a] = new MatchBoard();
    }
  }

  public int getPlayToScore () {

    return playToScore;
  }

  public int getWinnerID () {

    return matchBoard[0].matchScore > matchBoard[1].matchScore ? 0 : 1;
  }

  protected int highestMatchScore () {

    return
      matchBoard[0].matchScore > matchBoard[1].matchScore ?
      matchBoard[0].matchScore : matchBoard[1].matchScore;
  }

  public void setPlayToScore (int score) {

    playToScore = score;
    setCrawford();
  }

  protected void setCrawford () {

    if (highestMatchScore() == playToScore - 1) {
      crawford++;
    }
  }

  public boolean isCrawfordGame () {

    return crawford == 1;
  }

  public void writeScore(Game game) {

    Turn finalTurn = game.getLatestTurn();
    Cube cube = game.getGameCube();
    int score;
    int rearPos = -1;
    int nrOfBearOffs = -1;
    int winnerID;

    winnerID = finalTurn.getPlayerOnRollsID();
    if (game.getGameCube().cubeWasRejected()) {
      winnerID = winnerID == 0 ? 1 : 0;
    } else {
      Layout flippedLayout =
        finalTurn.getPlayedMove().getFlippedLayout();

      rearPos = flippedLayout.rearPos();
      nrOfBearOffs = flippedLayout.getPoint()[0];
    }
    if (rearPos < 0) {
      score = 1;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoard[winnerID].singlePoint++;
      } else {
        matchBoard[winnerID].singlePoint++;
      }
    } else if (rearPos > 18) {
      score = 3;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoard[winnerID].backgammons++;
      } else {
        matchBoard[winnerID].backgammons++;
      }
    } else if (rearPos > 6 || nrOfBearOffs == 0) {
      score = 2;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoard[winnerID].gammons++;
      } else {
        matchBoard[winnerID].gammons++;
      }
    } else {
      score = 1;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoard[winnerID].singlePoint++;
      } else {
        matchBoard[winnerID].singlePoint++;
      }
    }
    score = score * (cube.getOwner() >= 0 ? cube.getValue() : 1);
    if (this instanceof ScoreBoard) {
      ((ScoreBoard) this).gameBoard[winnerID].gameScore = score;
    } else {
      matchBoard[winnerID].matchScore += score;
      setCrawford();
    }
  }

  public boolean matchOver () {

    return highestMatchScore() >= playToScore;
  }

}
