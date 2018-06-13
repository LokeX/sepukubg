package bg.engine.moves;

import bg.engine.Dice;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.*;

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

  public Layout getParentLayout() {

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

  List<Integer> getMoveablePoints(int die) {

    List<Integer> moveablePoints = new ArrayList<>(20);
//    List<Integer> moveablePoints = new LinkedList<>();

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
      point[startingPoint] > 0 &&
      (point[25] == 0 || startingPoint == 25) &&
      (endingPoint == 0 || point[endingPoint + 26] < 2) &&
      (startingPoint == rearPos || endingPoint >= llep(rearPos));
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

    return new MoveLayout(this).setPartMove(dieNr, dieFaces, startingPoint);
  }

  public boolean isIllegal () {

    return
      stream(movePoints)
        .noneMatch(point -> point != -1);

//    for (int a = 0; a < movePoints.length; a++) {
//      if (movePoints[a] != -1) {
//        return false;
//      }
//    }
//    return true;
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

  public boolean movePointsMatch (int[] movePointsToMatch) {

    int nrOfMovePointsToMatch = 0;
    int nrOfMatches = 0;

    for (int movePoint : movePointsToMatch) {
      if (movePoint != -1) {
        nrOfMovePointsToMatch++;
      }
    }
    for (int a = 0; a < nrOfMovePointsToMatch; a++) {
      if (movePointsToMatch[a] == movePoints[a]) {
        nrOfMatches++;
      }
    }
    return nrOfMatches == nrOfMovePointsToMatch;
  }

  public void setMovePoints(int[] newPoints) {

    movePoints = newPoints.clone();
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

  public boolean endingPointIsAmbiguous(int endingPoint) {

    return
      !dice.areDouble() &&
      movePoints[3] == endingPoint &&
      hitPoints[1] != -1 &&
      movePoints[1] == movePoints[2];
  }

  public List<Layout> getMovePointLayouts () {

    List<Layout> movePointLayouts = new ArrayList<>();
    int[] tempPoint = parentLayout.point.clone();
    int nrOfMovePoints = getNrOfPartMoves()*2;

    for (int a = 0; a < nrOfMovePoints; a++) {
      if (hitPoints[a] >= 0) {
        if ((a+2)%2 == 0) {
          tempPoint[hitPoints[a]]--;
        } else {
          tempPoint[hitPoints[a]]++;
        }
      }
      if ((a+2)%2 == 0) {
        tempPoint[movePoints2[a]]--;
      } else {
        tempPoint[movePoints2[a]]++;
      }
      movePointLayouts.add(new Layout(tempPoint));
    }
    return movePointLayouts;
  }

  private void paintPosition (int position) {

    if (hitPoints[position] >= 0) {
      if (position%2 == 0) {
        point[hitPoints[position]]--;
      } else {
        point[hitPoints[position]]++;
      }
    }
    if (position%2 == 0) {
      point[movePoints2[position]]--;
    } else {
      point[movePoints2[position]]++;
    }
  }

  private List<MoveLayout> generateMoveLayouts (

    List<MoveLayout> moveLayouts,
    int[] originalMovePoints,
    int position ) {

    movePoints[position]
      = originalMovePoints[position];
    paintPosition(position);
    moveLayouts.add(this);
    if (position < movePoints.length-1) {
      if (movePoints2[position+1] != -1) {
        new MoveLayout(this)
          .generateMoveLayouts(
            moveLayouts,
            originalMovePoints,
            position+1
          );
      }
    }
    return moveLayouts;
  }

  private void resetMovePoints () {

    for (int a = 0; a < movePoints.length; a++) {
      movePoints[a] = -1;
    }
  }

  public List<MoveLayout> getMoveLayouts () {

    MoveLayout moveLayout = new MoveLayout(this);

    moveLayout.point = parentLayout.point.clone();
    moveLayout.resetMovePoints();

    return
      moveLayout
        .generateMoveLayouts(
          new ArrayList<>(),
          movePoints,
          0
        );
  }

  public List<Layout> getPartMoveLayouts () {

    List<Layout> movePointsLayout = getMovePointLayouts();
    List<Layout> partMoveLayouts = new ArrayList<>();

    for (int a = 1; a < movePoints.length; a += 2) {
      partMoveLayouts.add(movePointsLayout.get(a));
    }
    return partMoveLayouts;
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
