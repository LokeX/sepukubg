package bg.engine.play;

import bg.engine.core.moves.Layout;

import java.util.ArrayList;
import java.util.List;

import static bg.Main.sepuku;


public class MoveOutput {

  private List<Layout> outputLayouts = new ArrayList<>();
  private long startTime;
  private boolean firstLayout;
  private Object notifier;
  
  private void initOutput () {

    startTime = System.currentTimeMillis();
    firstLayout = true;
  }

  public void setOutputLayouts (List<Layout> layouts) {

      outputLayouts.addAll(layouts);
      initOutput();
  }

  public void setOutputLayout (Layout layouts) {

    outputLayouts.add(layouts);
    initOutput();
  }

  public boolean hasOutput () {
    
    return
      outputLayouts.size() > 0 && timeDelayElapsed();
  }

  private Layout takeLayout () {

    Layout layout = outputLayouts.remove(0);

    
    if (notifier != null && outputLayouts.size() == 0) {
      synchronized (notifier) {
        notifier.notifyAll();
      }
      this.notifier = null;
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

  public boolean isBusy () {

    return
      outputLayouts.size() > 0;
  }

  public void setEndOfOutputNotifier (Object notifier) {

      this.notifier = notifier;
  }

  private long elapsedTime () {

    return
      System.currentTimeMillis() - startTime;
  }

  private boolean timeDelayElapsed () {

    return
       firstLayout || elapsedTime() > sepuku.getSettings().getShowMoveDelay();
  }

}