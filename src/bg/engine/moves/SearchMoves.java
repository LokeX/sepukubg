package bg.engine.moves;

import java.util.List;
import static java.util.stream.Collectors.toList;

public class SearchMoves {

  private String report = "";
  private Moves moves;
  private List<EvaluatedMove> evaluatedMoves;
  private int nrOfMoves;
  private int nrOfTurns;
  private long startTime;

  public SearchMoves (int nrOfMoves, int nrOfTurns, Moves moves) {

    evaluatedMoves = moves.getModifiableEvaluatedMoves();
    this.moves = moves;
    this.nrOfMoves = nrOfMoves;
    this.nrOfTurns = nrOfTurns;
  }

  public SearchMoves setSearchEvaluations () {

    startTime = System.currentTimeMillis();
    evaluatedMoves.parallelStream().limit(nrOfMoves).forEach(move -> {
      move.setSearchEvaluation(
        move.getLayoutStrength() - getTurnStrengthAverage(1, move)
      );
    });
    report += "Before values:\n";
    appendReportValues();
    return this;
  }

  private int getTurnStrengthAverage(int turnCount, EvaluatedMove move) {

    List<Moves> searchMoves = move.getSearchMoves();
    int turnsStrengthAverage = getMovesStrengthAverage(searchMoves);
    int nextTurnsStrengthAverage = 0;

    if (turnCount < nrOfTurns && !move.isWinningMove()) {
      nextTurnsStrengthAverage = getNextTurnsStrengthAverage(turnCount, searchMoves);
    } else {
      nrOfTurns = turnCount;
    }
    return turnsStrengthAverage - nextTurnsStrengthAverage;
  }

  public int getMovesStrengthAverage(List<Moves> moves) {

    return moves.stream().map(Moves::getBestMove).
      mapToInt(EvaluatedMove::getProbabilityAdjustedLayoutStrength).sum()/36;
  }

  private int getNextTurnsStrengthAverage(int turnCount, List<Moves> moves) {

    return (int)moves.parallelStream().map(Moves::getBestMove).
      mapToInt(bestMove -> getTurnStrengthAverage(turnCount+1, bestMove)).
      average().getAsDouble();
  }

  public SearchMoves sort () {

    printSearchReport();

    List<EvaluatedMove> sortedMoves = evaluatedMoves.stream().
      limit(nrOfMoves).
      sorted((a, b) -> b.searchEvaluation - a.searchEvaluation).
      collect(toList());

    for (int a = 0; a < sortedMoves.size(); a++) {
      evaluatedMoves.set(a, sortedMoves.get(a));
    }
    report += "After values:\n";
    appendReportValues();
    appendTime();
    printSearchReport();
    return this;
  }

  public SearchMoves applyToMoves() {

    moves.setEvaluatedMoves(evaluatedMoves);

    return this;
  }

  private void appendTime () {

    report +=
      "Time in milliseconds: "+
      (System.currentTimeMillis()- startTime)+
      "\n";
  }

  private void appendReportValues () {

    report += evaluatedMoves.stream().
      limit(nrOfMoves).
      map(move ->
        move.getMovePointsString()+": "+
        move.getSearchEvaluation()+"\n"
      ).reduce(String::concat);
  }

  public String getReport () {

    return report;
  }

  public SearchMoves printSearchReport () {

    long searchEndTime = System.currentTimeMillis();

    System.out.println("SearchMove: strength difference");
    evaluatedMoves.stream().limit(nrOfMoves).forEach(move -> {
      move.printMovePoints();
      System.out.print(": "+move.getSearchEvaluation());
      System.out.println();
    });
    System.out.println("Time Millis: "+(searchEndTime- startTime));
    return this;
  }

}
