package bg.api;

import bg.engine.moves.Layout;

public interface MoveOutputApi {

  boolean outputReady ();

  Layout getOutputLayout ();

}
