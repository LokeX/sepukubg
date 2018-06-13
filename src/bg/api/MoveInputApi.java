package bg.api;

import java.awt.event.MouseEvent;

public interface MoveInputApi {

  void pointClicked (MouseEvent e, int clickedPoint);

  int getPlayerID ();

}
