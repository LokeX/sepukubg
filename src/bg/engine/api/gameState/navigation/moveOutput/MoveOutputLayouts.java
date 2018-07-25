package bg.engine.api.gameState.navigation.moveOutput;

import bg.engine.match.moves.Layout;

import java.util.List;

import static bg.Main.settings;

public class MoveOutputLayouts {

  private List<Layout> moveLayouts;
  private int layoutNr;
  private long startTime = System.currentTimeMillis();

  MoveOutputLayouts(List<Layout> moveLayouts) {

    this.moveLayouts = moveLayouts;
  }

  void setMoveLayouts (List<Layout> moveLayouts) {

    startTime = System.currentTimeMillis();
    this.moveLayouts = moveLayouts;
    layoutNr = 0;
  }

  public boolean hasOutput () {

    return
      moveLayouts != null
      && layoutNr < moveLayouts.size();
  }

  public Layout getNextLayout () {

    if (timeDelayElapsed()) {
      layoutNr++;
    }
    return
      hasOutput()
        ? moveLayouts.get(layoutNr)
        : null;
  }

  private long delayTime () {

    return
      settings
        .getShowMoveDelay()*layoutNr;
  }

  private long timeElapsed () {

    return
      System.currentTimeMillis() - startTime;
  }

  private boolean timeDelayElapsed () {

    return
      timeElapsed() > delayTime();
  }

}