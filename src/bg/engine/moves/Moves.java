package bg.engine.moves;

import bg.engine.Dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.List.*;
import static java.util.stream.Collectors.toList;

public class Moves {

  private List<EvaluatedMove> evaluatedMoves;
  private List<MoveLayout> legalMoves;
  private SearchEvaluation searchEvaluation;
  private MoveLayout parentMoveLayout;
  private Dice dice;
  private int nrOfLegalPartMoves;
  private int playerID;

  public Moves(Moves moves) {

    parentMoveLayout = moves.parentMoveLayout;
    dice = moves.dice;
    legalMoves = moves.legalMoves;
    evaluatedMoves = moves.evaluatedMoves;
    nrOfLegalPartMoves = moves.nrOfLegalPartMoves;
  }

  public Moves() {

  }

  public int getPlayerID () {

    return playerID;
  }

  public Layout getParentLayout() {

    return new Layout(parentMoveLayout);
  }

  public Moves generateSearchEvaluations (int nrOfMovesToSearch, int plyDepth) {

    searchEvaluation = new SearchEvaluation()
      .generateSearchEvaluations(
        nrOfMovesToSearch,
        plyDepth,
        evaluatedMoves
    );
    return this;
  }

  public void sortMovesBySearchEvaluation () {

    if (searchEvaluation != null) {
      searchEvaluation.sortEvaluatedMoves();
    }
  }

  public SearchEvaluation getSearchEvaluation () {

    return searchEvaluation;
  }

  protected int getMoveNr (EvaluatedMove evaluatedMove) {

    return evaluatedMoves.indexOf(evaluatedMove);
  }

  public List<MoveLayout> getLegalMoves() {

    return Collections.unmodifiableList(legalMoves);
  }

  public List<EvaluatedMove> getEvaluatedMoves() {

    return Collections.unmodifiableList(evaluatedMoves);
  }

  public List<int[]> getLegalMovePoints () {

    return legalMoves.stream().
      map(MoveLayout::getMovePoints).
      collect(toList());
  }

  public Layout getLayout (int layoutNr) {

    return new Layout(evaluatedMoves.get(layoutNr));
  }

  public MoveLayout getMoveLayout (int moveLayoutNr) {

    return new MoveLayout(evaluatedMoves.get(moveLayoutNr));
  }

  protected EvaluatedMove getEvaluatedMove (int evaluatedMoveNr) {

    return evaluatedMoves.get(evaluatedMoveNr);
  }

  protected EvaluatedMove getBestMove() {

    return evaluatedMoves.get(0);
  }

  public int getNrOfMoves () {

    return evaluatedMoves.size();
  }

  public int getNrOfLegalPartMoves () {

    return nrOfLegalPartMoves;
  }

  public int[] getDice () {

    return dice.getDice();
  }

//  public MovePoints getMovesAnalysis () {
//
//    return new MovePoints(this);
//  }

  public MovePointsInput getMovePointsInput () {

    return new MovePointsInput(this);
  }

  public int getMoveNr (Layout layout) {

    if (layout != null) {
      for (int a = 0; a < evaluatedMoves.size(); a++) {
        if (layout.isIdenticalTo(evaluatedMoves.get(a))) {
          return a;
        }
      }
    }
    return -1;
  }

  public void printEvaluatedMoves() {

    System.out.println("Moves:");
    for (int a = 0; a < getEvaluatedMoves().size(); a++) {
      System.out.print("["+a+"]: ");
      System.out.print(": "+ getEvaluatedMoves().get(a).getLayoutStrength()+": ");
      for (int b = 0; b < getEvaluatedMoves().get(a).getMovePoints().length; b++) {
    	System.out.print(getEvaluatedMoves().get(a).getMovePoints()[b]+",");
      }
      System.out.println();
    }
  }

  public void printLegalMoves () {

    legalMoves.forEach(move -> {
      move.printMovePoints();
      System.out.println();
    });
  }

  public void printDice (int[] dice) {

    System.out.print("Dice: ");
    for (int d : dice) {
      System.out.print(d+",");
    }
    System.out.println();
  }

  private void partMove (

    int dieNr, int[] dieFaces,
    MoveLayout moveLayout,
    List<Integer> moveablePoints) {

    List<Integer> nextMoveablePoints;
    MoveLayout nextMoveLayout;
    int nextDieNr = dieNr+1;
    boolean nextPartMoveOK = false;

    for (Integer startingPoint : moveablePoints) {
      nextMoveLayout = moveLayout
        .getPartMoveLayout(dieNr, dieFaces, startingPoint);
      if (nextDieNr < dieFaces.length) {
        nextMoveablePoints = nextMoveLayout
          .getMoveablePoints(dieFaces[nextDieNr]);
        nextPartMoveOK = !nextMoveablePoints.isEmpty();
        if (nextPartMoveOK) {
          partMove(
            nextDieNr,
            dieFaces,
            nextMoveLayout,
            nextMoveablePoints
          );
        }
      }
      if (!nextPartMoveOK && nextDieNr >= nrOfLegalPartMoves) {
        if (nextDieNr > nrOfLegalPartMoves) {
          nrOfLegalPartMoves = nextDieNr;
          if (!legalMoves.isEmpty()) {
            legalMoves.clear();
          }
        }
        legalMoves.add(nextMoveLayout);
      }
    }
  }

  private void removeHighPipMoves () {

    final int lowPip
      = legalMoves.stream()
        .mapToInt(Layout::getPip)
        .min().getAsInt();

    legalMoves
      = legalMoves.stream()
        .filter(legalMove -> legalMove.getPip() <= lowPip)
        .collect(toList());
  }

  private void generateEvaluatedMoves () {

    if (legalMoves.isEmpty()) {
      evaluatedMoves.add(new EvaluatedMove(parentMoveLayout));
    } else {
      legalMoves.forEach(legalMove -> {
        if (legalMove.notIn(evaluatedMoves)) {
          evaluatedMoves.add(new EvaluatedMove(legalMove));
        }
      });
    }
  }

  public Moves sortEvaluatedMoves () {

    evaluatedMoves.
      sort((a, b) -> b.getLayoutStrength() - a.getLayoutStrength());
    return this;
  }

  private boolean possibleHighPipMove () {

    return
      legalMoves.size() > 1
        && nrOfLegalPartMoves == 1
        && !dice.areDouble();
  }

  private void generateLegalMoves () {

    for (int a = 0; a < (dice.areDouble() ? 1 : 2); a++) {

      int[] dieFaces
        = a == 0
        ? dice.getDice()
        : dice.getSwappedDice();
      List<Integer> moveablePoints
        = parentMoveLayout
        .getMoveablePoints(dieFaces[0]);

      if (!moveablePoints.isEmpty()) {
        partMove(
          0,
          dieFaces,
          parentMoveLayout,
          moveablePoints
        );
      }
    }
    if (possibleHighPipMove()) {
      removeHighPipMoves();
    }
  }

  public Moves generateMoves (Layout layout, int[] diceToMove) {

    parentMoveLayout = new MoveLayout(layout, diceToMove);
    dice = new Dice(diceToMove);
    playerID = layout.playerID;
    legalMoves = new ArrayList<>(100);
    evaluatedMoves = new ArrayList<>(100);
    generateLegalMoves();
    generateEvaluatedMoves();
    sortEvaluatedMoves();
    return this;
  }

}
