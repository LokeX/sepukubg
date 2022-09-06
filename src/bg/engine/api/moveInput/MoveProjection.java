package bg.engine.api.moveInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static bg.util.StreamsUtil.streamAsList;

public class MoveProjection extends MoveSelection {

  MoveProjection(MoveSelection moveSelector) {

    super(moveSelector);
  }

  private void projectMovePoints (
    int terminalPosition,
    List<Integer> endingPoints,
    List<Integer> validEndingPoints) {

    if (positionIsEndingPoint()) {
      endingPoints = streamAsList(endingPointsIn(position()));
      validEndingPoints.addAll(endingPoints);
    }
    if (endingPoints.size() > 0 && position() <= terminalPosition && !endOfInput()) {
      movePoints[position()] = endingPoints.get(0);
      projectMovePoints(terminalPosition, endingPoints, validEndingPoints);
    }
  }

  int[] projectMovePointsTo (int terminalPosition) {

    System.out.println("Projecting movePoints to position: "+terminalPosition);
    projectMovePoints(
      terminalPosition,
      new ArrayList<>(),
      new ArrayList<>()
    );
    return movePoints;
  }

  Stream<Integer> projectedEndingPoints () {

    List<Integer> projectedEndingPoints = new ArrayList<>();

    if (positionIsEndingPoint()) {
      projectMovePoints(
        movePoints.length,
        new ArrayList<>(),
        projectedEndingPoints
      );
    }
    return projectedEndingPoints
      .stream()
      .distinct();
  }

}