package bg.engine.trainer;

import bg.engine.Match;
import bg.engine.moves.Layout;

import java.util.ArrayList;
import java.util.List;

import static bg.Main.*;
import static bg.util.Dialogs.getIntegerInput;
import static bg.util.ThreadUtil.threadSleep;
import static bg.util.time.Time.getConvertedTime;

public class Trainer {

  static public List<Bot> bots = new ArrayList<>();
  static public int whiteBot = 0;
  static public int blackBot = 0;
  static public int statScoreToWin = 7;
  static public boolean killRun = false;
  static public boolean running = false;
  static public int selectedScenarioNr = 0;

  static public int nrOfMatchesToPlay;
  static public int nrOfMatchesPlayed;
  static public long startTime;

  static public boolean isBotMember (String name) {

    return bots.stream().map(Bot::getName).
      anyMatch(botName -> botName.equals(name));
  }

  private BotStats[] botStats = new BotStats[2];

  public Trainer() {

    if (bots.isEmpty()) {
      bots.add(new Bot("Default bot"));
      blackBot = 0;
      whiteBot = 0;
      settings.setBlackBotOpponent(0);
      settings.setWhiteBotOpponent(0);
    } else {
      blackBot = settings.getTrainerBlackBot();
      whiteBot = settings.getTrainerWhiteBot();
    }
  }

  public void printReport () {

    System.out.println();
    System.out.println("Statistical report");
    System.out.println("Playing to scoreBoard: " + statScoreToWin);
    System.out.println("Matches played: " + nrOfMatchesPlayed + " / " + nrOfMatchesToPlay);
    System.out.println();
    bots.get(whiteBot).getStats().printStats(bots.get(blackBot));
    bots.get(blackBot).getStats().printStats(bots.get(whiteBot));

    long timeSpend = System.currentTimeMillis() - startTime;

    System.out.println("Time in seconds: " + (timeSpend / 1000));
    if (nrOfMatchesPlayed < nrOfMatchesToPlay) {
      System.out.println("Estimated time remaining: " + getTimeRemaining());
    }
  }

  public Bot getWhiteBot () {

    return bots.get(whiteBot);
  }

  public Bot getBlackBot () {

    return bots.get(blackBot);
  }

  public String getUsedScenarioTitle () {

    return scenarios.getScenarios().get(selectedScenarioNr).getName();
  }

  public String getFinalReport () {

    return
      "Statistical report"+
      "\nUsed layout: "+getUsedScenarioTitle()+
      "\nPlaying to scoreBoard: " + statScoreToWin+
      "\nMatches played: " + nrOfMatchesPlayed + " / " + nrOfMatchesToPlay+
      "\n\n"+
      getWhiteBot().getStats().getBotReport(getBlackBot())+
      getBlackBot().getStats().getBotReport(getWhiteBot())+
      "Time spend: " + getTimeSpend();
  }

  public void setSelectedScenarioNr(int scenarioNr) {

    selectedScenarioNr = scenarioNr;
  }

  public int getSelectedScenarioNr() {

    return selectedScenarioNr;
  }

  private int getNrOfCores () {

    int cores = Runtime.getRuntime().availableProcessors() - 1;

    return cores == 0 ? ++cores : cores;
  }

  public String getTimeSpend () {

    long spendTime = System.currentTimeMillis() - startTime;

    return getConvertedTime(spendTime);
  }

  public String getTimeRemaining () {

    long timeSpend = System.currentTimeMillis() - startTime;
    float timePerMatch = (float) timeSpend / (float) nrOfMatchesPlayed;
    long estimatedTimeRemaining = (long) (timePerMatch * (nrOfMatchesToPlay - nrOfMatchesPlayed));

    return getConvertedTime(estimatedTimeRemaining);
  }

  public String getInitialReport () {

    return
      "Using scenario: "+scenarios.getScenarios().get(selectedScenarioNr).getName()+
      "\nPlaying to matchApi-scoreBoard: "+statScoreToWin+
      "\nUsing Nr of cores: "+getNrOfCores();
  }

  private void printInitialReport () {

    System.out.println("Running "+nrOfMatchesToPlay+" matches");
    System.out.println("Playing to scoreBoard: "+statScoreToWin);
    System.out.println("Using scenario: "+scenarios.getScenarios().get(selectedScenarioNr).getName());
    System.out.println("Using Nr of cores: "+getNrOfCores());
  }

  public void playMatches (int nrOfMatches) {

    startTime = System.currentTimeMillis();
    nrOfMatchesToPlay = nrOfMatches;
    nrOfMatchesPlayed = 0;
    running = true;
    killRun = false;

    int nrOfCores = getNrOfCores();
    int matchesPerCore = nrOfMatches/nrOfCores;

    for (int a = 0; a < nrOfCores; a++) {
      if (a == nrOfCores-1) {
        playMatchesThread(
          matchesPerCore+
          (nrOfMatches-(matchesPerCore*nrOfCores)),
          true
        );
      } else {
        playMatchesThread(matchesPerCore, false);
      }
    }
    printInitialReport();
  }

  public void playMatchesThread(int nrOfMatches, boolean lastThread) {

    new Thread(() -> {

      Match match;
      Layout layout = scenarios.getLayoutByNr(selectedScenarioNr);

      for (int a = 0; a < nrOfMatches; a++) {
        match = new Match(layout, statScoreToWin).getMatch();
        bots.get(whiteBot).getStats().addScore(match.getScore(), 0, bots.get(blackBot));
        bots.get(blackBot).getStats().addScore(match.getScore(), 1, bots.get(whiteBot));
        nrOfMatchesPlayed++;
        if (killRun) {
          System.out.println("Statistical run terminated");
          break;
        }
      }
      if (lastThread) {
        while (nrOfMatchesPlayed < nrOfMatchesToPlay) {
          threadSleep(100);
        }
        printReport();
        running = false;
        System.out.println("Last thread signing out");
      }
    }).start();
  }

}