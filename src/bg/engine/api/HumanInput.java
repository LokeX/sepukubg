package bg.engine.api;

import bg.engine.api.gameState.navigation.humanMove.HumanMove;

import java.util.stream.Stream;

public class HumanInput {

  private EngineApi engineApi;

  HumanInput (EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  private HumanMove humanMove () {

    return
      engineApi
        .getMatchState()
        .getHumanMove();
  }

  public void pointClicked (int clickedPoint) {

    if (inputReady()) {
      humanMove().pointClicked(clickedPoint);
    }
  }

  public int getPlayerID () {

    return
      inputReady()
        ? humanMove().getPlayerID()
        : -1;
  }

  public boolean inputReady () {

    return
      engineApi.matchIsPlaying()
      && engineApi.getMatchState().gameIsPlaying()
      && humanMove().inputReady();
  }

  public boolean isEndingPoint () {

    return
      inputReady()
      && humanMove().isEndingPoint();
  }

  public Stream<Integer> getEndingPoints () {

    return
      inputReady()
        ? humanMove().getEndingPoints()
        : null;
  }

}
