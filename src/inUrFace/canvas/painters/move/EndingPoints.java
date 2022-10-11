package inUrFace.canvas.painters.move;

import inUrFace.canvas.painters.Paintable;
import util.Batch;

import java.awt.*;
import java.util.stream.Stream;

import static sepuku.WinApp.*;

public class EndingPoints implements Paintable {

  private boolean humanMoveReady () {

    return
      win.mouse != null
        && sepukuPlay.humanMove() != null
        && sepukuPlay.humanMove().inputActive();
  }

  private Batch[] clickPoints () {

    return
      win.mouse
        .getMoveInputListener()
        .getClickPoints();
  }

  private Stream<Batch> endingPoints () {

    Batch[] clickPoints = clickPoints();

    return
      sepukuPlay
        .humanMove()
        .getEndingPoints()
        .map(endingPoint -> clickPoints[endingPoint]);
  }

  private boolean gotEndingPoints () {

    return
      humanMoveReady()
      && sepukuPlay
        .humanMove()
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