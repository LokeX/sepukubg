package bg.api;

import java.awt.event.MouseEvent;

public interface Moveable {

  void pointClicked (MouseEvent e, int clickedPoint);

  boolean isAcceptingInput ();

  int getPlayerID ();
}
