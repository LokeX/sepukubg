package bg.engine.api.moveInput;

import bg.engine.coreLogic.Turn;
import bg.engine.coreLogic.moves.MoveLayout;
import bg.engine.coreLogic.moves.Moves;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveSelection extends Moves {

  public int[] movePoints;
  private int inputPoint;
  private int lastRecordedInputPosition;

  protected MoveSelection(MoveSelection moveSelector) {

    super(moveSelector);
    this.movePoints = moveSelector.movePoints.clone();
  }

  public MoveSelection(Turn turn) {

    super(turn);
    resetMovePoints();
  }

  private void resetMovePoints () {

    movePoints = new int[getDice().length*2];
    Arrays.fill(movePoints, -1);
  }

  public boolean endOfInput () {

    return
      position() == movePoints.length
      || position() == getNrOfLegalPartMoves()*2;
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

  public Stream<Integer> validEndingPoints () {

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

  public boolean positionIsEndingPoint () {

//    System.out.println("isEndingPoint = "+isEndingPoint(position()));
//    System.out.println("position = "+position());
    return isEndingPoint(position());
  }

  private boolean isLegalEndingPoint (int endingPoint) {

    return
      positionIsEndingPoint()
        && validEndingPoints()
        .anyMatch(point -> point == endingPoint);
  }

  private boolean isStartingPoint (int position) {

    return
      !endOfInput()
      && position%2 == 0;
  }

  private boolean uniquePointIn (int position) {

    return
      position < movePoints.length
      && pointsIn(position).count() == 1;
  }

  public MoveLayout getMatchingMoveLayout() {

    return
      position() == 0
        ? getParentMoveLayout()
        : movePointLayouts()
          .toList()
          .get(position()-1);
  }

  private Stream<MoveLayout> movePointLayouts() {

    return
      matchingMoves()
        .findAny()
        .get()
        .getMovePointLayouts()
        .stream();
  }

  public List<MoveLayout> getMovePointLayouts() {

    List<MoveLayout> moveLayouts = movePointLayouts().toList();
    int lastActualInputPosition = position()-1;

    if (lastActualInputPosition > lastRecordedInputPosition) {
      moveLayouts = moveLayouts.subList(
        lastRecordedInputPosition,
        lastActualInputPosition
      );
    } else {
      moveLayouts = List.of(
        moveLayouts.get(lastRecordedInputPosition)
      );
    }
    lastRecordedInputPosition = lastActualInputPosition;
    return moveLayouts;
  }

  public boolean hasMoveLayouts () {

    return  lastRecordedInputPosition != position();
  }

  private boolean inputPointIsLegalStartingPoint (int position) {

    return
      isStartingPoint(position)
      && pointsIn(position).anyMatch(point -> point == inputPoint);
  }

  void autoSelect () {

    int position = position();

    if (position < movePoints.length && uniquePointIn(position)) {
      movePoints[position] =
        pointsIn(position)
          .findFirst()
          .orElse(-1);
      autoSelect();
    }
  }

  public boolean isUniqueMove() {

    return
      matchingMoves().count() == 1;
  }

  private MoveLayout uniqueMove () {

    return
      isUniqueMove()
        ? matchingMoves().findAny().get()
        : null;
  }

  public int getUniqueEvaluatedMoveNr() {

    return getLayoutNr(uniqueMove());
  }

  private void setEndingPoint () {

    int endingPointPosition = endingPointPosition(inputPoint);

    System.out.println("endingPointPosition: "+endingPointPosition);
    if (endingPointPosition > position()) {
      System.out.println("Projecting movePoints");
      movePoints = new MoveProjection(this)
        .projectMovePointsTo(endingPointPosition);
      lastRecordedInputPosition = position()-1;
    } else {
      movePoints[endingPointPosition] = inputPoint;
    }
  }

  private boolean explicitEndingPoint () {

    return
      isEndingPoint(position())
      && validEndingPoints().count() == 1;
  }

  private void setInputPoint () {

    if (inputPointIsLegalStartingPoint(position())) {
      System.out.println("Input is startingPoint:");
      movePoints[position()] = inputPoint;
      printMovePoints();
      System.out.println("position: "+position());
      System.out.println("isLegalEndingPoint: "+isLegalEndingPoint(position()));
      System.out.println("validEndingPointCount: "+validEndingPoints().count());
      if (explicitEndingPoint()) {
        System.out.println("Explicit endingPoint in pos: "+position());
        movePoints[position()] = pointsIn(position()).findAny().get();
      }
    } else if (isLegalEndingPoint(inputPoint)) {
      setEndingPoint();
    }
  }

  private void deleteLatestInput () {

    if (--lastRecordedInputPosition < 0) {
      lastRecordedInputPosition = 0;
    }
    movePoints[lastRecordedInputPosition] = -1;
  }

  private boolean moveIsLegal () {

    return !noLegalMove();
  }

  public boolean inputIsLegal () {

    return position() != lastRecordedInputPosition;
  }

  void inputPoint (int point) {

    if (!endOfInput() && moveIsLegal()) {
      this.inputPoint = point;
      lastRecordedInputPosition = position();
      printReport();
      if (point == -1) {
        deleteLatestInput();
      } else {
        setInputPoint();
      }
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
