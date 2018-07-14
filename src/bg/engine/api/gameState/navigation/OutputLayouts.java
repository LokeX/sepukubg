package bg.engine.api.gameState.navigation;

import bg.engine.match.moves.Layout;

import java.util.List;

import static bg.Main.settings;

public class OutputLayouts {

  private List<Layout> outputLayouts;
  private int layoutNr;
  private long timeOfLastOutput;

  OutputLayouts (List<Layout> outputLayouts) {

    this.outputLayouts = outputLayouts;
    timeOfLastOutput = System.currentTimeMillis();
  }

  public OutputLayouts getOutputLayouts (List<Layout> outputLayouts) {

    this.outputLayouts = outputLayouts;
    return this;
  }

  private boolean hasOutput () {

    return
      outputLayouts != null;
  }

  public boolean endOfOutput () {

    return
      !hasOutput() || layoutNr == outputLayouts.size();
  }

  public Layout getNextLayout () {

    return
      mustEmit()
        ? outputLayouts.get(layoutNr++)
        : null;
  }

  private boolean mustEmit () {

    return
      timeDelayElapsed()
        && layoutNr < outputLayouts.size();
  }

  private long timeDelay () {

    return settings.getShowMoveDelay();
  }

  private long timeElapsed () {

    return
      System.currentTimeMillis() - timeOfLastOutput;
  }

  private boolean timeDelayElapsed () {

    return
      outputLayouts.size() == 1
        || timeElapsed() >= timeDelay();
  }

}