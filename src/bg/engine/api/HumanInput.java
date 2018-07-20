package bg.engine.api;

import bg.engine.api.gameState.navigation.humanMove.HumanMove;
import bg.engine.api.gameState.navigation.humanMove.MoveSelector;

import java.util.stream.Stream;

public class HumanInput {

  private EngineApi engineApi;

  public HumanInput getHumanInput (EngineApi engineApi) {

    this.engineApi = engineApi;
    return this;
  }

  private HumanMove humanMove () {

    return
      engineApi
        .getMatchState()
        .getHumanMove();
  }

  private MoveSelector moveSelector () {

    return
      humanMove()
        .getMoveSelector();
  }

  public void pointClicked (int clickedPoint) {

    if (humanInputActive()) {
      humanMove().pointClicked(clickedPoint);
    }
  }

  public int getPlayerID () {

    return
      humanInputActive()
        ? moveSelector().getPlayerID()
        : -1;
  }

  public boolean humanInputActive () {

    return
      engineApi.matchIsPlaying()
      && engineApi.getMatchState().gameIsPlaying()
      && humanMove().inputReady();
  }

  public boolean isEndingPoint () {

    return
      humanInputActive()
      && moveSelector()
        .positionIsEndingPoint();
  }

  public Stream<Integer> getEndingPoints () {

    return
      humanInputActive()
        ? moveSelector().validEndingPoints()
        : null;
  }

}
