package bg.engine.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static bg.util.StreamsUtil.streamAsList;

public class MovePointsInput extends MovePoints {

  private int inputPoint;
  private int storedPosition;

  public MovePointsInput (MovePoints movePoints) {

    super(movePoints);
  }

  MovePointsInput (Moves moves) {

    super(moves);
  }

  public int getStoredPosition () {

    return storedPosition;
  }

  private boolean inputPointIsLegalStartingPoint (int position) {

    return
      isStartingPoint(position) && pointsIn(position)
        .anyMatch(point -> point == inputPoint);
  }

  private List<Layout> autoMove (List<Layout> layouts, int max) {

    if (position() <= max && uniquePointIn(position())) {
      movePoints[position()]
        = pointsIn(position())
        .findFirst()
        .orElse(-1);
      layouts.add(
        getMatchingLayout()
      );
      autoMove(layouts, max);
    }
    return layouts;
  }

  private boolean isVirginMove () {

    return
      Arrays.stream(movePoints)
        .filter(point -> point == -1)
        .count() == movePoints.length;
  }

  public List<Layout> initialAutoMove () {

    if (isVirginMove()) {
      int max = autoMove(new ArrayList<>(), movePoints.length).size();

      resetMovePoints();
      return autoMove(new ArrayList<>(), max - (max%2));
    } else {
      return new ArrayList<>();
    }
  }

  public Layout getMatchingLayout () {

    if (position() == 0) {
      return getParentLayout();
    } else {
      return matchingMoves()
        .findFirst()
        .get()
        .getMoveLayouts()
        .get(position());
    }
  }

  private boolean isLegalEndingPoint (int endingPoint) {

    return
      positionIsEndingPoint()
        && validEndingPoints()
        .anyMatch(point -> point == endingPoint);
  }

  public boolean isUniqueMove () {

    return matchingMoves().count() == 1;
  }

  private MoveLayout uniqueMove () {

    return
      isUniqueMove() ?
        matchingMoves().findAny().get() :
        null;
  }

  public List<Layout> getUniqueMoveLayouts () {

    if (isUniqueMove() && position() > 0) {
      return matchingMoves()
        .findAny()
        .get()
        .getMovePointLayouts()
          .stream()
          .skip(storedPosition) // is this correct?
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  private void setAnyUniqueMove () {

    if (isUniqueMove()) {
      movePoints = uniqueMove().getMovePoints();
    }
  }

  public int getUniqueEvaluatedMoveNr () {

    return getMoveNr(uniqueMove());
  }

  private void setEndingPoint () {

    int endingPointPosition = endingPointPosition(inputPoint);

    System.out.println("endingPointPosition: "+endingPointPosition);
    storedPosition = position();
    if (endingPointPosition > position()) {
      movePoints = new MoveProjection(this)
        .projectMovePointsTo(endingPointPosition);
    } else {
      movePoints[endingPointPosition] = inputPoint;
    }
  }

  private void setInputPoint () {

    if (inputPointIsLegalStartingPoint(position())) {
      storedPosition = position();
      this.movePoints[position()] = inputPoint;
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

    return !getEvaluatedMoves().get(0).isIllegal();
  }

  public void input (int inputPoint) {

    this.inputPoint = inputPoint;
    if (!endOfInput() && moveIsLegal()) {
      printReport();
      setInputPoint();
      setAnyUniqueMove();
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

  public void printReport () {

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
