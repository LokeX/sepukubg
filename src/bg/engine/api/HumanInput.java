package bg.engine.api;

import bg.engine.api.gamePlay.navigation.humanMove.HumanMove;
import bg.engine.api.gamePlay.navigation.humanMove.MoveSelector;

import java.util.stream.Stream;

public class HumanInput implements HumanInputAPI {

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

  @Override
  public void pointClicked(int clickedPoint) {

    if (humanInputActive()) {
      humanMove().pointClicked(clickedPoint);
    }
  }

  @Override
  public int getPlayerID() {

    return
      humanInputActive()
        ? moveSelector().getPlayerID()
        : -1;
  }

  @Override
  public boolean humanInputActive() {

    return
      engineApi.gameIsPlaying()
      && humanMove().inputReady();
  }

  @Override
  public boolean endingPointIsNext() {

    return
      humanInputActive()
      && !moveSelector().endOfInput()
      && moveSelector().positionIsEndingPoint();
  }

  @Override
  public Stream<Integer> getEndingPoints() {

    return
      humanInputActive()
        ? moveSelector().validEndingPoints()
        : null;
  }

}
