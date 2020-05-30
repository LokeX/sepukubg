package bg.engine.api;

import java.util.stream.Stream;

public interface HumanInputAPI {

  void pointClicked(int clickedPoint);

  int getPlayerID();

  boolean humanInputActive();

  boolean endingPointIsNext();

  Stream<Integer> getEndingPoints();
}
