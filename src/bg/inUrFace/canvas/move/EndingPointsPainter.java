package bg.inUrFace.canvas.move;

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
        && engineApi.getHumanInput() != null
        && engineApi.getHumanInput().humanInputActive();
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
      engineApi
        .getHumanInput()
        .getEndingPoints()
        .map(endingPoint -> clickPoints[endingPoint]);
  }

  private boolean gotEndingPoints () {

    return
      humanMoveReady()
      && engineApi
        .getHumanInput()
        .endingPointIsNext();
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
