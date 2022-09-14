package bg.engine.api.moveInput;

import bg.engine.coreLogic.moves.Layout;
import bg.engine.coreLogic.moves.MoveLayout;
import bg.engine.coreLogic.moves.Moves;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveSelection extends Moves {

  protected int[] movePoints;
  private int inputPoint;
  private int startPos;
  private int endPos;

  protected MoveSelection(MoveSelection moveSelection) {

    super(moveSelection);
    this.movePoints = moveSelection.movePoints.clone();
  }

  public MoveSelection(Moves moves) {

    super(moves);
    resetMovePoints();
  }

  private void resetMovePoints () {

    movePoints = new int[getDice().length*2];
    Arrays.fill(movePoints, -1);
  }

  public boolean endOfInput () {

    return
      endOfInput(position());
  }

  public boolean endOfInput (int position) {

    return
      position >= movePoints.length;
  }

  public int[] getMovePoints () {

    return movePoints;
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

    return
      !endOfInput(position)
      && position % 2 == 1;
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

    return
      isEndingPoint(position());
  }

  private boolean isLegalEndingPoint (int endingPoint) {

    return
      positionIsEndingPoint()
      && validEndingPoints()
        .anyMatch(point -> point == endingPoint);
  }

  private boolean isStartingPoint (int position) {

    return
      !endOfInput(position)
      && position % 2 == 0;
  }

  public List<Layout> getMovePointLayouts() {

    System.out.println("Generating moveOutputLayouts: ");
    System.out.println("startPos: "+startPos);
    System.out.println("endPos: "+endPos);

    return
      startPos != endPos
      ?  matchingMoves()
          .findAny()
          .get()
          .getMovePointLayouts()
          .stream()
          .map(Layout::new)
          .toList()
          .subList(
            startPos,
            endPos
          )
      : List.of(getParentMoveLayout());
  }

  private boolean inputPointIsLegalStartingPoint (int position) {

    return
      isStartingPoint(position)
      && pointsIn(position).anyMatch(point -> point == inputPoint);
  }

  private int getPointIn (int position) {

    return
      pointsIn(position)
        .findFirst()
        .orElse(-1);
  }

  private boolean singlePointIn(int position) {

    return
      pointsIn(position).count() == 1;
  }

  public void autoSelectPoints () {

    while (!endOfInput() && singlePointIn(position())) {
      movePoints[position()] = getPointIn(position());
    }
    if (positionIsEndingPoint()) {
      movePoints[position()-1] = -1;
    }
    endPos = position();
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

    return
      uniqueMove() != null
      ? getLayoutNr(uniqueMove())
      : -1;
  }

  private void setEndingPoint () {

    int endingPointPosition = endingPointPosition(inputPoint);

    System.out.println("endingPointPosition: "+endingPointPosition);
    if (endingPointPosition > position()) {
      System.out.println("Projecting movePoints");
      movePoints = new MoveProjection(this)
        .projectMovePointsTo(endingPointPosition);
    } else {
      movePoints[endingPointPosition] = inputPoint;
    }
  }

  private boolean explicitEndingPoint (int position) {

    return
      isEndingPoint(position)
      && validEndingPoints().count() == 1;
  }

  private void setInputPoint () {

    startPos = position();
    if (inputPointIsLegalStartingPoint(position())) {
      System.out.println("Input is startingPoint:");
      movePoints[position()] = inputPoint;
      printMovePoints();
      System.out.println("position: "+position());
      System.out.println("isLegalEndingPoint: "+isLegalEndingPoint(position()));
      System.out.println("validEndingPointCount: "+validEndingPoints().count());
      if (explicitEndingPoint(position())) {
        System.out.println("Explicit endingPoint in pos: "+position());
        movePoints[position()] = getPointIn(position());
      }
    } else if (isLegalEndingPoint(inputPoint)) {
      setEndingPoint();
    }
    endPos = position();
    if (startPos < endPos-1) {
      startPos = endPos-1;
    }
  }

  private void pointBackSpace () {

    startPos = position();
    movePoints[startPos == 0 ? 0 : startPos-1] = -1;
    endPos = position();
    startPos = endPos > 0 ? endPos-1 : 0;
    System.out.println("startPos = "+startPos);
    System.out.println("endPos = "+endPos);
  }

  private boolean moveIsLegal () {

    return !noLegalMove();
  }

  void inputPoint (int point) {

    if (moveIsLegal()) {
      inputPoint = point;
      System.out.println();
      System.out.println("Start report:");
      printReport();
      if (point == -1) {
        pointBackSpace();
      } else if (!endOfInput()) {
        setInputPoint();
      }
      System.out.println();
      System.out.println("End report:");
      printReport();
    }
  }

  public void printMovePoints () {

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
