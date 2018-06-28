
package bg.engine.match;

import bg.engine.api.matchPlay.ScoreBoard;
import bg.engine.match.moves.Layout;

public class Score {

  public class PlayerData {

    public int backgammons = 0;
    public int gammons = 0;
    public int singlePoint = 0;

  }

  public class MatchBoard extends PlayerData {

    public int matchScore = 0;
  }

  private int playToScore = 7;
  private int crawford = 0;
  public MatchBoard[] matchBoards = new MatchBoard[2];

  public Score (int playToScore) {

    this.playToScore = playToScore;
    for (int a = 0; a < matchBoards.length; a++) {
      matchBoards[a] = new MatchBoard();
    }
  }

  public int getPlayToScore () {

    return playToScore;
  }

  public int getWinnerID () {

    return matchBoards[0].matchScore > matchBoards[1].matchScore ? 0 : 1;
  }

  protected int highestMatchScore () {

    return
      matchBoards[0].matchScore > matchBoards[1].matchScore ?
      matchBoards[0].matchScore : matchBoards[1].matchScore;
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

  protected void resetCrawford () {

    crawford = 0;
  }

  public boolean isCrawfordGame () {

    return crawford == 1;
  }

  protected void writeScore(Game game) {

    Turn finalTurn = game.lastTurn();
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
        ((ScoreBoard) this).gameBoards[winnerID].singlePoint++;
      } else {
        matchBoards[winnerID].singlePoint++;
      }
    } else if (rearPos > 18) {
      score = 3;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoards[winnerID].backgammons++;
      } else {
        matchBoards[winnerID].backgammons++;
      }
    } else if (rearPos > 6 || nrOfBearOffs == 0) {
      score = 2;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoards[winnerID].gammons++;
      } else {
        matchBoards[winnerID].gammons++;
      }
    } else {
      score = 1;
      if (this instanceof ScoreBoard) {
        ((ScoreBoard) this).gameBoards[winnerID].singlePoint++;
      } else {
        matchBoards[winnerID].singlePoint++;
      }
    }
    score = score * (cube.getOwner() >= 0 ? cube.getValue() : 1);
    if (this instanceof ScoreBoard) {
      ((ScoreBoard) this).gameBoards[winnerID].gameScore = score;
    } else {
      matchBoards[winnerID].matchScore += score;
      setCrawford();
    }
  }

  public boolean matchOver () {

    return highestMatchScore() >= playToScore;
  }

}
