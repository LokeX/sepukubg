package bg.engine.coreLogic.moves;

import bg.engine.coreLogic.Dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Moves {

  private List<EvaluatedMove> evaluatedMoves;
  private List<MoveLayout> legalMoves;
  private List<MoveBonuses> moveBonuses;
  private SearchEvaluation searchEvaluation;
  private MoveLayout parentMoveLayout;
  private Dice dice;
  private int nrOfLegalPartMoves;

  public Moves (Moves moves) {

    parentMoveLayout = moves.parentMoveLayout;
    dice = moves.dice;
    legalMoves = moves.legalMoves;
    evaluatedMoves = moves.evaluatedMoves;
    nrOfLegalPartMoves = moves.nrOfLegalPartMoves;
  }

  Moves () {

  }

  public MoveBonuses getMoveBonuses (EvaluatedMove move) {

    if (moveBonuses == null) {
      generateMoveBonuses();
    }
    return moveBonuses.get(getLayoutNr(move));
  }

  public boolean noLegalMove () {

    return evaluatedMoves.get(0).isIllegal();
  }

  public int getPlayerID () {

    return parentMoveLayout.playerID;
  }

  public MoveLayout getParentMoveLayout() {

    return new MoveLayout(parentMoveLayout);
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
      moveBonuses = null;
      searchEvaluation.sortEvaluatedMoves();
    }
  }

  public SearchEvaluation getSearchEvaluation () {

    return searchEvaluation;
  }

  private int getLayoutNr(EvaluatedMove evaluatedMove) {

    return evaluatedMoves.indexOf(evaluatedMove);
  }

  protected List<MoveLayout> getLegalMoves() {

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

  public EvaluatedMove getBestMove() {

    return evaluatedMoves.get(0);
  }

  public int getNrOfMoves () {

    return evaluatedMoves.size();
  }

  protected int getNrOfLegalPartMoves () {

    return nrOfLegalPartMoves;
  }

  Dice getDiceObj () {

    return dice;
  }

  public int[] getDice () {

    return dice.getDice();
  }

  public void setEvaluatedMove (

    MoveLayout moveLayout) {

    evaluatedMoves.set(
      getLayoutNr(moveLayout),
      new EvaluatedMove(moveLayout)
    );
  }

  protected int getLayoutNr (Layout layout) {

    if (layout != null) {
      return
        IntStream.range(0, evaluatedMoves.size())
          .filter(index ->
            evaluatedMoves.get(index)
              .isIdenticalTo(layout)
          )
          .findAny()
          .orElse(-1);
    } else {
      return -1;
    }
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

    moveBonuses = null;
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

      int[] dieFaces =
        a == 0
          ? dice.getDice()
          : dice.getSwappedDice();
      List<Integer> moveablePoints =
        parentMoveLayout
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

  private void generateMoveBonuses () {

    moveBonuses =
      evaluatedMoves.stream()
        .map(MoveBonuses::new)
        .collect(toList());
  }

  protected Moves generateMoves (Layout layout, int[] diceToMove) {

    nrOfLegalPartMoves = 0;
    dice = new Dice(diceToMove);
    parentMoveLayout = new MoveLayout(this, layout);
    legalMoves = new ArrayList<>(100);
    evaluatedMoves = new ArrayList<>(100);
    generateLegalMoves();
    generateEvaluatedMoves();
    sortEvaluatedMoves();
    return this;
  }

}
