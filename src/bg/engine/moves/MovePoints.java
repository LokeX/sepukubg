package bg.engine.moves;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MovePoints extends Moves {

  int[] movePoints;

  MovePoints(MovePoints movePoints) {

    super(movePoints);
    this.movePoints = movePoints.movePoints.clone();
  }

  MovePoints(Moves moves) {

    super(moves);
    resetMovePoints();
  }

  public void resetMovePoints () {

    movePoints = new int[getDice().length*2];
    for (int pointNr = 0; pointNr < movePoints.length; pointNr++) {
      movePoints[pointNr] = -1;
    }
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

    return
      position != 3
        ? pointsIn(position)
        : pointsIn(position)
            .filter(endingPoint ->
              !isAmbiguous(endingPoint)
            );
  }

  int endingPointPosition (int endingPoint) {

    return range(1, movePoints.length)
      .filter(this::isEndingPoint)
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

    return
      new MoveProjection(this)
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

  MoveLayout matchingMoveLayout() {

//    System.out.println("moveLayouts size: "+moveLayouts().size());
//    moveLayouts().stream()
//      .map(MoveLayout::getMovePointsString)
//      .forEach(System.out::println);
    return
      position() == 0
        ? getParentMoveLayout()
        : moveLayouts().get(position()-1);
  }

  List<MoveLayout> moveLayouts() {

    return
      matchingMoves()
        .findAny()
        .get()
        .getMoveLayouts();
  }

  public List<MoveLayout> getMoveLayouts () {

    return moveLayouts();
  }

  public List<Layout> getMoveLayoutsAsLayouts () {

    return
      moveLayouts()
      .stream()
      .map(Layout::new)
      .collect(Collectors.toList());
  }

  public String getMovePointsString () {

    return
      matchingMoveLayout().getMovePointsString();
  }


}
