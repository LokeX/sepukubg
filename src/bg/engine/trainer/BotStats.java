package bg.engine.trainer;

import bg.engine.match.score.MatchScore;
import bg.engine.match.score.PlayerScore;

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
        "\nWinning percentage ["+name+"]: "+getWinningPercentage()+"%\n\n";
    }

    String getOpponentsName () {

      return opponentName;
    }

    void setOpponentsName(String name) {

      opponentName = name;
    }

    void addScores (MatchScore score, int thisPlayerID) {

      PlayerScore playerScore = score.getPlayerScores()[thisPlayerID];

      if (score.getWinnerID() == thisPlayerID) {
        matchesWon++;
      }
      matchesPlayed++;
      singlePointsWon += playerScore.nrOfSinglePointsScored();
      gammonsWon      += playerScore.nrOfGammonsScored();
      backgammonsWon  += playerScore.nrOfBackgammonsScored();
    }

  }

  BotStats (String name) {

    this.name = name;
  }

  private List<Stats> botStats = new ArrayList<>();
  private String name;

  private int getBotOpponentsNr (Bot bot) {

    for (int a = 0; a < botStats.size(); a++) {
      if (botStats.get(a).getOpponentsName().equals(bot.name())) {
        return a;
      }
    }
    return -1;
  }

  String getBotReport (Bot bot) {

    return botStats.get(getBotOpponentsNr(bot)).getBotReport();
  }

  void addScore (MatchScore score, int thisPlayerID, Bot opponent) {

    int botOpponentsNr = getBotOpponentsNr(opponent);
    Stats stats = botOpponentsNr > -1 ? botStats.get(botOpponentsNr) : new Stats();

    stats.addScores(score, thisPlayerID);
    if (botOpponentsNr < 0) {
      stats.setOpponentsName(opponent.name());
      botStats.add(stats);
    }
  }

  void printStats (Bot opponent) {

    botStats.get(getBotOpponentsNr(opponent)).printStats();
  }

  private List<String> getBotReports() {

    return
      botStats.stream()
        .map(Stats::getBotReport)
        .collect(Collectors.toList());
  }

  public String getStatReport () {

    return
      getBotReports().stream()
        .reduce(String::concat)
        .orElse("No data");
  }

}