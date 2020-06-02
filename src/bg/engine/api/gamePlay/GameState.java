package bg.engine.api.gamePlay;

import bg.engine.api.navigation.Navigation;
import bg.engine.coreLogic.moves.Layout;

public class GameState extends Navigation {

  private Search search;
  private GameInfo gameInfo;

  public GameState (Layout matchLayout) {

    super(matchLayout);
    search = new Search(this);
    gameInfo = new GameInfo(this);
  }

  public Search getSearch() {

    return search;
  }

  public GameInfo getGameInfo() {

    return gameInfo.getGameData();
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
