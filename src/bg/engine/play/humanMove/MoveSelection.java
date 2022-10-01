package bg.engine.play.humanMove;

import bg.engine.core.moves.Layout;
import bg.engine.core.moves.MoveLayout;
import bg.engine.core.moves.Moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveSelection extends Moves {

  protected int[] movePoints;
  private int inputPoint;
  private int startPos;
  private int endPos;

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
      position >= getNrOfLegalPartMoves()*2;
  }

  public int[] getMovePoints () {

    return movePoints;
  }

  private int position () {

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

  private boolean isOptionalHit(int endingPoint) {

    return
      matchingMoves()
        .anyMatch(move ->
          move.hasFirstEndingPointHit(endingPoint)
        );
  }

  private Stream<Integer> endingPointsIn (int position) {

    return
      getDiceObj().areDouble() || position == 3
      ? pointsIn(position)
      : pointsIn(position)
          .filter(endingPoint ->
            !isOptionalHit(endingPoint)
          );
  }

  private boolean endingPointIsInPosition (int endingPoint, int position) {

    return
      endingPointsIn(position)
        .anyMatch(point -> point == endingPoint);
  }

  public Stream<Integer> validEndingPoints () {

    int[] tempPoints = movePoints.clone();
    Stream<Integer> validEndingPoints =
      projectMovePointsTo(movePoints.length);

    movePoints = tempPoints;

    return
      validEndingPoints;
  }

  private boolean isEndingPoint (int position) {

    return
      !endOfInput(position)
      && position % 2 == 1;
  }

  public boolean positionIsEndingPoint () {

    return
      isEndingPoint(position());
  }

  private boolean isLegalEndingPoint (int endingPoint) {

    return
      positionIsEndingPoint()
      && validEndingPoints().anyMatch(point -> point == endingPoint);
  }

  private boolean isStartingPoint (int position) {

    return
      !endOfInput(position)
      && position % 2 == 0;
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
  
  private boolean explicitEndingPoint (int position) {

    return
      isEndingPoint(position)
      && validEndingPoints().count() == 1;
  }

  public List<Layout> getMovePointLayouts() {

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

  private boolean positionIsAutoCompletable () {

    return
      isStartingPoint(position())
      && singlePointIn(position())
      || explicitEndingPoint(position());

  }

  public void autoSelectPoints () {

    while (!endOfInput() && positionIsAutoCompletable()) {
      movePoints[position()] = getPointIn(position());
    }
    if (position() > endPos && positionIsEndingPoint()) {
      movePoints[position()-1] = -1;
    }
    endPos = position();
  }

  public boolean isUniqueMove() {

    return
      endOfInput();
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
  
  private int[] noUsedDicePattern () {
    
    int[] pattern = getDice().clone();
    
    Arrays.fill(pattern,0);
    return
      pattern;
  }

  public int[] dicePattern () {

    return
      position() == 0
      ? noUsedDicePattern()
      : matchingMoves()
        .findAny()
        .get()
        .getMovePointLayouts()
        .get(position()-1)
        .dicePattern();
  }
  
  private Stream<Integer> projectMovePointsTo (int terminalPosition) {

    List<Integer> validEndingPoints = new ArrayList<>();
    List<Integer> endingPoints = new ArrayList<>();

    do {
      if (positionIsEndingPoint()) {
        endingPoints = endingPointsIn(position()).toList();
        validEndingPoints.addAll(endingPoints);
      }
      if (endingPoints.size() > 0) {
        movePoints[position()] = endingPoints.get(0);
      }
    } while (
      endingPoints.size() > 0
      && !endOfInput()
      && position() <= terminalPosition
    );
    return
      validEndingPoints.stream().distinct();
  }
  
  private int inputPosition () {
    
    return
      range(position(), movePoints.length)
        .filter(this::isEndingPoint)
        .filter(position -> endingPointIsInPosition(inputPoint, position))
        .findAny()
        .orElse(-1);
  }
  
  private int doubleDiceInputPosition() {
    
    int startingPointPosition = position()-1;
    int pointDistance = movePoints[startingPointPosition] - inputPoint;
    
    pointDistance *= pointDistance < 0 ? -1 : 1;
    return
      startingPointPosition+((pointDistance/getDice()[0])*2)-1;
  }
  
  private void setEndingPoint () {

    int inputPosition = inputPosition();

    if (getDiceObj().areDouble()) {
      projectMovePointsTo(doubleDiceInputPosition());
    } else if (inputPosition > position()) {
      projectMovePointsTo(inputPosition);
    } else {
      movePoints[inputPosition] = inputPoint;
    }
  }
  
  private void setStartingPoint () {
    
    movePoints[position()] = inputPoint;
    if (explicitEndingPoint(position())) {
      movePoints[position()] = getPointIn(position());
    }
  }

  private void setInputPoint () {

    startPos = position();
    if (inputPointIsLegalStartingPoint(position())) {
      setStartingPoint();
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
  }

  private boolean moveIsLegal () {

    return !noLegalMove();
  }
  
  private boolean legalInputPoint() {
    
    return
      inputPointIsLegalStartingPoint(position())
      || isLegalEndingPoint(inputPoint);
  }

  public void inputPoint (int point) {

    if (moveIsLegal()) {
      inputPoint = point;
      if (point == -1 && position() > 0) {
        pointBackSpace();
      } else if (!endOfInput() && legalInputPoint()) {
        setInputPoint();
      }
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
