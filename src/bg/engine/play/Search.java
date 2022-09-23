package bg.engine.play;

import bg.engine.api.Settings;
import bg.engine.core.Turn;
import bg.inUrFace.windows.TextDisplay;

public class Search {

  private MatchPlay matchPlay;
  private boolean searching = false;

  public Search (MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
  }

  public boolean isSearching() {

    return searching;
  }

  private GameState gameState () {

    return
      matchPlay.getGameState();

  }
  
  private Settings settings () {
    
    return matchPlay.settings();
  }

  private void displaySearchReport () {

    TextDisplay.displayReport(
      "Search report",
      "Turn nr: "+(gameState().lastTurnNr()+1)+"\n"+
        gameState().lastTurn().getSearchEvaluation().getReport()
    );
  }

  private void searchTurn (Turn turn) {

    turn.generateSearchEvaluations(
      settings().getNrOfMovesToSearch(),
      settings().getSearchToPly()
    ).sortMovesBySearchEvaluation();
  }

  private boolean playerLooksAhead () {

    return
      settings().getLookaheadForAllPlayers()
      || !gameState().humanTurnSelected();
  }

  private boolean okToSearch () {

    System.out.println("gameIsPlaying: "+matchPlay.gameIsPlaying());
    System.out.println("nrOfTurns: "+gameState().nrOfTurns());
    System.out.println("searchIsOff: "+settings().searchIsOff());
    System.out.println("playerLooksAhead: "+playerLooksAhead());

    return
      matchPlay.gameIsPlaying()
      && gameState().nrOfTurns() > 0
      && !settings().searchIsOff()
      && playerLooksAhead();
  }

  public void searchRolledMoves () {
    
    System.out.println();
    System.out.println("Search:");
    System.out.println("Search to ply: "+settings().getSearchToPly());
    if (okToSearch()) {
      System.out.println("okToSearch");
      searching = true;
      searchTurn(gameState().lastTurn());
      searching = false;
      if (settings().isSearchReportOn()) {
        displaySearchReport();
      }
    }
  }

}
