package bg.api;

import java.awt.event.MouseEvent;

public interface HumanMoveApi {

  void pointClicked (MouseEvent e, int clickedPoint);

  int getPlayerID ();

  boolean inputReady ();

}
