package bg.engine.api.gameState;

import bg.engine.match.moves.Layout;

import java.util.ArrayList;

public class GameState extends Navigation {

  private Search search;
  private GameData gameData;

  public GameState (Layout matchLayout) {

    super(matchLayout);
    search = new Search(this);
    gameData = new GameData();
  }

  public Search getSearch() {

    return search;
  }

  public GameData getGameData () {

    return gameData.getGameData(this);
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

  private void computerMove () {

    setOutputLayouts(
      new ArrayList<>(
        selectedMove()
          .getMoveLayoutsNew())
    );
  }

  private boolean playerIsHuman () {

    return
      isHumanTurn(selectedTurn());
  }

  void moveNew () {

    if (playerIsHuman()) {
      startHumanMove();
    } else {
      computerMove();
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
