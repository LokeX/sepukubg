package bg.engine.api;

import bg.engine.match.moves.Layout;

import java.util.List;

public class DisplayLayouts {

  private List<Layout> outputLayouts;
  private int layoutNr;

  public boolean outputIsAvailable() {

    return
      outputLayouts != null
      && !lastLayoutConsumed();
  }

  private boolean lastLayoutConsumed () {

    return layoutNr == outputLayouts.size();
  }

  public int getWhitePip () {

    return outputLayouts.get(layoutNr).getWhitePip();
  }

  public int getBlackPip () {

    return outputLayouts.get(layoutNr).getBlackPip();
  }

}
