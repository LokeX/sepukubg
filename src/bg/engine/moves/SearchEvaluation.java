package bg.engine.moves;

import java.util.List;
import java.util.stream.Stream;

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

  public SearchEvaluation generateSearchEvaluations (

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
            projectedStrengthAverage(1, move)
        )
      );
    report += "Before values:\n";
    appendReportValues();
    return this;
  }

  private int projectedStrengthAverage (int turnCount, EvaluatedMove move) {

    List<EvaluatedMove> bestMoves
      = move.getSearchMoves()
        .parallelStream()
        .map(Moves::getBestMove)
        .collect(toList());
    int nextTurnsStrengthAverage = 0;
    int turnsStrengthAverage
      = bestMoves
        .parallelStream()
        .mapToInt(EvaluatedMove::getProbabilityAdjustedLayoutStrength)
        .sum()/36;

    if (turnCount < nrOfTurns && !move.isWinningMove()) {
      nextTurnsStrengthAverage
        = (int) bestMoves
          .parallelStream()
          .mapToInt(bestMove ->
            projectedStrengthAverage(
              turnCount+1,
              bestMove
            )
          )
          .average()
          .orElse(0);
    } else {
      nrOfTurns = turnCount;
    }
    return turnsStrengthAverage - nextTurnsStrengthAverage;
  }

  void sortEvaluatedMoves() {

    printSearchReport();

    List<EvaluatedMove> sortedMoves = searchMoves.stream().
      limit(nrOfMoves).
      sorted((a, b) -> b.searchEvaluation - a.searchEvaluation).
      collect(toList());

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
