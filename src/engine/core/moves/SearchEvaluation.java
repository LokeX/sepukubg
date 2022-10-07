package engine.core.moves;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SearchEvaluation {

  private List<EvaluatedMove> searchMoves;
  private List<String> searchReport;
  private int nrOfMoves;
  private int nrOfTurns;
  private long startTime;

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
          move.getLayoutStrength()
          - projectedStrengthAverage(
            1,
            move
          )
        )
      );
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
  
  private void addSearchEvaluationsToReport() {
    
    searchReport.add("Search evaluations:");
    searchReport.add("");
    searchReport.addAll(moveSearchReports());
    searchReport.add(timeReport());
    searchReport.add("");
  }
  
  private void addMoveEvaluationsToReport() {
    
    searchReport.add("Move evaluations:");
    searchReport.add("");
    searchReport.addAll(moveEvaluationReports());
    searchReport.add(timeReport());
    searchReport.add("");
  }
  
  private void createReportHeader() {
    
    searchReport = new ArrayList<>();
    searchReport.add(
      "Search report ["+nrOfMoves+" best moves, to ply "+nrOfTurns+"]:"
    );
    searchReport.add("");
  }

  void sortEvaluatedMoves() {

    createReportHeader();
    addMoveEvaluationsToReport();

    List<EvaluatedMove> sortedMoves
      = searchMoves.stream()
      .limit(nrOfMoves)
      .sorted((a, b) -> b.searchEvaluation - a.searchEvaluation)
      .toList();

    for (int a = 0; a < sortedMoves.size(); a++) {
      searchMoves.set(a, sortedMoves.get(a));
    }
    addSearchEvaluationsToReport();
    printSearchReport();
  }

  private String timeReport () {

    long searchEndTime = System.currentTimeMillis();
    
    return
      "Time Millis: "+(searchEndTime- startTime);
  }

  private List<String> moveSearchReports() {

    return
      searchMoves
      .stream()
      .limit(nrOfMoves)
      .map(move ->
        "["+move.getMovePointsString()+"]: "+move.getSearchEvaluation()
      )
      .toList();
  }

  private List<String> moveEvaluationReports() {

    return
      searchMoves
      .stream()
      .limit(nrOfMoves)
      .map(move ->
        "["+move.getMovePointsString()+"]: "+move.getLayoutStrength()
      )
      .toList();
  }

  public SearchEvaluation printSearchReport () {

    searchReport.forEach(System.out::println);
    
    return this;
  }
  
  public List<String> getSearchReport() {

    return
      searchReport;
  }
  
}
