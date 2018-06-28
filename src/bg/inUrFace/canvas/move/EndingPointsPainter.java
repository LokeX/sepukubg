package bg.inUrFace.canvas.move;

import bg.engine.api.HumanMoveApi;
import bg.inUrFace.canvas.Paintable;
import bg.util.Batch;

import java.awt.*;
import java.util.stream.Stream;

import static bg.Main.engineApi;
import static bg.Main.mouse;

public class EndingPointsPainter implements Paintable {

  private boolean humanMoveReady () {

    return
      mouse != null
        && humanMove() != null
        && humanMove().inputReady();
  }

  private Batch[] clickPoints () {

    return
      mouse
        .getMoveInputListener()
        .getClickPoints();
  }

  private Stream<Batch> endingPoints () {

    Batch[] clickPoints = clickPoints();

    return
      humanMove()
        .getEndingPoints()
        .map(endingPoint -> clickPoints[endingPoint]);
  }

  private HumanMoveApi humanMove () {

    return engineApi.getHumanMove();
  }

  private boolean gotEndingPoints () {

    return
      humanMoveReady()
      && humanMove().isEndingPoint();
  }

  public void paint (Graphics graphics) {

    if (gotEndingPoints()) {
      endingPoints().forEach(
        endingPoint ->
          endingPoint.drawBatch(graphics)
      );
    }
  }

}
