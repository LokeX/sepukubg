package bg.engine.api.moveInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static bg.util.StreamsUtil.streamAsList;

public class MoveProjection extends MoveSelection {

  private List<Integer> endingPoints = new ArrayList<>();
  private List<Integer> validEndingPoints = new ArrayList<>();
  private int terminalPosition;

  MoveProjection(MoveSelection moveSelector) {

    super(moveSelector);
  }

  public void projectMovePoints() {

    if (positionIsEndingPoint()) {
      endingPoints = streamAsList(endingPointsIn(position()));
      validEndingPoints.addAll(endingPoints);
    }
    if (endingPoints.size() > 0 && position() <= terminalPosition && !endOfInput()) {
      movePoints[position()] = endingPoints.get(0);
      projectMovePoints();
    }
  }

  public Stream<Integer> projectedEndingPoints  () {

    terminalPosition = movePoints.length;
    projectMovePoints();

    return
      validEndingPoints
        .stream()
        .distinct();
  }

  public int[] projectMovePointsTo (int terminalPosition) {

    this.terminalPosition = terminalPosition;
    projectMovePoints();

    return movePoints;
  }

}