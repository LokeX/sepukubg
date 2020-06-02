package bg.engine.api.score;

import bg.engine.coreLogic.score.GameScore;
import bg.engine.coreLogic.score.MatchScore;

public class MatchBoard extends MatchScore {

  private GameScore gameScore;

  public MatchBoard (int playToScore) {

    super(playToScore);
  }

  @Override
  public void addGameScore (GameScore gameScore) {

    this.gameScore = gameScore;
  }

  public void mergeScores () {

    playerScores[gameScore.getWinnerID()].addGameScore(gameScore);
    gameScore = null;
    setCrawford();
  }

  public void setPlayersMatchScore (int playerID, int matchScore) {

    playerScores[playerID].setMatchScore(matchScore);
    resetCrawford();
  }

  public int[] matchPlusGameScores () {

    int[] matchPlusGameScores = new int[2];

    matchPlusGameScores[0] = playerScores[0].matchScore();
    matchPlusGameScores[1] = playerScores[1].matchScore();
    if (gameScore != null) {
      matchPlusGameScores[gameScore.getWinnerID()] +=
        gameScore.getTotalScore();
    }
    return
      matchPlusGameScores;
  }

  @Override
  public int highestMatchScore () {

    int[] matchPlusGameScores = matchPlusGameScores();

    return
        matchPlusGameScores[0] >
        matchPlusGameScores[1]
      ? matchPlusGameScores[0]
      : matchPlusGameScores[1];
  }

  int getWhiteMatchScore () {

    return playerScores[0].matchScore();
  }

  int getBlackMatchScore () {

    return playerScores[1].matchScore();
  }

}
