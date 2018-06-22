package bg.api;

public interface LayoutOutputApi {

  boolean outputReady ();

  int[] getOutputLayout ();

  int getWhitePip ();

  int getBlackPip ();

}
