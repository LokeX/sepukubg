package bg.engine.match.moves;

import bg.engine.match.Turn;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveInput extends Moves {

  int[] movePoints;
  private int inputPoint;
  private int storedPosition;
  private int turnNr;

  MoveInput(MoveInput moveInput) {

    super(moveInput);
    this.movePoints = moveInput.movePoints.clone();
  }

  public MoveInput(Turn turn) {

    super(turn);
    turnNr = turn.getTurnNr();
    resetMovePoints();
  }

  public boolean hasIdenticalTurnNrTo(int turnNr) {

    return this.turnNr == turnNr;
  }

  private void resetMovePoints () {

    movePoints = IntStream
      .range(0, getDice().length*2)
      .map(i -> -1)
      .toArray();
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

  private int endingPointPosition (int endingPoint) {

    return range(position(), movePoints.length)
      .filter(this::isEndingPoint)
      .filter(position -> endingPointIsInPosition(endingPoint, position))
      .findAny()
      .orElse(-1);
  }

  private boolean isStartingPoint (int position) {

    return position%2 == 0;
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

  private boolean uniquePointIn (int position) {

    return
      position < movePoints.length
        && pointsIn(position).count() == 1;
  }

  public MoveLayout getMatchingMoveLayout() {

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
        .getMoveLayouts()
        .stream();
  }

  public List<Layout> getOutputLayouts () {

    List<Layout> layouts = moveLayouts()
      .collect(Collectors.toList());
    int start =
      storedPosition > position()
        ? position()
        : storedPosition;
    int end =
      storedPosition < position()
        ? position()
        : storedPosition;

    return layouts.subList(start, end);
  }

  private boolean inputPointIsLegalStartingPoint (int position) {

    return
      isStartingPoint(position) && pointsIn(position)
        .anyMatch(point -> point == inputPoint);
  }

  private void autoSelect (int max) {

    if (position() < max && uniquePointIn(position())) {
      movePoints[position()] = pointsIn(position())
        .findFirst().orElse(-1);
      autoSelect(max);
    }
  }

  public void initialSelection() {

    if (position() == 0) {
      autoSelect(movePoints.length);

      int storedPosition = position();

      resetMovePoints();
      autoSelect(storedPosition - (storedPosition%2));
    }
  }

  private boolean isLegalEndingPoint (int endingPoint) {

    return
      positionIsEndingPoint() && validEndingPoints()
        .anyMatch(point -> point == endingPoint);
  }

  private boolean isUniqueMove () {

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
      storedPosition = position()-1;
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

  public void deleteLatestInput () {

    int inputPointNr = position();

    if (inputPointNr > 0) {
      movePoints[inputPointNr-1] = -1;
    }
  }

  private boolean moveIsLegal () {

    return !isIllegal();
  }

  public boolean inputIsLegal () {

    return position() != storedPosition;
  }

  public void input (int inputPoint) {

    if (!endOfInput() && moveIsLegal()) {
      this.inputPoint = inputPoint;
      storedPosition = position();
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
