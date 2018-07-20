package bg.engine.api.gameState.navigation.moveOutput;

import bg.engine.match.moves.Layout;

import java.util.List;

import static bg.Main.settings;

public class OutputLayouts {

  private List<Layout> outputLayouts;
  private int layoutNr;
  private long startTime;

  OutputLayouts (List<Layout> outputLayouts) {

    this.outputLayouts = outputLayouts;
    startTime = System.currentTimeMillis();
  }

  void setOutputLayouts(List<Layout> outputLayouts) {

    this.outputLayouts = outputLayouts;
  }

  public boolean hasOutput () {

    return
      outputLayouts != null
      && layoutNr < outputLayouts.size();
  }

  public Layout getNextLayout () {

    return
      hasOutput() && timeDelayElapsed()
        ? outputLayouts.get(layoutNr++)
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