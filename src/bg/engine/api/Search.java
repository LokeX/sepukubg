package bg.engine.api;

import bg.engine.api.gamePlay.GameState;
import bg.engine.match.Turn;
import bg.inUrFace.windows.TextDisplay;

import static bg.Main.settings;

public class Search {

  private GameState gameState;

  public Search (GameState gameState) {

    this.gameState = gameState;
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
      searchTurn(gameState.lastTurn());
      if (settings.isSearchReportOn()) {
        displaySearchReport();
      }
    }
  }

}
