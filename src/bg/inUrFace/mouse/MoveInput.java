package bg.inUrFace.mouse;

import bg.engine.moves.Layout;
import bg.engine.moves.EvaluatedMove;
import bg.Main;
import static bg.Main.matchApi;
import static bg.Main.settings;
import static bg.Main.win;

import bg.engine.moves.MovePointsInput;
import bg.inUrFace.canvas.BoardDim;
import bg.inUrFace.canvas.move.MoveOutput;
import bg.util.Batch;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MoveInput {

  public MoveInput() {

    calculatePoints();
    for (int a = 0; a < movePoints.length; a++) {
      movePoints[a] = -1;
    }
    if (playerID == 1) {
      customMoveLayout.flipLayout();
    }
    outputMove();
  }

//  private MovePoints movesAnalysis = matchApi.getMovesAnalysis();
  private MovePointsInput movePointsInput = matchApi.getMovePointsInput();
  private Layout customMoveLayout = new Layout(Main.matchApi.getSelectedTurn().getParentLayout());
  private List<int[]> legalMovePoints = Main.matchApi.getSelectedTurn().getLegalMovePoints();
  private Batch[] points = new Batch[26];
  private int[] movePoints = new int[matchApi.getSelectedTurn().getDice().length*2];
  private int playerID = Main.matchApi.getSelectedTurn().getPlayerOnRollsID();
  private List<Integer> hits = new ArrayList();
  private Integer[] legalEndingPoints;
  private int inputPoint = 0;

  final public void calculatePoints () {

    BoardDim d = win.canvas.getDimensions();
    Batch[] regularPoints = MouseController.getRegularPoints();
    Color pointsColor = new Color(56, 75, 174, 150);

    if (playerID == 1) {
      points[0] = new Batch(
        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth,
        d.frameOffsetY+d.boardInnerHeight/2,
        d.barWidth,
        d.boardInnerHeight/2
      );
    } else {
      points[0] = new Batch(
        d.bottomRightBearOffOffsetX,
        d.bottomRightBearOffOffsetY,
        d.chequerSize,
        d.bearOffHeight
      );
    }
    points[0].setComponent(win.canvas);
    points[0].setBackgroundColor(pointsColor);
    for (int a = 0; a < regularPoints.length; a++) {
      points[a+1] = regularPoints[a];
      points[a+1].setBackgroundColor(pointsColor);
    }
    if (playerID == 1) {
      points[25] = new Batch(
        d.topRightBearOffOffsetX,
        d.topRightBearOffOffsetY,
        d.chequerSize,
        d.bearOffHeight
      );
    } else {
      points[25] = new Batch(
        d.leftPlayAreaOffsetX+d.leftPlayAreaWidth,
        d.frameOffsetY,
        d.barWidth,
        d.boardInnerHeight/2
      );
    }
    points[25].setBackgroundColor(pointsColor);
    points[25].setComponent(win.canvas);
  }

  public final void outputMove() {

    new MoveOutput().outputCustomMove(this);
  }

  public int[] getMovePoints() {

    return movePoints;
  }

  public Layout getCustomMoveLayout () {

    return customMoveLayout;
  }

  public int getNrOfLegalPartMoves() {

    int count = 0;

    for (int a = 0; a < movePoints.length; a++) {
      if (movePoints[a] >= 0) {
        count++;
      } else {
        break;
      }
    }
    return count / 2;
  }

  public Layout getMouseLayout() {

    return customMoveLayout;
  }

  private boolean isLegalEndingPoint(int clickedPoint) {

    if (legalEndingPoints != null) {
      for (Integer legalEndingPoint : legalEndingPoints) {
        if (legalEndingPoint == clickedPoint) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean inputPointIsLegalStartingPointFor(int inputPoint, int startingPoint) {

    if (customMoveLayout.getPoint()[startingPoint + (playerID == 1 ? 26 : 0)] > 0 && (inputPoint + 2) % 2 == 0) {
      if (getLegalEndingPointsFor(movePoints, inputPoint, startingPoint).length > 0) {
        for (int a = 0; a < legalMovePoints.size(); a++) {
          if (legalMovePoints.get(a)[inputPoint] == startingPoint) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void setMoveInputAsSelectedTurnsSelectedLegalMove() {

    List<EvaluatedMove> moves = matchApi.getSelectedTurn().getEvaluatedMoves();
    int nrOfLegalMovePoints = getNrOfLegalMovePoints(movePoints);
    int[] partMoves = new int[nrOfLegalMovePoints - (nrOfLegalMovePoints % 2)];
    int completeMove = getCompleteMove();

    for (int a = 0; a < partMoves.length; a++) {
      partMoves[a] = movePoints[a];
    }
    if (settings.isAutoCompletePartMoves() && completeMove >= 0 && inputPoint < movePoints.length-1) {
      inputPoint = autoSetPoints(legalMovePoints.get(completeMove),
        getNrOfLegalMovePoints(legalMovePoints.get(completeMove))-1);
    }
    printMove(movePoints);
    customMoveLayout.generateHashCode();
    for (int a = 0; a < moves.size(); a++) {
      if (moves.get(a).isIdenticalTo(customMoveLayout)) {
//        if (partMoves.length == movePoints.length || moves.get(a).movePointsMatch(partMoves)) {
        if (partMoves.length == movePoints.length || moves.get(a).containsThesePartMoves(partMoves)) {
          System.out.println("Selecting move nr: "+a);
          matchApi.setSelectedMove(a);
          matchApi.getSelectedMove().printMovePoints();
          matchApi.getSelectedTurn().getPlayedMove().setMovePoints(movePoints);
          break;
        }
      }
    }
  }

  private void setStartingPoint(int startingPoint) {

    if ((inputPoint + 2) % 2 != 0) {
      customMoveLayout.point[movePoints[--inputPoint] + (playerID == 1 ? 26 : 0)]++;
    }
    movePoints[inputPoint] = startingPoint;
    customMoveLayout.point[startingPoint + (playerID == 1 ? 26 : 0)]--;
    legalEndingPoints = getLegalEndingPoints(startingPoint, inputPoint);
    inputPoint++;
    if (legalEndingPoints.length == 2 && legalEndingPoints[0] < 0) {
      setEndingPoint(legalEndingPoints[1]);
    } else if (legalEndingPoints.length == 1) {
      setEndingPoint(legalEndingPoints[0]);
    }
  }

  private int getListPosition(int endingPoint) {

    for (int a = 0; a < legalEndingPoints.length; a++) {
      if (legalEndingPoints[a] == endingPoint) {
        return a;
      }
    }
    return -1;
  }

  private void handleHit(int endingPoint) {

    if (customMoveLayout.point[endingPoint + (playerID == 1 ? 0 : 26)] == 1) {
      customMoveLayout.point[endingPoint + (playerID == 1 ? 0 : 26)] = 0;
      customMoveLayout.point[playerID == 1 ? 25 : 26]++;
      hits.add(endingPoint);
    }
  }

  public void printMove(int[] movePoints) {

    System.out.print("Moves: ");
    for (int a = 0; a < movePoints.length; a++) {
      System.out.print(movePoints[a] + ",");
    }
    System.out.println();
  }

  private int autoSetPoints (int[] movepoints, int endPoint) {

    int storedInputPoint = inputPoint;

    while (inputPoint <= endPoint && inputPoint < movePoints.length-1) {
      if ((inputPoint+2)%2 == 0) {
        setStartingPoint(movepoints[inputPoint]);
      } else {
        if (storedInputPoint == inputPoint) {
          legalEndingPoints = getLegalEndingPoints(movepoints[inputPoint-1], inputPoint-1);
        }
        setEndingPoint(movepoints[inputPoint]);
      }
    }
    return storedInputPoint;
  }

  private int identicalPointsCount () {

    int identicalPointsCount = 0;
    int matches;

    for (int a = 0; a < movePoints.length; a++) {
      matches = 0;
      for (int b = 0; b < legalMovePoints.size()-1; b++) {
        if (legalMovePoints.get(b)[a] == legalMovePoints.get(b+1)[a]) {
          matches++;
        }
      }
      if (matches == legalMovePoints.size()-1) {
        identicalPointsCount++;
      } else {
        break;
      }
    }
    return identicalPointsCount;
  }

  public void initialAutoMove (Object notifier) {

    int identicalPointsCount = identicalPointsCount();

    if (identicalPointsCount > 1) {

      int endPoint = identicalPointsCount-((identicalPointsCount+2)%2)-1;
      int storedInputPoint = autoSetPoints(matchApi.getSelectedMove().getMovePoints(), endPoint);

      if (endPoint < movePoints.length) {
        new MoveOutput(matchApi.getSelectedMove()).showMove(storedInputPoint, endPoint, notifier);
      }
    }
  }

  private void setEndingPoint(int endingPoint) {

    if (movePoints.length == 4 && inputPoint == 1 && getListPosition(endingPoint) == 2) {
      movePoints[1] = legalEndingPoints[1];
      movePoints[2] = legalEndingPoints[1];
      movePoints[3] = legalEndingPoints[2];
      handleHit(endingPoint);
      inputPoint = movePoints.length - 1;
    } else if (movePoints.length == 8) {

      int endingPointsCount = 0;
      int endPoint = inputPoint + ((getListPosition(endingPoint) + 1) * 2) - 2;

      for (int a = inputPoint; a <= endPoint; a += 2) {
        handleHit(legalEndingPoints[endingPointsCount]);
        movePoints[a] = legalEndingPoints[endingPointsCount++];
        if (a > inputPoint) {
          movePoints[a - 1] = movePoints[a - 2];
        }
      }
      inputPoint = endPoint;
    } else {
      handleHit(endingPoint);
    }
    movePoints[inputPoint++] = endingPoint;
    if (playerID == 1 && endingPoint == 25) {
      customMoveLayout.point[51]++;
    } else {
      customMoveLayout.point[endingPoint + (playerID == 1 ? 26 : 0)]++;
    }
    legalEndingPoints = null;
  }

  private int getNrOfLegalMovePoints(int[] movePoints) {

    int count = 0;

    for (int a = 0; a < movePoints.length; a++) {
      if (movePoints[a] >= 0) {
        count++;
      } else {
        break;
      }
    }
    return count;
  }

  private int getCompleteMove() {

    int nrOfMovesFound = 0;
    int match = -1;
    int nrOfLegalMovePoints = getNrOfLegalMovePoints(movePoints);
    int nrOfPointMatches;

    for (int a = 0; a < legalMovePoints.size(); a++) {
      nrOfPointMatches = 0;
      for (int b = 0; b < nrOfLegalMovePoints; b++) {
        if (movePoints[b] == legalMovePoints.get(a)[b]) {
          nrOfPointMatches++;
        }
      }
      if (nrOfPointMatches == nrOfLegalMovePoints) {
        nrOfMovesFound++;
        match = a;
      }
    }
    return nrOfMovesFound == 1 ? match : -1;
  }

  public boolean endOfInputReached () {

    return inputPoint == movePoints.length || inputPoint/2 == matchApi.getSelectedMove().getNrOfPartMoves();
  }

  public boolean moveAutoCompletion () {

    return (settings.isAutoCompletePartMoves() && getCompleteMove() >= 0);
  }

  private void handlePointInputAftermath() {

    Main.sound.playSoundEffect("Blop-Mark_DiAngelo");
    if (moveAutoCompletion() || endOfInputReached()) {
      if (playerID == 1) {
        customMoveLayout.flipLayout();
      }
      setMoveInputAsSelectedTurnsSelectedLegalMove();
      if (inputPoint < matchApi.getSelectedMove().getNrOfPartMoves()*2) {
        matchApi.showMove(inputPoint);
        inputPoint = matchApi.getSelectedMove().getNrOfPartMoves()*2;
      } else {
        new MoveOutput(matchApi.getSelectedMove()).outputMove();
        matchApi.endTurn();
      }
    } else {
      outputMove();
    }
  }

  private boolean isLegalStartingPoint (int clickedPoint) {

    return
      inputPointIsLegalStartingPointFor(inputPoint, clickedPoint ) ||
      (inputPointIsLegalStartingPointFor(inputPoint - 1, clickedPoint));
  }

  public void pointClicked() {

    int clickedPoint = getClickedPoint();

    movePointsInput.input(clickedPoint);
    if (clickedPoint >= 0 && !endOfInputReached()) {
      if (!isLegalEndingPoint(clickedPoint)) {
        if (isLegalStartingPoint(clickedPoint))  {
          setStartingPoint(clickedPoint);
          handlePointInputAftermath();
        }
      } else {
        setEndingPoint(clickedPoint);
        handlePointInputAftermath();
      }
    }
  }

  private boolean equals(int[] partMoves, int[] movePoints, int inputPoint) {

    for (int a = 0; a <= inputPoint; a++) {
      if (partMoves[a] != movePoints[a]) {
        return false;
      }
    }
    return true;
  }

  private boolean isMember(int b, List<Integer> endingPoints) {

    for (int a = 0; a < endingPoints.size(); a++) {
      if (endingPoints.get(a) == b) {
        return true;
      }
    }
    return false;
  }

  private boolean noHits(Integer[] endingPoints) {

    for (Integer endingPoint : endingPoints) {
      if (customMoveLayout.point[endingPoint + (playerID == 0 ? 26 : 0)] != 0) {
        return false;
      }
    }
    return true;
  }

  private Integer[] getLegalEndingPointsFor(int[] movePoints, int inputPoint, int startingPoint) {

    List<Integer> endingPoints = new ArrayList();
    int[] tempMove = movePoints.clone();

    tempMove[inputPoint] = startingPoint;
    for (int a = 0; a < legalMovePoints.size(); a++) {
      if (equals(legalMovePoints.get(a), tempMove, inputPoint) && !isMember(legalMovePoints.get(a)[inputPoint + 1], endingPoints)) {
        endingPoints.add(legalMovePoints.get(a)[inputPoint + 1]);
      }
    }
    return endingPoints.toArray(new Integer[endingPoints.size()]);
  }

  public Integer[] getLegalEndingPoints(int startingPoint, int inputPoint) {

    List<Integer> endingPoints = new ArrayList();
    int[] tempMovePoints = movePoints.clone();
    Integer[] tempEndingPoints;

    tempMovePoints[inputPoint] = startingPoint;
    if (movePoints.length == 4) {
      tempEndingPoints = getLegalEndingPointsFor(tempMovePoints, inputPoint, tempMovePoints[inputPoint]);
      if (tempEndingPoints.length > 0) {
        if (tempEndingPoints.length == 1) {
          endingPoints.add(-1);
        }
        for (Integer tempEndingPoint : tempEndingPoints) {
          endingPoints.add(tempEndingPoint);
        }
        if (inputPoint == 0 && noHits(tempEndingPoints)) {
          tempMovePoints[inputPoint + 1] = endingPoints.get(endingPoints.size() - 1);
          tempEndingPoints = getLegalEndingPointsFor(
            tempMovePoints,
            inputPoint + 2,
            tempMovePoints[inputPoint + 1]
          );
          if (tempEndingPoints.length > 0) {
            endingPoints.add(tempEndingPoints[0]);
          }
        }
      }
    } else {

      int count = inputPoint;

      tempEndingPoints = getLegalEndingPointsFor(tempMovePoints, inputPoint, tempMovePoints[inputPoint]);
      while (tempEndingPoints.length > 0 && count < tempMovePoints.length - 1) {
        endingPoints.add(tempEndingPoints[0]);
        tempMovePoints[++count] = endingPoints.get(endingPoints.size() - 1);
        if (count < tempMovePoints.length - 1) {
          tempMovePoints[count + 1] = tempMovePoints[count];
          tempEndingPoints = getLegalEndingPointsFor(tempMovePoints, ++count, tempMovePoints[count]);
        }
      }
    }
    return endingPoints.toArray(new Integer[endingPoints.size()]);
  }

  private int getClickedPoint() {

    for (int a = 0; a < points.length; a++) {
      if (points[a].mouseOnBatch()) {
//        System.out.println("point clicked: "+a);
        return a;
      }
    }
    return -1;
  }

  public void undoPointSelection() {

    movePointsInput.deleteLatestInput();
    if (inputPoint > 0) {
//      mouse.actionButton.setShowButton(false);
      if ((inputPoint + 2) % 2 == 1) {
        customMoveLayout.point[movePoints[--inputPoint] + (playerID == 1 ? 26 : 0)]++;
        legalEndingPoints = null;
      } else {
        customMoveLayout.point[movePoints[--inputPoint] + (playerID == 1 ? 26 : 0)]--;
        if (hits.size() > 0 && movePoints[inputPoint] == hits.get(hits.size() - 1)) {
          customMoveLayout.point[hits.remove(hits.size() - 1) + (playerID == 0 ? 26 : 0)]++;
          customMoveLayout.point[(playerID == 0 ? 26 : 25)]--;
        }
        legalEndingPoints = getLegalEndingPoints(movePoints[inputPoint-1], inputPoint-1);
      }
      movePoints[inputPoint] = -1;
      outputMove();
    }
  }

  public Batch[] getPoints () {

    return points;
  }

  public Integer[] getEndingPoints () {

    return legalEndingPoints;
  }

  public int getInputPoint () {

    return inputPoint;
  }

  public int getPlayerID () {

    return playerID;
  }

}
