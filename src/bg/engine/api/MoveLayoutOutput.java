package bg.engine.api;

import bg.engine.coreLogic.moves.Layout;

import java.util.ArrayList;
import java.util.List;

import static bg.Main.engineApi;


public class MoveLayoutOutput {

  private List<Layout> outputLayouts = new ArrayList<>();
  private long startTime;
  private boolean firstLayout;
  private Object notifier;
  private Displayable layoutDisplay;
  
  public MoveLayoutOutput () {
    System.out.println("MoveLayoutOutput constructed");
  }

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

      System.out.println("received "+layouts.size()+" layouts");
//    if (!hasOutput()) {
      outputLayouts.addAll(layouts);
      initOutput();
//    }
  }

  public void setOutputLayout (Layout layouts) {

//    if (!hasOutput()) {
      outputLayouts.add(layouts);
      System.out.println("Layout set to output");
      System.out.println("hasOutput: "+hasOutput());
      System.out.println("nrOfLayouts left: "+outputLayouts.size());
      
      initOutput();
      System.out.println("nrOfLayouts left after initOutput: "+outputLayouts.size());
      System.out.println("and hasOutput: "+hasOutput());
//    }
  }

  public boolean hasOutput () {
    
//    System.out.println("checked for output");
//    System.out.println("hasOutput = "+(outputLayouts.size() > 0 && timeDelayElapsed()));
//    System.out.println("nrOfLayouts = "+outputLayouts.size());
//    System.out.println("timeDelayElapsed = "+timeDelayElapsed());
    return
      outputLayouts.size() > 0 && timeDelayElapsed();
  }

  private Layout takeLayout () {

    System.out.println("Taking layout");
    Layout layout = outputLayouts.remove(0);

    
    if (notifier != null && outputLayouts.size() == 0) {
      System.out.println("Notifying endOfOutput");
      synchronized (notifier) {
        notifier.notifyAll();
      }
      this.notifier = null;
    }
    startTime = System.currentTimeMillis();
    firstLayout = false;
    System.out.println("Layout taken");
    System.out.println("nrOfLayouts left: "+outputLayouts.size());

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

    System.out.println("Layout Requested");
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
       firstLayout || elapsedTime() > engineApi.getSettings().getShowMoveDelay();
  }

}