package bg.engine.api.gameState.humanMove;

import java.util.stream.Stream;

public interface HumanMoveApi {

  void pointClicked (int clickedPoint);

  int getPlayerID ();

  boolean inputReady ();

  boolean isEndingPoint ();

  Stream<Integer> getEndingPoints ();

}
