package bg.api;

import bg.engine.Turn;
import bg.inUrFace.windows.TextDisplay;

import static bg.Main.settings;

class Search {

  private EngineApi engineApi;

  Search (EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  private void displaySearchReport () {

    TextDisplay.displayReport(
      "Search report",
      "Turn nr: "+(engineApi.getLatestTurnNr()+1)+"\n"+
        engineApi.getLatestTurn().getSearchEvaluation().getReport()
    );
  }

  private void searchTurn (Turn turn) {

    turn.generateSearchEvaluations(
      settings.getNrOfMovesToSearch(),
      settings.getSearchToPly()
    ).sortMovesBySearchEvaluation();
  }

  private boolean okToSearch () {

    return !settings.searchIsOff() && (
      settings.getLookaheadForAllPlayers() ||
        !engineApi.turnsPlayerIsHuman(engineApi.getLatestTurn())
    );
  }

  void searchRolledMoves () {

    if (okToSearch()) {
      searchTurn(engineApi.getLatestTurn());
      if (settings.isSearchReportOn()) {
        displaySearchReport();
      }
    }
  }

}
