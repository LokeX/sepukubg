package bg.engine.api;

public interface DisplayLayoutsApi {

  boolean outputReady ();

  int[] getOutputLayout ();

  int getWhitePip ();

  int getBlackPip ();

}
