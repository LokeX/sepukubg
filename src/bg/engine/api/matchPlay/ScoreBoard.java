package bg.engine.api.matchPlay;

import bg.engine.match.Game;
import bg.engine.match.Score;

public class ScoreBoard extends Score {

  public class MatchBoard extends Score.MatchBoard {

    void addGameBoard (GameBoard gameBoard) {

      matchScore += gameBoard.gameScore;
      backgammons += gameBoard.backgammons;
      gammons += gameBoard.gammons;
      singlePoint += gameBoard.singlePoint;
    }

  }

  public class GameBoard extends Score.PlayerData {

    public int gameScore = 0;

    void wipe() {

      gameScore = 0;
      backgammons = 0;
      gammons = 0;
      singlePoint = 0;
    }

  }

  private MatchBoard[] matchBoards = new MatchBoard[2];
  public GameBoard[] gameBoards = new GameBoard[2];

  ScoreBoard (int playToScore) {

    super(playToScore);
    for (int a = 0; a < matchBoards.length; a++) {
      matchBoards[a] = new MatchBoard();
      gameBoards[a] = new GameBoard();
    }
  }

  public void setPlayersMatchScore (int playerID, int matchScore) {

    matchBoards[playerID].matchScore = matchScore;
    resetCrawford();
    setCrawford();
  }

  protected int highestMatchScore () {

    int[] matchPlusGameScore = getMatchPlusGameScore();

    return
      matchPlusGameScore[0] > matchPlusGameScore[1] ?
      matchPlusGameScore[0] : matchPlusGameScore[1];
  }

  void writeMatchScore () {

    for (int a = 0; a < matchBoards.length; a++) {
      if (gameBoards[a].gameScore > 0) {
        matchBoards[a].addGameBoard(gameBoards[a]);
        gameBoards[a].wipe();
      }
    }
    setCrawford();
  }

  void writeGameScore (Game game) {

    for (GameBoard gameBoard : gameBoards) {
      gameBoard.wipe();
    }
    writeScore(game);
  }

  public int[] getMatchPlusGameScore () {

    int[] matchPlusGameScore = new int[matchBoards.length];

    matchPlusGameScore[0] = matchBoards[0].matchScore+ gameBoards[0].gameScore;
    matchPlusGameScore[1] = matchBoards[1].matchScore+ gameBoards[1].gameScore;
    return matchPlusGameScore;
  }

  public int getWhiteMatchScore () {

    return matchBoards[0].matchScore;
  }

  public int getBlackMatchScore () {

    return matchBoards[1].matchScore;
  }

}
