package bg.engine.api.gamePlay.navigation.humanMove;

import java.util.stream.Stream;

public interface HumanMoveApi {

  void pointClicked (int clickedPoint);

  int getPlayerID ();

  boolean inputReady ();

  boolean isEndingPoint ();

  Stream<Integer> getEndingPoints ();

}
