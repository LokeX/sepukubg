package bg.engine.coreLogic.moves;

import bg.engine.coreLogic.Dice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.joining;

public class MoveLayout extends Layout {

  private MovePointLayouts movePointLayouts;

  Moves parentMoves;
  int[] hitPoints;
  int[] movePoints;
  int[] movePoints2;

  public MoveLayout getParentMoveLayout () {

    return parentMoves.getParentMoveLayout();
  }

  public int[] getDice() {

    return parentMoves.getDice();
  }

  public Dice getDiceObj () {

    return new Dice(parentMoves.getDice());
  }

  public int[] getMovePoints() {

    int[] temp = new int[movePoints.length];

    System.arraycopy(movePoints, 0, temp, 0, movePoints.length);
    return temp;
  }

  public int[] getMovePoints2() {

    int[] temp = new int[movePoints2.length];

    System.arraycopy(movePoints2, 0, temp, 0, movePoints2.length);
    return temp;
  }

  public int[] getHitPoints() {

    int[] temp = new int[hitPoints.length];

    System.arraycopy(hitPoints, 0, temp, 0, hitPoints.length);
    return temp;
  }

  protected MoveLayout () {

  }

  public MoveLayout (Layout layout) {

    super(layout);
  }

  public MoveLayout (MoveLayout moveLayout) {

    super(moveLayout);
    parentMoves = moveLayout.parentMoves;
    movePoints = moveLayout.getMovePoints();
    movePoints2 = moveLayout.getMovePoints2();
    hitPoints = moveLayout.getHitPoints();
  }

  MoveLayout (Moves moves, Layout layout) {

    super(layout);
    parentMoves = moves;
    movePoints = new int[parentMoves.getDice().length*2];
    hitPoints = new int[movePoints.length];
    movePoints2 = new int[movePoints.length];
    for (int a = 0; a < movePoints.length; a++) {
      movePoints[a] = -1;
      hitPoints[a] = -1;
      movePoints2[a] = -1;
    }
  }

  List<Integer> getMoveablePoints(int die) {

    List<Integer> moveablePoints = new ArrayList<>(20);

    if (rearPos < 0) {
      calcRearPos();
    } else if (rearPos < 25 && point[25] > 0) {
      rearPos = 25;
    } else if (point[rearPos] == 0) {
      calcRearPos(rearPos);
    }
    for (int startingPoint = rearPos; startingPoint > 0; startingPoint--) {
      if (point[startingPoint] > 0) {
        if (partMoveIsLegal(startingPoint, startingPoint-die)) {
          moveablePoints.add(startingPoint);
        } else if (startingPoint == 25) {
          return moveablePoints;
        }
      }
    }
    return moveablePoints;
  }

  private boolean partMoveIsLegal (int startingPoint, int endingPoint) {

    return
      point[startingPoint] > 0
      && (point[25] == 0 || startingPoint == 25)
      && (endingPoint == 0 || point[endingPoint + 26] < 2)
      && (startingPoint == rearPos || endingPoint >= llep(rearPos));
  }

  boolean notIn (List<EvaluatedMove> moveLayoutsList) {

    generateHashCode();
    return moveLayoutsList.stream().
      noneMatch(evaluatedMove -> evaluatedMove.hash == this.hash);
  }

  private MoveLayout setPartMove (int dieNr, int[] dieFaces, int startingPoint) {

    int endingPoint = startingPoint - dieFaces[dieNr];

    if (endingPoint < 0) {
      endingPoint = 0;
    }
    movePoints[dieNr * 2] = playerID == 1 ? 25 - startingPoint : startingPoint;
    movePoints[(dieNr * 2) + 1] = playerID == 1 ? 25 - endingPoint : endingPoint;
    movePoints2[dieNr * 2] = startingPoint;
    movePoints2[(dieNr * 2) + 1] = endingPoint;
    if (point[endingPoint + 26] == 1) {
      hitPoints[dieNr * 2] = endingPoint + 26;
      hitPoints[(dieNr * 2) + 1] = 26;
      point[endingPoint + 26]--;
      point[26]++;
    }
    point[startingPoint]--;
    point[endingPoint]++;
    return this;
  }

  MoveLayout getPartMoveLayout (int dieNr, int[] dieFaces, int startingPoint) {

    return new
      MoveLayout(this)
        .setPartMove(
          dieNr,
          dieFaces,
          startingPoint
        );
  }

  public boolean isIllegal () {

    return
      stream(movePoints)
        .noneMatch(point -> point != -1);
  }

  public boolean containsThesePartMoves (int[] partMove) {

    for (int a = 0; a < movePoints.length; a += 2) {
      for (int b = 0; b < partMove.length; b += 2) {
        if (movePoints[a] == partMove[b] && movePoints[a+1] == partMove[b+1]) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void printMovePointsArray (int[] movePoints) {
    
    System.out.println(
      Arrays.stream(movePoints)
        .mapToObj(Integer::toString)
        .collect(Collectors.joining(","))
    );
  }

  public boolean movePointsMatch (int[] pointsToMatch) {

//    System.out.println("Comparing pointsToMatch: ");
//    printMovePointsArray(pointsToMatch);
//    System.out.println("and movePoints: ");
//    printMovePointsArray(movePoints);
    int nrOfMatchesRequired =
      (int) Arrays.stream(pointsToMatch)
        .filter(point -> point != -1)
        .count();
//    System.out.println("nrOfMatchesRequired: "+nrOfMatchesRequired);
    int nrOfMatches =
      (int) IntStream.range(0, nrOfMatchesRequired)
        .filter(position -> pointsToMatch[position] == movePoints[position])
        .count();
//    System.out.println("nrOfMatches: "+nrOfMatches);

    return nrOfMatches == nrOfMatchesRequired;
  }

  public void setMovePoints(int[] newMovePoints) {

    movePoints = newMovePoints.clone();
  }

  final public int getNrOfPartMoves() {

    return parentMoves.getNrOfLegalPartMoves();
  }

  boolean isWinningMove () {

    return rearPos == 0;
  }

  public boolean endingPointIsAmbiguous(int endingPoint) {

    return
      !parentMoves.getDiceObj().areDouble() &&
      movePoints[3] == endingPoint &&
      hitPoints[1] != -1 &&
      movePoints[1] == movePoints[2];
  }

  public List<MoveLayout> getMovePointLayouts() {

    if (movePointLayouts == null) {
      movePointLayouts = new MovePointLayouts(new MoveLayout(this));
    }
    return movePointLayouts.getMoveLayoutsList();
  }

  public String getMovePointsString() {

    String movePointsString = "";

    for (int a = 0; a < movePoints.length; a++) {
      movePointsString += Integer.toString(
        movePoints[a]) + (a == movePoints.length-1 ? "" : ","
      );
    }
    return "["+movePointsString+"]";
  }

  public void printMovePoints () {

    System.out.print("[");
    for (int mp : movePoints) {
      System.out.print(mp+",");
    }
    System.out.print("]");
  }

  public void printDice () {

    System.out.print("[");
    for (int d : parentMoves.getDice()) {
      System.out.print(d+",");
    }
    System.out.print("]");
  }

}
