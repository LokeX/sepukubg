package bg.inUrFace.canvas.move;

import bg.inUrFace.canvas.Paintable;
import bg.util.Batch;

import java.awt.*;
import java.util.stream.Stream;

import static bg.Main.sepuku;
import static bg.Main.mouse;

public class EndingPointsPainter implements Paintable {

  private boolean humanMoveReady () {

    return
      mouse != null
        && sepuku.getHumanMove() != null
        && sepuku.getHumanMove().humanInputActive();
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
      sepuku
        .getHumanMove()
        .getEndingPoints()
        .map(endingPoint -> clickPoints[endingPoint]);
  }

  private boolean gotEndingPoints () {

    return
      humanMoveReady()
      && sepuku
        .getHumanMove()
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
