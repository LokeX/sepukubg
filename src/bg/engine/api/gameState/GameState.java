package bg.engine.api.gameState;

import bg.engine.api.gameState.navigation.Navigation;
import bg.engine.match.moves.Layout;

public class GameState extends Navigation {

  private Search search;
  private GameData gameData;

  public GameState (Layout matchLayout) {

    super(matchLayout);
    search = new Search(this);
    gameData = new GameData(this);
  }

  public Search getSearch() {

    return search;
  }

  public GameData getGameData () {

    return gameData.getGameData();
  }

  public boolean playedMoveSelected() {

    return
      selectedMoveNr == playedMoveNr();
  }

  public boolean humanTurnSelected () {

    return
      isHumanTurn(
        selectedTurn()
      );
  }

  public boolean lastTurnSelected() {

    return
      selectedTurnNr == lastTurnNr();
  }

  private boolean playerIsHuman () {

    return
      isHumanTurn(selectedTurn());
  }

  private boolean moveIsLegal () {

    return
      !selectedMove().isIllegal();
  }

  public void moveNew () {

    if (moveIsLegal() && playerIsHuman()) {
      startHumanMove();
    } else {
      startComputerMove();
    }
  }

  public void newTurn () {

    if (nrOfTurns() == 0) {
      nextTurn(0, 0);
    } else {
      selectedTurn()
        .setPlayedMoveNr(
          selectedMoveNr
        );
      nextTurn(
        selectedTurnNr,
        selectedMoveNr
      );
    }
    selectedTurnNr = lastTurnNr();
    selectedMoveNr = 0;
  }

}
