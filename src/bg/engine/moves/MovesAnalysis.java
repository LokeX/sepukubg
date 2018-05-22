package bg.engine.moves;

import java.util.List;
import java.util.stream.Collectors;

public class MovesAnalysis extends Moves {

  public MovesAnalysis(Moves moves) {

    super(moves);
  }

  public List<MoveLayout> legalMovePoints (int pointNr, int movePoint) {

    return
      getLegalMoves().stream().
      filter(move -> move.getMovePoints()[pointNr] == movePoint).
      distinct().collect(Collectors.toList());
  }

//  public boolean isLegalMovePoint (int pointNr, int movePoint) {
//
////    return
////      legalMovePoints(pointNr).stream().
////      anyMatch(point -> point == movePoint);
//  }



}
