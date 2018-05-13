package bg.engine;

public class ScoreBoard extends Score {

  class MatchBoard extends Score.MatchBoard {

    void addGameBoard (GameBoard gameBoard) {

      matchScore += gameBoard.gameScore;
      backgammons += gameBoard.backgammons;
      gammons += gameBoard.gammons;
      singlePoint += gameBoard.singlePoint;
    }

  }

  class GameBoard extends Score.PlayerData {

    public int gameScore = 0;

    void wipe() {

      gameScore = 0;
      backgammons = 0;
      gammons = 0;
      singlePoint = 0;
    }

  }

  private MatchBoard[] matchBoard = new MatchBoard[2];
  protected GameBoard[]  gameBoard = new GameBoard[2];

  public ScoreBoard (int playToScore) {

    super(playToScore);
    for (int a = 0; a < matchBoard.length; a++) {
      matchBoard[a] = new MatchBoard();
      gameBoard[a] = new GameBoard();
    }
  }

  public void setPlayersMatchScore (int playerID, int matchScore) {

    matchBoard[playerID].matchScore = matchScore;
    crawford = 0;
    setCrawford();
  }

  protected int highestMatchScore () {

    int[] matchPlusGameScore = getMatchPlusGameScore();

    return
      matchPlusGameScore[0] > matchPlusGameScore[1] ?
      matchPlusGameScore[0] : matchPlusGameScore[1];
  }

  public void writeMatchScore () {

    for (int a = 0; a < matchBoard.length; a++) {
      if (gameBoard[a].gameScore > 0) {
        matchBoard[a].addGameBoard(gameBoard[a]);
        gameBoard[a].wipe();
      }
    }
    setCrawford();
  }

  public void writeGameScore (Game game) {

    for (int a = 0; a < gameBoard.length; a++) {
      gameBoard[a].wipe();
    }
    writeScore(game);
  }

  public int[] getMatchPlusGameScore () {

    int[] matchPlusGameScore = new int[matchBoard.length];

    matchPlusGameScore[0] = matchBoard[0].matchScore+ gameBoard[0].gameScore;
    matchPlusGameScore[1] = matchBoard[1].matchScore+ gameBoard[1].gameScore;
    return matchPlusGameScore;
  }

  public int getWhiteMatchScore () {

    return matchBoard[0].matchScore;
  }

  public int getBlackMatchScore () {

    return matchBoard[1].matchScore;
  }

}
