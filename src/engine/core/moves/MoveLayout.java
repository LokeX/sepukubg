package engine.core.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.joining;

public class MoveLayout extends Layout {

  private String annotation;
  private List<int[]> dicePatterns;
  protected List<MoveLayout> movePointLayouts;
  protected Moves parentMoves;
  protected int[] hitPoints;
  protected int[] movePoints;
  protected int[] movePoints2;
  protected int[] movePointsBackup;

  public MoveLayout getParentMoveLayout () {

    return parentMoves.getParentMoveLayout();
  }

  public int[] getDice() {

    return parentMoves.getDice();
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

  public int[] getMovePointsBackup () {

    int[] temp = new int[hitPoints.length];

    System.arraycopy(movePointsBackup, 0, temp, 0, movePointsBackup.length);
    return temp;
  }

  protected MoveLayout () {

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

    if (rearPos < 0 || (rearPos == 25 && point[25] == 0)) {
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
    int startPos = dieNr*2;
    int endPos = startPos+1;

    if (endingPoint < 0) {
      endingPoint = 0;
    }
    movePoints[startPos] = playerID == 1 ? 25 - startingPoint : startingPoint;
    movePoints[endPos] = playerID == 1 ? 25 - endingPoint : endingPoint;
    movePoints2[startPos] = startingPoint;
    movePoints2[endPos] = endingPoint;
    if (point[endingPoint + 26] == 1) {
      hitPoints[startPos] = endingPoint + 26;
      hitPoints[endPos] = 26;
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

  public boolean movePointsMatch (int[] pointsToMatch) {

    int nrOfMatchesRequired =
      (int) Arrays.stream(pointsToMatch)
        .filter(point -> point != -1)
        .count();
    int nrOfMatches =
      (int) IntStream.range(0, nrOfMatchesRequired)
        .filter(position -> pointsToMatch[position] == movePoints[position])
        .count();

    return nrOfMatches == nrOfMatchesRequired;
  }

  public void setMovePoints(int[] newMovePoints) {

    movePoints = newMovePoints.clone();
  }

  boolean isWinningMove () {

    return rearPos == 0;
  }

  public boolean hasHitOnFirst(int endingPoint) {

    return
      movePoints[3] == endingPoint &&
      hitPoints[1] != -1 &&
      movePoints[1] == movePoints[2];
  }
  
  public List<MoveLayout> getMovePointLayouts() {

    if (movePointLayouts == null) {
      movePointLayouts =
        new MovePointLayouts(this).getMoveLayoutsList();
      movePointLayouts.forEach(
        moveLayout -> moveLayout.movePointsBackup = getMovePoints()
      );
    }
    return movePointLayouts;
  }

  public String getMovePointsString() {

    return
      stream(movePoints)
        .mapToObj(Integer::toString)
        .collect(joining(","));
  }
  
  public List<int[]> dicePatterns () {
    
    if (dicePatterns == null) {
      dicePatterns = getMovePointLayouts()
        .stream()
        .map(UsedDicePattern::new)
        .map(UsedDicePattern::dicePattern)
        .toList();
    }
    return
      dicePatterns;
  }

  public String MoveNotation() {
    
    if (annotation == null) {
      annotation = new MoveNotation(this).notation();
    }
    return annotation;
  }
  
  public void printMovePoints () {

    System.out.println("["+getMovePointsString()+"]");
  }
  
}
