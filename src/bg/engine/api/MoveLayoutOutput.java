package bg.engine.api;

import bg.engine.coreLogic.moves.Layout;

import java.util.ArrayList;
import java.util.List;

import static bg.Main.settings;

public class MoveLayoutOutput {

  private List<Layout> outputLayouts = new ArrayList<>();
  private long startTime;
  private boolean firstLayout;
  private Object notifier;
  private Displayable layoutDisplay;

  public void setDisplayLayout (Layout displayLayout) {

    if (layoutDisplay != null) {
      layoutDisplay.setDisplayedLayout(
       playerAdjustedLayout(displayLayout)
      );
    }
  }

  public void registerLayoutDisplay (Displayable layoutDisplay) {

    this.layoutDisplay = layoutDisplay;
  }

  private void initOutput () {

    startTime = System.currentTimeMillis();
    firstLayout = true;
  }

  public void setOutputLayouts (List<Layout> layouts) {

    if (!hasOutput()) {
      outputLayouts.addAll(layouts);
      initOutput();
    }
  }

  public void setOutputLayout (Layout layouts) {

    if (!hasOutput()) {
      outputLayouts.add(layouts);
      initOutput();
    }
  }

  public boolean hasOutput () {

    return
      outputLayouts.size() > 0 && timeDelayElapsed();
  }

  private Layout takeLayout () {

    Layout layout = outputLayouts.remove(0);

    if (notifier != null && !hasOutput()) {
      synchronized (notifier) {
        notifier.notifyAll();
      }
      notifier = null;
    }
    startTime = System.currentTimeMillis();
    firstLayout = false;

    return
      playerAdjustedLayout(layout);
  }

  private Layout playerAdjustedLayout (Layout layout) {

    return
      layout.getPlayerID() == 0
        ? layout
        : layout.getFlippedLayout();
  }

  public Layout getMovePointLayout () {

    return
      hasOutput()
      ? takeLayout()
      : null;
  }

  public void setEndOfOutputNotifier (Object notifier) {

    if (outputLayouts.size() == 0) {
      this.notifier = notifier;
    }
  }

  private long elapsedTime () {

    return
      System.currentTimeMillis() - startTime;
  }

  private boolean timeDelayElapsed () {

    return
       firstLayout || elapsedTime() > settings.getShowMoveDelay();
  }

}