package bg.engine.api.gamePlay;

import bg.engine.coreLogic.Turn;
import bg.inUrFace.windows.TextDisplay;

import static bg.Main.settings;

public class Search {

  private GameState gameState;
  private boolean searching = false;

  public Search (GameState gameState) {

    this.gameState = gameState;
  }

  public boolean isSearching() {

    return searching;
  }

  private void displaySearchReport () {

    TextDisplay.displayReport(
      "Search report",
      "Turn nr: "+(gameState.lastTurnNr()+1)+"\n"+
        gameState.lastTurn().getSearchEvaluation().getReport()
    );
  }

  private void searchTurn (Turn turn) {

    turn.generateSearchEvaluations(
      settings.getNrOfMovesToSearch(),
      settings.getSearchToPly()
    ).sortMovesBySearchEvaluation();
  }

  private boolean playerLooksAhead () {

    return
      settings.getLookaheadForAllPlayers()
      || !gameState.humanTurnSelected();
  }

  private boolean okToSearch () {

    return
      !settings.searchIsOff()
      && playerLooksAhead();
  }

  public void searchRolledMoves () {

    if (okToSearch()) {
      searching = true;
      searchTurn(gameState.lastTurn());
      searching = false;
      if (settings.isSearchReportOn()) {
        displaySearchReport();
      }
    }
  }

}
