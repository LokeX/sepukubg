package bg.engine.match.moves;

import bg.engine.match.Dice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.*;

public class MoveLayout extends Layout {

  List<MoveLayout> movePointsLayouts;
  protected Dice dice;
  private Moves parentMoves;
  private int[] hitPoints;
  int[] movePoints;
  int[] movePoints2;

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

  protected MoveLayout () {

  }

  public MoveLayout (MoveLayout moveLayout) {

    super(moveLayout);
    parentMoves = moveLayout.parentMoves;
    dice = moveLayout.getDiceObj();
    movePoints = moveLayout.getMovePoints();
    movePoints2 = moveLayout.getMovePoints2();
    hitPoints = moveLayout.getHitPoints();
  }

  MoveLayout (Moves moves, Layout layout, int[] dice) {

    super(layout);
    parentMoves = moves;
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

  boolean movePointsMatch (int[] movePointsToMatch) {

    int nrOfMovePointsToMatch =
      (int) Arrays.stream(movePointsToMatch)
        .filter(point -> point != -1)
        .count();
    int nrOfMatches =
      (int) IntStream.range(0, movePointsToMatch.length)
        .filter(position -> movePointsToMatch[position] == movePoints[position])
        .count();

    return nrOfMatches == nrOfMovePointsToMatch;
  }

  public void setMovePoints(int[] newPoints) {

    movePoints = newPoints.clone();
  }

  final public int getNrOfPartMoves() {

    return parentMoves.getNrOfLegalPartMoves();
  }

  boolean isWinningMove () {

    return rearPos == 0;
  }

  boolean endingPointIsAmbiguous(int endingPoint) {

    return
      !dice.areDouble() &&
      movePoints[3] == endingPoint &&
      hitPoints[1] != -1 &&
      movePoints[1] == movePoints[2];
  }

  protected List<Layout> getMovePointLayouts () {

    List<Layout> movePointLayouts = new ArrayList<>();
    int[] tempPoint = parentMoves.getParentMoveLayout().getPoint().clone();
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

    movePoints[position] =
      originalMovePoints[position];
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

  private MoveLayout clonedMoveLayout () {

    MoveLayout moveLayout = new MoveLayout(this);

    moveLayout.point =
      parentMoves
        .getParentMoveLayout()
        .point
        .clone();
    moveLayout.resetMovePoints();
    return moveLayout;
  }

  private List<MoveLayout> moveLayoutsList () {

    return
      clonedMoveLayout()
        .generateMoveLayouts(
          new ArrayList<>(),
          movePoints,
          0
        );
  }

  private List<MoveLayout> parentMoveLayoutList () {

    return
      List.of(
        parentMoves
          .getParentMoveLayout()
      );
  }

  public List<MoveLayout> getMoveLayouts () {

    if (isIllegal()) {
      return parentMoveLayoutList();
    } else {
      if (movePointsLayouts == null) {
        movePointsLayouts = moveLayoutsList();
      }
      return movePointsLayouts;
    }
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
