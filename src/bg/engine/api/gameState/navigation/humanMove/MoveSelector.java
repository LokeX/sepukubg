package bg.engine.api.gameState.navigation.humanMove;

import bg.engine.match.Turn;
import bg.engine.match.moves.Layout;
import bg.engine.match.moves.MoveLayout;
import bg.engine.match.moves.Moves;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveSelector extends Moves {

  int[] movePoints;
  private int inputPoint;
  private int lastInputPosition;

  MoveSelector(MoveSelector moveSelector) {

    super(moveSelector);
    this.movePoints = moveSelector.movePoints.clone();
  }

  public MoveSelector(Turn turn) {

    super(turn);
    resetMovePoints();
  }

  private void resetMovePoints () {

    movePoints = IntStream
      .range(0, getDice().length*2)
      .map(i -> -1)
      .toArray();
  }

  boolean endOfInput () {

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

  private Stream<MoveLayout> matchingMoves () {

    return
      getLegalMoves()
        .stream()
        .filter(move ->
          move.movePointsMatch(movePoints)
        );
  }

  private Stream<Integer> pointsIn (int position) {

    return
      matchingMoves()
        .map(MoveLayout::getMovePoints)
        .map(movePoint -> movePoint[position])
        .distinct()
        .filter(point -> point != -1);
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

  private boolean endingPointIsInPosition (int endingPoint, int position) {

    return
      endingPointsIn(position)
        .anyMatch(point -> point == endingPoint);
  }

  Stream<Integer> validEndingPoints () {

    return
      new MoveProjection(this)
        .projectedEndingPoints();
  }

  private boolean isEndingPoint (int position) {

    return position%2 == 1;
  }

  private int endingPointPosition (int endingPoint) {

    return
      range(position(), movePoints.length)
        .filter(this::isEndingPoint)
        .filter(position -> endingPointIsInPosition(endingPoint, position))
        .findAny()
        .orElse(-1);
  }

  boolean positionIsEndingPoint () {

    return isEndingPoint(position());
  }

  private boolean isLegalEndingPoint (int endingPoint) {

    return
      positionIsEndingPoint()
        && validEndingPoints()
        .anyMatch(point -> point == endingPoint);
  }

  private boolean isStartingPoint (int position) {

    return position%2 == 0;
  }

  private boolean uniquePointIn (int position) {

    return
      position < movePoints.length
      && pointsIn(position).count() == 1;
  }

  MoveLayout getMatchingMoveLayout() {

    return
      position() == 0
        ? getParentMoveLayout()
        : moveLayouts()
          .collect(Collectors.toList())
          .get(position()-1);
  }

  private Stream<MoveLayout> moveLayouts() {

    return
      matchingMoves()
        .findAny()
        .get()
        .getMoveLayoutsNew()
        .stream();
  }

  List<Layout> getMoveLayouts() {

    List<Layout> layouts = moveLayouts()
      .collect(Collectors.toList());
    int start =
      lastInputPosition > position()
        ? position() - 1
        : lastInputPosition;
    int end =
      lastInputPosition < position()
        ? position() - 1
        : lastInputPosition;

    lastInputPosition = position();
    return layouts.subList(start, end);
  }

  public boolean hasMoveLayouts () {

    return  lastInputPosition != position();
  }

  private boolean inputPointIsLegalStartingPoint (int position) {

    return
      isStartingPoint(position)
      && pointsIn(position).anyMatch(point -> point == inputPoint);
  }

  private void autoSelect (int max) {

    if (position() < max && uniquePointIn(position())) {
      movePoints[position()] =
        pointsIn(position())
          .findFirst()
          .orElse(-1);
      autoSelect(max);
    }
  }

  void initialSelection() {

    if (position() == 0) {
      autoSelect(movePoints.length);

      int storedPosition = position();

      resetMovePoints();
      autoSelect(storedPosition - (storedPosition%2));
    }
  }

  boolean isUniqueMove () {

    return
      matchingMoves().count() == 1;
  }

  private MoveLayout uniqueMove () {

    return
      isUniqueMove()
        ? matchingMoves().findAny().get()
        : null;
  }

  private void setAnyUniqueMove () {

    if (isUniqueMove()) {
      setEvaluatedMove(uniqueMove());
      movePoints = uniqueMove().getMovePoints();
    }
  }

  public int getUniqueEvaluatedMoveNr () {

    return getMoveNr(uniqueMove());
  }

  private void setEndingPoint () {

    int endingPointPosition = endingPointPosition(inputPoint);

    System.out.println("endingPointPosition: "+endingPointPosition);
    if (endingPointPosition > position()) {
      System.out.println("Projecting movePoints");
      movePoints =
        new MoveProjection(this)
        .projectMovePointsTo(endingPointPosition);
      lastInputPosition = position()-1;
    } else {
      movePoints[endingPointPosition] = inputPoint;
    }
  }

  private void setInputPoint () {

    if (inputPointIsLegalStartingPoint(position())) {
      movePoints[position()] = inputPoint;
    } else if (isLegalEndingPoint(inputPoint)) {
      setEndingPoint();
    }
  }

  void deleteLatestInput () {

    int inputPointNr = position();

    if (inputPointNr > 0) {
      movePoints[inputPointNr-1] = -1;
    }
  }

  private boolean moveIsLegal () {

    return !isIllegal();
  }

  boolean inputIsLegal () {

    return position() != lastInputPosition;
  }

  public void input (int inputPoint) {

    if (!endOfInput() && moveIsLegal()) {
      this.inputPoint = inputPoint;
      lastInputPosition = position();
      printReport();
      setInputPoint();
      setAnyUniqueMove();
      printReport();
    }
  }

  private void printMovePoints () {

    System.out.print("movePoints: ");
    for (int a : movePoints) {
      System.out.print(a+",");
    }
    System.out.println();
  }

  private void printReport () {

    printMovePoints();
    System.out.println("inputPoint: " + inputPoint);
    System.out.println("position: " + position());
    if (positionIsEndingPoint()) {
      System.out.println(
        "endingPointsInPosition: "
          + streamAsList(endingPointsIn(position()))
      );
      System.out.println(
        "projectedEndingPoints: "
          + streamAsList(validEndingPoints())
      );
    }
    if (isUniqueMove()) {
      System.out.print("uniqueMove: ");
      printMovePoints();
    }
  }

}
