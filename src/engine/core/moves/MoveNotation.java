package engine.core.moves;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class MoveNotation extends MoveLayout {
  
  public MoveNotation(MoveLayout moveLayout) {
    
    super(moveLayout);
  }
  
  private boolean thereAreDoublesIn(List<String> annotatedPartMoves) {
    
    return
      parentMoves.getDiceObj().areDouble()
      && annotatedPartMoves
        .stream().distinct().count() < parentMoves.getNrOfLegalPartMoves();
  }
  
  private String nrOfDuplicatesIn(List<String> annotatedPartMoves, String partMove) {
    
    int nrOfDuplicates =
      (int) IntStream.range(0,annotatedPartMoves.size())
        .filter(position ->
          annotatedPartMoves.get(position).equals(partMove)
        ).count();
  
    return
      nrOfDuplicates > 1
      ? "("+nrOfDuplicates+")"
      : "";
  }
  
  private List<String> annotateDoublesIn(List<String> annotatedPartMovesOf) {
  
    return
      annotatedPartMovesOf
      .stream()
      .distinct()
      .map(partMove ->
        partMove+nrOfDuplicatesIn(
          annotatedPartMovesOf,
          partMove
        )
      )
      .toList();
  }
  
  private List<String> annotatedDoublesIn(List<String> annotatedPartMoves) {
    
    return
      thereAreDoublesIn(annotatedPartMoves)
      ? annotateDoublesIn(annotatedPartMoves)
      : annotatedPartMoves;
  }
  
  private String pointNotation (int position) {
    
    return
      movePoints[position] == 0
      || movePoints[position] == 26
      ? "off"
      : movePoints[position] == 25
      || movePoints[position] == 51
      ? "bar/"
      : position%2 == 1
      && hitPoints[position] != -1
      ? movePoints[position]+"*"
      : position%2 == 0
      ? movePoints[position]+"/"
      : movePoints[position]+"";
  }
  
  private List<String> listOfPartMovesFrom(List<String> annotatedMovePoints) {
    
    return
      IntStream.range(0,annotatedMovePoints.size())
        .filter(position -> position%2 == 0)
        .mapToObj(position ->
          annotatedMovePoints.get(position)+annotatedMovePoints.get(position+1)
        )
        .toList();
  }
  
  private List<String> annotatedMovePoints() {
    
    return
      IntStream.range(0,parentMoves.getNrOfLegalPartMoves()*2)
        .mapToObj(this::pointNotation)
        .toList();
  }
  
  private String distinctSpaceDelimited(List<String> partMovesList) {
    
    return
      partMovesList
      .stream()
      .distinct()
      .collect(
        joining(" ")
      );
  }
  
  public String notation () {
    
    String dice = parentMoves.getDiceObj().getDiceInt()+": ";
    List<String> annotatedPartMoves =
      annotatedDoublesIn(listOfPartMovesFrom(annotatedMovePoints()));
    
    return
      annotatedPartMoves.isEmpty()
        ? dice+"N/A"
        : dice+distinctSpaceDelimited(annotatedPartMoves);
  }
  
}
