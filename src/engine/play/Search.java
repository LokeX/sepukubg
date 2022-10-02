package engine.play;

import engine.api.Settings;
import engine.core.Turn;
import inUrFace.windows.TextDisplay;

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

    return
      matchPlay.gameIsPlaying()
      && gameState().nrOfTurns() > 0
      && !settings().searchIsOff()
      && playerLooksAhead();
  }

  public void searchRolledMoves () {
    
    if (okToSearch()) {
      searching = true;
      searchTurn(gameState().lastTurn());
      searching = false;
      if (settings().isSearchReportOn()) {
        displaySearchReport();
      }
    }
  }

}
