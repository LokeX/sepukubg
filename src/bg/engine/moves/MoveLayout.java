package bg.engine.moves;

import bg.engine.Dice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveLayout extends Layout {

  protected Dice dice;
  protected int[] movePoints;
  protected int[] movePoints2;
  protected int[] hitPoints;
  protected Layout parentLayout;

  public int[] getDice() {

    return dice.getDice();
  }

  public Dice getDiceObj () {

    return new Dice(dice);
  }

  public int[] getMovePoints() {

    int[] temp = new int[movePoints.length];

    System.arraycopy(movePoints, 0, temp, 0, movePoints.length);
    return temp;
//    return movePoints.clone();
  }

  public int[] getMovePoints2() {

    int[] temp = new int[movePoints2.length];

    System.arraycopy(movePoints2, 0, temp, 0, movePoints2.length);
    return temp;
//    return movePoints2.clone();
  }

  public int[] getHitPoints() {

    int[] temp = new int[hitPoints.length];

    System.arraycopy(hitPoints, 0, temp, 0, hitPoints.length);
    return temp;
//    return hitPoints.clone();
  }

  public Layout getParentLayout () {

    return parentLayout;
  }

  public MoveLayout () {

  }

  public MoveLayout (MoveLayout moveLayout) {

    super(moveLayout);
    parentLayout = moveLayout.getParentLayout();
    dice = moveLayout.getDiceObj();
    movePoints = moveLayout.getMovePoints();
    movePoints2 = moveLayout.getMovePoints2();
    hitPoints = moveLayout.getHitPoints();
  }

  public MoveLayout (Layout layout, int[] dice) {

    super(layout);
    parentLayout = layout;
    this.dice = new Dice(dice);
    movePoints = new int[this.dice.getDice().length*2];
    hitPoints = new int[movePoints.length];
    movePoints2 = new int[movePoints.length];
    for (int a = 0; a < movePoints.length; a++) {
      movePoints[a] = -1;
      hitPoints[a] = -1;
      movePoints2[a] = -1;
    }
  }

  public List<Integer> getMoveablePointsList (int die) {

    List<Integer> moveablePoints = new ArrayList();

    if (rearPos < 0) {
      calcRearPos();
    } else if (rearPos < 25 && point[25] > 0) {
      rearPos = 25;
    } else if (point[rearPos] == 0) {
      calcRearPos(rearPos);
    }
//    calcRearPos(); // Brute force, I can't fix
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

  public boolean partMoveIsLegal (int startingPoint, int endingPoint) {

    return
      point[startingPoint] > 0 &&
      (point[25] == 0 || startingPoint == 25) &&
      (endingPoint == 0 || point[endingPoint + 26] < 2) &&
      (startingPoint == rearPos || endingPoint >= llep(rearPos));
  }

  public boolean notIn (List<EvaluatedMove> moveLayoutsList) {

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

  public MoveLayout getPartMoveLayout (int dieNr, int[] dieFaces, int startingPoint) {

    return new MoveLayout(this).setPartMove(dieNr, dieFaces, startingPoint);
  }

  public boolean isIllegal () {

    for (int a = 0; a < movePoints.length; a++) {
      if (movePoints[a] != -1) {
        return false;
      }
    }
    return true;
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

  public void setMovePoints(int[] newPoints) {

//    movePoints = newPoints.clone();
    movePoints2 = newPoints.clone();
    if (playerID == 1) {
      for (int a = 0; a < newPoints.length; a++) {
        movePoints[a] = 25-newPoints[a];
      }
    }
  }

  final public int getNrOfPartMoves() {

    int count = 0;

    for (int a = 0; a < movePoints.length; a++) {
      if (movePoints[a] < 0) {
        count++;
      }
    }
    return (movePoints.length - count) / 2;
  }

  public boolean isWinningMove () {

    return rearPos == 0;
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
    for (int d : dice.getDice()) {
      System.out.print(d+",");
    }
    System.out.print("]");
  }

}
