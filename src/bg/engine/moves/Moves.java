package bg.engine.moves;

import bg.engine.Dice;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Moves {

  private SearchMoves searchMoves;
  private List<EvaluatedMove> evaluatedMoves;
  private List<MoveLayout> legalMoves;
  private Layout parentLayout;
  private Dice dice;
  private int nrOfLegalPartMoves;

  public Moves(Moves moves) {

    parentLayout = new Layout(moves.parentLayout);
    dice = new Dice(moves.dice);
    legalMoves = moves.legalMoves;
    evaluatedMoves = moves.evaluatedMoves;
  }

  public Moves() {

  }

  public Layout getParentLayout() {

    return parentLayout;
  }

  public SearchMoves getSearchMoves () {

    return searchMoves;
  }

  public SearchMoves searchMoves(int nrOfMovesToSearch, int plyDepth) {

    searchMoves = new SearchMoves(nrOfMovesToSearch, plyDepth, this);

    return searchMoves;
  }

  public int getMoveNr (EvaluatedMove evaluatedMove) {

    return evaluatedMoves.indexOf(evaluatedMove);
  }

  public List<MoveLayout> getLegalMoves() {

    return legalMoves.stream().collect(toList());
  }

  public List<EvaluatedMove> getEvaluatedMoves() {

    return evaluatedMoves.stream().collect(toList());
  }

  public void setEvaluatedMoves(List<EvaluatedMove> list) {

    evaluatedMoves = list;
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

  public EvaluatedMove getEvaluatedMove (int evaluatedMoveNr) {

    return evaluatedMoves.get(evaluatedMoveNr);
  }

  public EvaluatedMove getBestMove() {

    return evaluatedMoves.get(0);
  }

  public int getNrOfMoves () {

    return evaluatedMoves.size();
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

  private void partMove (int dieNr, int[] dieFaces, MoveLayout moveLayout, List<Integer> moveablePointsList) {

    List<Integer> nextMoveablePointsList;
    MoveLayout nextMoveLayout;
    boolean nextPartMoveOK = false;

    for (int a = 0; a < moveablePointsList.size(); a++) {
      nextMoveLayout = moveLayout.getPartMoveLayout(dieNr, dieFaces, moveablePointsList.get(a));
      if (dieNr+1 < dieFaces.length) {
        nextMoveablePointsList = nextMoveLayout.getMoveablePointsList(dieFaces[dieNr+1]);
        nextPartMoveOK = nextMoveablePointsList.size() > 0;
        if (nextPartMoveOK) {
          partMove(dieNr+1, dieFaces, nextMoveLayout, nextMoveablePointsList);
        }
      }
      if (!nextPartMoveOK) {
        int nrOfPartMoves = nextMoveLayout.getNrOfPartMoves();

        if (nrOfPartMoves >= nrOfLegalPartMoves) {
          if (nrOfPartMoves > nrOfLegalPartMoves) {
            nrOfLegalPartMoves = nrOfPartMoves;
            legalMoves.clear();
          }
          legalMoves.add(nextMoveLayout);
        }
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

  public Moves generateMoves (Layout layout, int[] useDice) {

    MoveLayout moveLayout = new MoveLayout(layout, useDice);
    int[] dieFaces;

    dice = new Dice(useDice);
    parentLayout = new Layout(layout);
    legalMoves = new ArrayList<>();
    evaluatedMoves = new ArrayList<>();
    for (int a = 0; a < (dice.areDouble() ? 1 : 2); a++) {
      dieFaces = a == 0 ? useDice : dice.getSwappedDice();
      List<Integer> moveablePointsList = moveLayout.getMoveablePointsList(dieFaces[0]);

      if (!moveablePointsList.isEmpty()) {
        partMove(0, dieFaces, moveLayout, moveablePointsList);
      }
    }
    if (legalMoves.isEmpty()) {
      evaluatedMoves.add(new EvaluatedMove(moveLayout));
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
