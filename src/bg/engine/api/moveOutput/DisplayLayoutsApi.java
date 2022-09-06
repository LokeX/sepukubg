package bg.engine.api.moveOutput;

public interface DisplayLayoutsApi {

  boolean outputReady ();

  int[] getOutputLayout ();

  int getWhitePip ();

  int getBlackPip ();

}
