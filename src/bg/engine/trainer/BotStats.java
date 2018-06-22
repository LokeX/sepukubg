package bg.engine.trainer;

import bg.engine.Score;

import java.io.Serializable;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BotStats implements Serializable {

  class Stats implements Serializable {

    private int matchesPlayed = 0;
    private int matchesWon = 0;
    private int singlePointsWon = 0;
    private int gammonsWon = 0;
    private int backgammonsWon = 0;
    private String opponentName;

    private String getWinningPercentage () {

      return new DecimalFormat("#.00").
        format(((((float)matchesWon)/(float)matchesPlayed)*100));
    }

    void printStats () {

      System.out.println("Opponents name: "+opponentName);
      System.out.println("Name: "+name);
      System.out.println("Matches played: "+matchesPlayed);
      System.out.println("Matches won: "+matchesWon);
      System.out.println("Games won: "+singlePointsWon);
      System.out.println("Gammons won: "+(gammonsWon));
      System.out.println("Backgammons won: "+(backgammonsWon));
      System.out.println("Total points won: "+(singlePointsWon+(2*gammonsWon)+(3*backgammonsWon)));
      System.out.println("Winning percentage ["+name+"]: "+getWinningPercentage()+"%");
      System.out.println();
    }

    String getBotReport () {

      DecimalFormat df = new DecimalFormat("#,#00");

      return
        "Opponents name: "+opponentName+
        "\nName: "+name+
        "\nMatches played: "+df.format(matchesPlayed)+
        "\nMatches won: "+df.format(matchesWon)+
        "\nGames won: "+df.format(singlePointsWon)+
        "\nGammons won: "+df.format(gammonsWon)+
        "\nBackgammons won: "+df.format(backgammonsWon)+
        "\nTotal points won: "+df.format((singlePointsWon+(2*gammonsWon)+(3*backgammonsWon)))+
        "\nEngineApi win percentage ["+name+"]: "+getWinningPercentage()+"%\n\n";
    }

    String getOpponentsName () {

      return opponentName;
    }

    void  setOpponentName (String name) {

      opponentName = name;
    }

    void addScores (Score score, int thisPlayerID) {

      if (score.getWinnerID() == thisPlayerID) {
        matchesWon++;
      }
      matchesPlayed++;
      singlePointsWon += score.matchBoard[thisPlayerID].singlePoint;
      gammonsWon += score.matchBoard[thisPlayerID].gammons;
      backgammonsWon += score.matchBoard[thisPlayerID].backgammons;
    }

  }

  BotStats (String name) {

    this.name = name;
  }

  private List<Stats> botStats = new ArrayList<>();
  private String name;

  private int getBotOpponentsNr (Bot bot) {

    for (int a = 0; a < botStats.size(); a++) {
      if (botStats.get(a).getOpponentsName().equals(bot.getName())) {
        return a;
      }
    }
    return -1;
  }

  public String getBotReport (Bot bot) {

    return botStats.get(getBotOpponentsNr(bot)).getBotReport();
  }

  public void addScore (Score score, int thisPlayerID, Bot opponent) {

    int botOpponentNr = getBotOpponentsNr(opponent);
    Stats stats = botOpponentNr > -1 ? botStats.get(botOpponentNr) : new Stats();

    stats.addScores(score, thisPlayerID);
    if (botOpponentNr < 0) {
      stats.setOpponentName(opponent.getName());
      botStats.add(stats);
    }
  }

  public void printStats (Bot opponent) {

    botStats.get(getBotOpponentsNr(opponent)).printStats();
  }

  private List<String> getBotReports() {

    return botStats.stream().
      map(Stats::getBotReport).
      collect(Collectors.toList());
  }

  public String getStatReport () {

    return getBotReports().stream().
      reduce(String::concat).
      orElse("No data");
  }

}