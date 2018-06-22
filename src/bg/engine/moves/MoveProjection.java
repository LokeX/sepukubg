package bg.engine.moves;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static bg.util.StreamsUtil.streamAsList;

public class MoveProjection extends MoveSelect {

  MoveProjection(MoveSelect moveSelect) {

    super(moveSelect);
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

    projectMovePoints(
      terminalPosition,
      new ArrayList<>(),
      new ArrayList<>()
    );
    return movePoints;
  }

  private Stream<Integer> streamProjectedEndingPoints () {

    List<Integer> projectedEndingPoints = new ArrayList<>();

    projectMovePoints(
      movePoints.length,
      new ArrayList<>(),
      projectedEndingPoints
    );
    return projectedEndingPoints.stream();
  }

  Stream<Integer> projectedEndingPoints () {

    Stream<Integer> validEndingPoints =
      new ArrayList<Integer>().stream();

    if (position()%2 == 1) {
      validEndingPoints = streamProjectedEndingPoints();
    }
    return validEndingPoints.distinct();
  }

}
