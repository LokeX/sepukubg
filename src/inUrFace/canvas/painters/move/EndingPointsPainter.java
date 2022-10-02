package inUrFace.canvas.painters.move;

import inUrFace.canvas.painters.Paintable;
import util.Batch;

import java.awt.*;
import java.util.stream.Stream;

import static sepuku.App.playSepuku;
import static sepuku.App.mouse;

public class EndingPointsPainter implements Paintable {

  private boolean humanMoveReady () {

    return
      mouse != null
        && playSepuku.getHumanMove() != null
        && playSepuku.getHumanMove().humanInputActive();
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
      playSepuku
        .getHumanMove()
        .getEndingPoints()
        .map(endingPoint -> clickPoints[endingPoint]);
  }

  private boolean gotEndingPoints () {

    return
      humanMoveReady()
      && playSepuku
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
