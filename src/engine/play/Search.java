package engine.play;

import engine.api.Settings;
import engine.core.Turn;
import engine.play.game.GameState;

import java.util.List;

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
      matchPlay.gameState();

  }
  
  private Settings settings () {
    
    return matchPlay.settings();
  }

  private void searchTurn (Turn turn) {

    turn.generateSearchEvaluations(
      settings().getNrOfMovesToSearch(),
      settings().getSearchToPly()
    ).sortMovesBySearchEvaluation();
  }
  
  public List<String> getSearchReport () {
    
    return
      gameState()
      .lastTurn()
      .getSearchEvaluation()
      .getSearchReport();
  }

  private boolean playerLooksAhead () {

    return
      settings().getLookaheadForAllPlayers()
      || !gameState().humanTurnSelected();
  }

  public boolean okToSearch () {

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
//      if (settings().isSearchReportOn()) {
//        displaySearchReport();
//      }
    }
  }

}
