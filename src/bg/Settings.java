package bg;

import java.io.Serializable;

public class Settings implements Serializable {

    public final int COMPUTER = 1;
    public final int HUMAN = 0;

    public int[] playerStatus = new int[] {HUMAN,COMPUTER};

    private int blackBotOpponent = 0;
    private int whiteBotOpponent = 0;
    private int canvasWidth = 1000;
    private int canvasHeight = 575;
    private int showMoveDelay = 300;
    private int bonusDisplayMode = 0;
    private int gameStartMode = 2;
    private int frameX = -1;
    private int frameY = -1;
    private int automateMoves = 0;
    private boolean automateEndTurn = false;
    private boolean searchReportOn = false;
    private boolean winMaximized = false;
    private int scoreToWin = 7;
    private int nrOfMovesToSearch = 3;
    private int searchToPly = 0;
    private int trainerWhiteBot = 0;
    private int trainerBlackBot = 0;
    private boolean lookaheadForAllPlayers = true;

    public boolean getLookaheadForAllPlayers() {

      return lookaheadForAllPlayers;
    }

    public boolean getWinMaximized () {

      return winMaximized;
    }

    public void setWinMaximized (boolean maximized) {

      winMaximized = maximized;
    }

    public void setLookaheadForAllPlayers(boolean allPlayers) {

      lookaheadForAllPlayers = allPlayers;
    }

    public boolean playerIsHuman (int playerID) {

      return playerStatus[playerID] == HUMAN;
    }

  public boolean playerIsComputer (int playerID) {

    return playerStatus[playerID] == COMPUTER;
  }

  public boolean isSearchReportOn() {

      return searchReportOn;
    }

    public void setSearchReportOn(boolean onOff) {

      searchReportOn = onOff;
    }

    public int getWhiteBotOpponent() {

      return whiteBotOpponent;
    }

    public void setWhiteBotOpponent(int botNr) {

      whiteBotOpponent = botNr;
    }

    public int getBlackBotOpponent () {

      return blackBotOpponent;
    }

    public void setBlackBotOpponent (int botNr) {

      blackBotOpponent = botNr;
    }

    public int getCanvasWidth () {

      return canvasWidth;
    }

    public void setCanvasWidth (int width) {

      canvasWidth = width;
    }

    public int getCanvasHeight () {

      return canvasHeight;
    }

    public void setCanvasHeight (int height) {

      canvasHeight = height;
    }

    public int getTrainerWhiteBot() {

      return trainerWhiteBot;
    }

    public void setTrainerWhiteBot(int botNr) {

      trainerWhiteBot = botNr;
    }

    public int getTrainerBlackBot() {

      return trainerBlackBot;
    }

    public void setTrainerBlackBot(int botNr) {

      trainerBlackBot = botNr;
    }

    public int getSearchToPly () {

//      System.out.println("Settings return search ply: "+searchToPly);
      return searchToPly;
    }

    public void setSearchToPly (int ply) {

      searchToPly = ply;
//      System.out.println("Settings now set to search ply: "+searchToPly);
    }

    public boolean searchIsOff () {

      return searchToPly == 0;
    }

    public int getScoreToWin () {

      return scoreToWin;
    }

    public void setNrOfMovesToSearch(int limit) {

      nrOfMovesToSearch = limit;
    }

    public int getNrOfMovesToSearch() {

      return nrOfMovesToSearch;
    }

    public void setScoreToWin (int score) {

      scoreToWin = score;
    }

    public boolean humanPlayerExists () {

      return playerStatus[0] == HUMAN || playerStatus[1] == HUMAN;
    }

    public boolean computerPlayerExists () {

      return playerStatus[0] == COMPUTER || playerStatus[1] == COMPUTER;
    }

    public boolean isAutomatedEndTurn () {

      return automateEndTurn;
    }

    public void setAutomateEndTurn (boolean autoMode) {

      automateEndTurn = autoMode;
    }

    public boolean isAutoCompleteMoves () {

      return automateMoves == 1;
    }

    public boolean isAutoCompletePartMoves () {

      return automateMoves == 2;
    }

    public void setAutoMoves (int autoMode) {

      automateMoves = autoMode;
    }

    public int getAutoMoves () {

      return automateMoves;
    }

    public int getShowMoveDelay () {

      return showMoveDelay;
    }

    public void setShowMoveDelay (int delay) {

      showMoveDelay = delay;
    }

    public void setFrameX (int fx) {

      frameX = fx;
    }

    public void setFrameY (int fy) {

      frameY = fy;
    }

    public int getFrameX () {

      return frameX;
    }

    public int getFrameY () {

      return frameY;
    }

    public void setPlayerStatus (int player, int status) {

      playerStatus[player] = status;
    }

    public int getGameStartMode () {

      return gameStartMode;
    }

    public void setGameStartMode (int mode) {

      gameStartMode = mode;
    }

    public void setBonusDisplayMode (int mode) {

      bonusDisplayMode = mode;
    }

    public void toggleBonusDisplayMode () {

      if (++bonusDisplayMode > 3) {
        bonusDisplayMode = 0;
      }
    }

    public int getBonusDisplayMode () {

      return bonusDisplayMode;
    }

  }
