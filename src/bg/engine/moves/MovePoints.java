package bg.engine.moves;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;

public class MovePoints extends Moves {

  int[] movePoints;

  MovePoints(MovePoints movePoints) {

    super(movePoints);
    this.movePoints = movePoints.movePoints.clone();
  }

  MovePoints(Moves moves) {

    super(moves);
    movePoints = new int[getDice().length*2];
    for (int pointNr = 0; pointNr < movePoints.length; pointNr++) {
      movePoints[pointNr] = -1;
    }
  }

  public Layout getMatchingLayout () {

    return matchingMoves()
      .map(MoveLayout::getMovePointLayouts)
      .map(layouts -> layouts.get(position()))
      .findFirst()
      .orElse(getParentLayout());
  }

  public boolean endOfInput () {

    return
      position() == movePoints.length
        || position()/2 == getNrOfLegalPartMoves();
  }

  int position () {

    return (int)
      Arrays.stream(movePoints)
        .filter(point -> point != -1)
        .count();
  }

  public boolean positionIsEndingPoint () {

    return isEndingPoint(position());
  }

  public int[] getMovePoints () {

    return movePoints.clone();
  }

  Stream<MoveLayout> matchingMoves () {

    return
      getLegalMoves()
        .stream()
        .filter(move ->
          move.movePointsMatch(movePoints)
        );
  }

  Stream<Integer> pointsIn (int position) {

    return
      matchingMoves()
        .map(MoveLayout::getMovePoints)
        .map(movePoint -> movePoint[position])
        .filter(point -> point != -1)
        .distinct();
  }

  private boolean isAmbiguous (int endingPoint) {

    return
      matchingMoves()
        .anyMatch(move ->
          move.endingPointIsAmbiguous(endingPoint)
        );
  }

  Stream<Integer> endingPointsIn (int position) {

    return position != 3
      ? pointsIn(position)
      : pointsIn(position)
        .filter(endingPoint ->
          !isAmbiguous(endingPoint)
        );
  }

  int endingPointPosition (int endingPoint) {

    return IntStream
      .range(1, movePoints.length)
      .filter(position -> position%2 == 1)
      .filter(position -> endingPointIsInPosition(endingPoint, position))
      .findAny()
      .orElse(-1);
  }

  boolean isStartingPoint (int position) {

    return position%2 == 0;
  }

  private boolean endingPointIsInPosition (int endingPoint, int position) {

    return
      endingPointsIn(position)
        .anyMatch(point -> point == endingPoint);
  }

  public List<Integer> getValidEndingPoints () {

    return streamAsList(validEndingPoints());
  }

  Stream<Integer> validEndingPoints () {

    return new
      MoveProjection(this)
      .projectedEndingPoints();
  }

  private boolean isEndingPoint (int position) {

    return position%2 == 1;
  }

  boolean uniquePointIn (int position) {

    return
      position < movePoints.length
        && pointsIn(position).count() == 1;
  }


}
