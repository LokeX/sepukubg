package engine.core.score;

public class MatchScore {

  protected PlayerScore[] playerScores = new PlayerScore[2];

  private int crawford = 0;
  private int playToScore;

  public MatchScore (int playToScore) {

    this.playToScore = playToScore;
    playerScores[0] = new PlayerScore();
    playerScores[1] = new PlayerScore();
  }

  public PlayerScore[] getPlayerScores () {

    return playerScores;
  }

  public void addGameScore (GameScore gameScore) {

    playerScores[gameScore.getWinnerID()].addGameScore(gameScore);
    setCrawford();
  }

  public void setPlayToScore (int score) {

    playToScore = score;
    resetCrawford();
  }

  public int getPlayToScore () {

    return playToScore;
  }

  protected void resetCrawford () {

    crawford = 0;
    setCrawford();
  }

  protected int highestMatchScore () {

    return
      Math.max(
        playerScores[0].matchScore(),
        playerScores[1].matchScore()
      );
  }

  protected void setCrawford () {

    if (highestMatchScore() == playToScore - 1) {
      crawford++;
    }
  }

  public boolean isCrawfordGame () {

    return crawford == 1;
  }

  public int getWinnerID () {

    return
      playerScores[0].matchScore() >
      playerScores[1].matchScore() ? 0 : 1;
  }

  public boolean matchOver () {

    return highestMatchScore() >= playToScore;
  }

}
