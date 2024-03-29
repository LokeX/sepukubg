package engine.core.trainer;

import engine.core.Match;
import engine.core.moves.Layout;

import java.util.ArrayList;
import java.util.List;

import static sepuku.WinApp.*;
import static util.ThreadUtil.threadSleep;
import static util.time.Time.getConvertedTime;
import static java.util.Arrays.stream;

public class Trainer {

  static public List<Bot> bots = new ArrayList<>();
  static public int whiteBot = 0;
  static public int blackBot = 0;
  static public int statScoreToWin = 7;
  static public boolean killRun = false;
  static public boolean running = false;
  static public int selectedScenarioNr = 0;

  static public int nrOfMatchesToPlay;
  static public long startTime;

  static public int[] matchesPlayedByThreads = new int[getNrOfCores()];
  static public List<List<Match>> threadMatches = new ArrayList<>();

  static public boolean isBotMember (String name) {

    return bots.stream().map(Bot::name).
      anyMatch(botName -> botName.equals(name));
  }

  private BotStats[] botStats = new BotStats[2];

  public Trainer() {

    if (bots.isEmpty()) {
      bots.add(new Bot("Default bot"));
      blackBot = 0;
      whiteBot = 0;
      sepukuPlay.settings().setBlackBotOpponent(0);
      sepukuPlay.settings().setWhiteBotOpponent(0);
    } else {
      blackBot = sepukuPlay.settings().getTrainerBlackBot();
      whiteBot = sepukuPlay.settings().getTrainerWhiteBot();
    }
  }

  static public int getNrOfMatchesPlayed () {

    return stream(matchesPlayedByThreads).sum();
  }

  public void printReport () {

    System.out.println();
    System.out.println("Statistical report");
    System.out.println("Playing to scoreBoard: " + statScoreToWin);
    System.out.println("Matches played: " + getNrOfMatchesPlayed() + " / " + nrOfMatchesToPlay);
    System.out.println();
    bots.get(whiteBot).getStats().printStats(bots.get(blackBot));
    bots.get(blackBot).getStats().printStats(bots.get(whiteBot));

    long timeSpend = System.currentTimeMillis() - startTime;

    System.out.println("Time in seconds: " + (timeSpend / 1000));
    if (getNrOfMatchesPlayed() < nrOfMatchesToPlay) {
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

    return sepukuPlay.scenarios().getScenarios().get(selectedScenarioNr).getName();
  }

  public String getFinalReport () {

    return
      "Statistical report"+
      "\nUsed layout: "+getUsedScenarioTitle()+
      "\nPlaying to scoreBoard: " + statScoreToWin+
      "\nMatches played: " + getNrOfMatchesPlayed() + " / " + nrOfMatchesToPlay+
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

  private static int getNrOfCores () {

    int cores = Runtime.getRuntime().availableProcessors() - 1;

    return cores == 0 ? ++cores : cores;
  }

  public String getTimeSpend () {

    long spendTime = System.currentTimeMillis() - startTime;

    return getConvertedTime(spendTime);
  }

  public String getTimeRemaining () {

    long timeSpend = System.currentTimeMillis() - startTime;
    float timePerMatch = (float) timeSpend / (float) getNrOfMatchesPlayed();
    long estimatedTimeRemaining = (long) (timePerMatch * (nrOfMatchesToPlay - getNrOfMatchesPlayed()));

    return getConvertedTime(estimatedTimeRemaining);
  }

  public String getInitialReport () {

    return
      "Using scenario: "+ sepukuPlay.scenarios().getScenarios().get(selectedScenarioNr).getName()+
      "\nPlaying to matchScore: "+statScoreToWin+
      "\nUsing Nr of cores: "+getNrOfCores();
  }

  private void printInitialReport () {

    System.out.println("Running "+nrOfMatchesToPlay+" matches");
    System.out.println("Playing to matchScore: "+statScoreToWin);
    System.out.println("Using scenario: "+ sepukuPlay.scenarios().getScenarios().get(selectedScenarioNr).getName());
    System.out.println("Using Nr of cores: "+getNrOfCores());
  }

  public void playMatches (int nrOfMatches) {

    startTime = System.currentTimeMillis();
    nrOfMatchesToPlay = nrOfMatches;
    matchesPlayedByThreads = new int[getNrOfCores()];
    running = true;
    killRun = false;

    int nrOfCores = getNrOfCores();
    int matchesPerCore = nrOfMatches/nrOfCores;
    int deviation = nrOfMatches-(matchesPerCore*nrOfCores);
    int lastThreadNOM = matchesPerCore+deviation;

    System.out.println("Deviation: "+deviation);
    System.out.println("Starting threads: "+nrOfCores);
    System.out.println("Matches pr thread: "+matchesPerCore);
    System.out.println("Matches last thread: "+lastThreadNOM);

    for (int threadNr = 0; threadNr < nrOfCores; threadNr++) {
      if (threadNr < nrOfCores-1) {
        playMatchesThread(
          matchesPerCore,
          threadNr,
          false
        );
      } else {
        playMatchesThread(
          lastThreadNOM,
          threadNr,
          true
        );
      }
    }
    printInitialReport();
  }
  
  private void addMatchStatsToBotStats (Match match) {
  
    bots.get(whiteBot)
      .getStats()
      .addScore(
        match.getMatchScore(),
        0,
        bots.get(blackBot)
      );
    bots.get(blackBot)
      .getStats()
      .addScore(
        match.getMatchScore(),
        1,
        bots.get(whiteBot)
      );
  }
  
  public void playMatchesThread(int nrOfMatches, int threadNr, boolean lastThread) {

    System.out.println("Starting thread: "+threadNr+" [matches: "+nrOfMatches+"]");

    new Thread(() -> {

      Layout layout = sepukuPlay.scenarios().getLayoutByNr(selectedScenarioNr);
      List<Match> matches = new ArrayList<>();

      for (int a = 0; a < nrOfMatches; a++) {
        matches.add(new Match(layout, statScoreToWin).getMatch());
        matchesPlayedByThreads[threadNr]++;
        if (killRun) {
          running = false;
          System.out.println("Statistical run terminated");
          break;
        }
      }
      threadMatches.add(matches);
      if (lastThread) {
        while (getNrOfMatchesPlayed() < nrOfMatchesToPlay) {
          threadSleep(100);
        }
        threadMatches.forEach(
          matchList -> matchList.forEach(
            this::addMatchStatsToBotStats
          )
        );
        printReport();
        running = false;
        System.out.println("Last thread signing out");
      } else {
        System.out.println("Thread signing out: "+threadNr);
      }
    }).start();
  }

}