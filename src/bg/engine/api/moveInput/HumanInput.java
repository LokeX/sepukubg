package bg.engine.api.moveInput;

import bg.engine.api.EngineApi;

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
        .getMatchPlay()
        .getHumanMove();
  }

  private MoveSelection moveSelection() {

    return
      humanMove()
        .getMoveSelection();
  }

  public void pointClicked(int clickedPoint) {

    if (humanInputActive()) {
      humanMove().pointClicked(clickedPoint);
    }
  }

  public int getPlayerID() {

    return
      humanInputActive()
        ? moveSelection().getPlayerID()
        : -1;
  }

  public boolean humanInputActive() {

    return
      engineApi.gameIsPlaying()
      && humanMove().inputReady();
  }

  public boolean endingPointIsNext() {

    return
      humanInputActive()
      && !moveSelection().endOfInput()
      && moveSelection().positionIsEndingPoint();
  }

  public Stream<Integer> getEndingPoints() {

    return
      humanInputActive()
        ? moveSelection().validEndingPoints()
        : null;
  }

  public int[] getMovePoints () {

    return
      humanInputActive()
        ? moveSelection().getMovePoints()
        : null;
  }

}
