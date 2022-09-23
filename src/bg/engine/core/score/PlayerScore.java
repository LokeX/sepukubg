package bg.engine.core.score;

public class PlayerScore {

  private int[] scoreStats = new int[3];
  private int matchScore;

  public int nrOfSinglePointsScored () {

    return scoreStats[0];
  }

  public int nrOfGammonsScored () {

    return scoreStats[1];
  }

  public int nrOfBackgammonsScored () {

    return scoreStats[2];
  }

  public int matchScore () {

    return matchScore;
  }

  public void setMatchScore (int score) {

    matchScore = score;
  }

  public void addGameScore (GameScore gameScore) {

    matchScore += gameScore.getTotalScore();
    scoreStats[gameScore.getBaseScore()-1]++;
  }

}
