package bg.engine.api;

import java.awt.event.MouseEvent;
import java.util.stream.Stream;

public interface HumanMoveApi {

  void pointClicked (MouseEvent e, int clickedPoint);

  int getPlayerID ();

  boolean inputReady ();

  boolean isEndingPoint ();

  Stream<Integer> getEndingPoints ();

}
