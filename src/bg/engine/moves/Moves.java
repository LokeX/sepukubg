package bg.engine.moves;

import bg.engine.Dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Moves {

  List<EvaluatedMove> evaluatedMoves;
  private SearchMoves searchMoves;
  private List<MoveLayout> legalMoves;
  private MoveLayout parentMove;
  private Dice dice;
  private int nrOfLegalPartMoves;

  public Moves(Moves moves) {

    parentMove = moves.parentMove;
    dice = new Dice(moves.dice);
    legalMoves = moves.legalMoves;
    evaluatedMoves = moves.evaluatedMoves;
  }

  public Moves() {

  }

  public Layout getParentLayout() {

    return new Layout(parentMove);
  }

  public SearchMoves getSearchMoves () {

    return searchMoves;
  }

  public SearchMoves searchMoves(int nrOfMovesToSearch, int plyDepth) {

    searchMoves = new SearchMoves(nrOfMovesToSearch, plyDepth, this);

    return searchMoves;
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

  void setEvaluatedMoves(List<EvaluatedMove> list) {

    evaluatedMoves = list;
  }

  public List<int[]> getLegalMovePoints () {

    return
      Collections.unmodifiableList(
        legalMoves.stream().
        map(MoveLayout::getMovePoints).
        collect(toList())
      );
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

  private void partMove (int dieNr, int[] dieFaces, MoveLayout moveLayout, List<Integer> moveablePoints) {

    List<Integer> nextMoveablePoints;
    MoveLayout nextMoveLayout;
    int nextDieNr = dieNr+1;
    boolean nextPartMoveOK = false;

    for (int a = 0; a < moveablePoints.size(); a++) {
      nextMoveLayout = moveLayout.getPartMoveLayout(dieNr, dieFaces, moveablePoints.get(a));
      if (nextDieNr < dieFaces.length) {
        nextMoveablePoints = nextMoveLayout.getMoveablePoints(dieFaces[nextDieNr]);
        nextPartMoveOK = nextMoveablePoints.size() > 0;
        if (nextPartMoveOK) {
          partMove(nextDieNr, dieFaces, nextMoveLayout, nextMoveablePoints);
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

    final int lowPIP = legalMoves.stream().
      mapToInt(Layout::getPip).min().getAsInt();

    legalMoves = legalMoves.stream().
      filter(legalMove -> legalMove.getPip() <= lowPIP).
      collect(toList());
  }

  private void generateEvaluatedMoves () {

    legalMoves.forEach(legalMove -> {
      if (legalMove.notIn(evaluatedMoves)) {
        evaluatedMoves.add(new EvaluatedMove(legalMove));
      }
    });
  }

  public Moves sortEvaluatedMoves () {

    evaluatedMoves.
      sort((a, b) -> b.getLayoutStrength() - a.getLayoutStrength());
    return this;
  }

  private void generateLegalMoves () {

    for (int a = 0; a < (dice.areDouble() ? 1 : 2); a++) {

      int[] dieFaces = a == 0 ? dice.getDice() : dice.getSwappedDice();
      List<Integer> moveablePoints = parentMove.getMoveablePoints(dieFaces[0]);

      if (!moveablePoints.isEmpty()) {
        partMove(0, dieFaces, parentMove, moveablePoints);
      }
    }
  }

  public Moves generateMoves (Layout layout, int[] diceToMove) {

    parentMove = new MoveLayout(layout, diceToMove);
    dice = new Dice(diceToMove);
    legalMoves = new ArrayList<>();
    evaluatedMoves = new ArrayList<>();
    generateLegalMoves();
    if (legalMoves.isEmpty()) {
      evaluatedMoves.add(new EvaluatedMove(parentMove));
    } else {
      if (!dice.areDouble() && nrOfLegalPartMoves == 1) {
        removeHighPipMoves();
      }
      generateEvaluatedMoves();
      sortEvaluatedMoves();
    }
    return this;
  }

}
