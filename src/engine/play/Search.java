package engine.play;

import engine.api.Settings;
import engine.core.Turn;
import inUrFace.windows.TextDisplay;

public class Search {

  private PlayMatch playMatch;
  private boolean searching = false;

  public Search (PlayMatch playMatch) {

    this.playMatch = playMatch;
  }

  public boolean isSearching() {

    return searching;
  }

  private GameState gameState () {

    return
      playMatch.getGameState();

  }
  
  private Settings settings () {
    
    return playMatch.settings();
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
      playMatch.gameIsPlaying()
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
