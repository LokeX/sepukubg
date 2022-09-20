package bg.engine.api.score;

import bg.engine.api.matchPlay.GameState;
import bg.engine.api.matchPlay.MatchPlay;

public class ScoreBoard {

  private MatchBoard matchBoard;
  private GameState gameState;

  public ScoreBoard (MatchPlay matchPlay) {

    matchBoard = matchPlay.getMatchBoard();
    gameState = matchPlay.getGameState();
  }

  public boolean gameOver () {

    return
      gameState != null
      && gameState.lastTurnSelected()
      && gameState.gameOver();
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
