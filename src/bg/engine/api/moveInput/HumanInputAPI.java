package bg.engine.api.moveInput;

import java.util.stream.Stream;

public interface HumanInputAPI {

  void pointClicked(int clickedPoint);

  int getPlayerID();

  boolean humanInputActive();

  boolean endingPointIsNext();

  Stream<Integer> getEndingPoints();

  int[] getMovePoints ();
}
