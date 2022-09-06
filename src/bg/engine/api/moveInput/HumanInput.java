package bg.engine.api.moveInput;

import bg.engine.api.EngineApi;

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

  private MoveSelection moveSelection() {

    return
      humanMove()
        .getMoveSelection();
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
        ? moveSelection().getPlayerID()
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
      && !moveSelection().endOfInput()
      && moveSelection().positionIsEndingPoint();
  }

  @Override
  public Stream<Integer> getEndingPoints() {

    return
      humanInputActive()
        ? moveSelection().validEndingPoints()
        : null;
  }

}
