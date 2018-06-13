package bg.engine.moves;

import java.util.ArrayList;
import java.util.List;

import static bg.util.StreamsUtil.streamAsList;

public class InputPoints extends MovePoints {

  private int inputPoint;

  InputPoints(Moves moves) {

    super(moves);
  }

  public int getPosition () {

    return position();
  }

  private boolean inputPointIsLegalStartingPoint (int position) {

    return
      isStartingPoint(position) && pointsIn(position)
        .anyMatch(point -> point == inputPoint);
  }

  private void autoMove (int max) {

    if (position() < max && uniquePointIn(position())) {
      movePoints[position()]
        = pointsIn(position())
        .findFirst()
        .orElse(-1);
      autoMove(max);
    }
  }

  public void initialAutoMove () {

    if (position() == 0) {
      autoMove(movePoints.length);

      int storedPosition = position();

      resetMovePoints();
      autoMove(storedPosition - (storedPosition%2));
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
      isUniqueMove()
        ? matchingMoves().findAny().get()
        : null;
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
    if (endingPointPosition > position()) {
      movePoints = new MoveProjection(this)
        .projectMovePointsTo(endingPointPosition);
    } else {
      movePoints[endingPointPosition] = inputPoint;
    }
  }

  private void setInputPoint () {

    if (inputPointIsLegalStartingPoint(position())) {
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
