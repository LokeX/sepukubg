package engine.play.score;

import engine.play.PlayMatch;

public class ScoreBoard {

  private MatchBoard matchBoard;
  private PlayMatch playMatch;

  public ScoreBoard (PlayMatch playMatch) {

    matchBoard = playMatch.getMatchBoard();
    this.playMatch = playMatch;
  }

  public boolean gameOver () {

    return
      playMatch.gameOver()
      && playMatch.lastTurnSelected();
  }

  private int [] matchScores () {

    int[] matchScores = new int[2];

    if (gameOver()) {
      matchScores[0] = matchBoard.matchPlusGameScores()[0];
      matchScores[1] = matchBoard.matchPlusGameScores()[1];
    } else {
      matchScores[0] = matchBoard.getWhiteMatchScore();
      matchScores[1] = matchBoard.getBlackMatchScore();
    }
    return matchScores;
  }

  public String getWhiteMatchScore () {

    return Integer.toString(matchScores()[0]);
  }

  public String getBlackMatchScore () {

    return Integer.toString(matchScores()[1]);
  }

  public int getPlayToScore () {

    return matchBoard.getPlayToScore();
  }

  public int getWinnerID () {

    return matchBoard.getWinnerID();
  }

  public boolean matchOver () {

    return matchBoard.matchOver();
  }

}
