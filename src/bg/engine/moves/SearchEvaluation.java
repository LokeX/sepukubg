package bg.engine.moves;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SearchEvaluation {

  private List<EvaluatedMove> searchMoves;
  private String report = "";
  private int nrOfMoves;
  private int nrOfTurns;
  private long startTime;

  public String getReport () {

    return report;
  }

  SearchEvaluation generateSearchEvaluations (

    int nrOfMoves, int nrOfTurns,
    List<EvaluatedMove> searchMoves) {

    startTime = System.currentTimeMillis();
    this.searchMoves = searchMoves;
    this.nrOfMoves = nrOfMoves;
    this.nrOfTurns = nrOfTurns;
    searchMoves
      .parallelStream()
      .limit(nrOfMoves)
      .forEach(move ->
        move.setSearchEvaluation(
          move.getLayoutStrength() -
            projectedStrengthAverage(
              1,
              move
            )
        )
      );
    report += "Before values:\n";
    appendReportValues();
    return this;
  }

  private List<EvaluatedMove> bestMoves (

    List<Moves> searchMoves) {

    return
      searchMoves
        .parallelStream()
        .map(Moves::getBestMove)
        .collect(toList());
  }

  private int movesStrengthAverage (

    List<EvaluatedMove> moves) {

    return
      moves
        .parallelStream()
        .mapToInt(EvaluatedMove::getProbabilityAdjustedLayoutStrength)
        .sum()/36;
  }

  private int nextProjectedStrengthAverage (

    int turnCount,
    List<EvaluatedMove> bestMoves) {

    return (int)
      bestMoves
        .parallelStream()
        .mapToInt(bestMove ->
          projectedStrengthAverage(
            turnCount + 1,
            bestMove
          )
        )
        .average()
        .orElse(0);
  }

  private int projectedStrengthAverage (int turnCount, EvaluatedMove move) {

    List<EvaluatedMove> bestMoves = bestMoves(move.getSearchMoves());
    int projectedStrengthAverage = 0;

    if (turnCount < nrOfTurns && !move.isWinningMove()) {
      projectedStrengthAverage =
        nextProjectedStrengthAverage(
          turnCount,
          bestMoves
        );
    } else {
      nrOfTurns = turnCount;
    }
    return movesStrengthAverage(bestMoves) - projectedStrengthAverage;
  }

  void sortEvaluatedMoves() {

    printSearchReport();

    List<EvaluatedMove> sortedMoves
      = searchMoves.stream()
        .limit(nrOfMoves)
        .sorted((a, b) -> b.searchEvaluation - a.searchEvaluation)
        .collect(toList());

    for (int a = 0; a < sortedMoves.size(); a++) {
      searchMoves.set(a, sortedMoves.get(a));
    }
    report += "After values:\n";
    appendReportValues();
    appendTime();
    printSearchReport();
  }

  private void appendTime () {

    report +=
      "Time in milliseconds: "+
      (System.currentTimeMillis()- startTime)+
      "\n";
  }

  private void appendReportValues () {

    report += searchMoves.stream().
      limit(nrOfMoves).
      map(move ->
        move.getMovePointsString()+": "+
        move.getSearchEvaluation()+"\n"
      ).reduce(String::concat);
  }

  public SearchEvaluation printSearchReport () {

    long searchEndTime = System.currentTimeMillis();

    System.out.println("SearchMove: strength difference");
    searchMoves.stream().limit(nrOfMoves).forEach(move -> {
      move.printMovePoints();
      System.out.print(": "+move.getSearchEvaluation());
      System.out.println();
    });
    System.out.println("Time Millis: "+(searchEndTime- startTime));
    return this;
  }

}
