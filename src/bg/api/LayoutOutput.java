package bg.api;

import bg.engine.moves.Layout;

import java.util.ArrayList;
import java.util.List;

import static bg.Main.settings;

public class LayoutOutput implements LayoutOutputApi {

  private List<List<Layout>> outputLayoutsBuffer = new ArrayList<>();
  private List<Layout> outputLayouts;
  private Object notifier;
  private int layoutNr;
  private long timeOfLastOutput;

  void outputLayouts(List<Layout> outputLayouts, Object notifier) {

    synchronized (this) {
      if (outputIsAvailable()) {
        outputLayoutsBuffer.add(outputLayouts);
      } else {
        this.outputLayouts = outputLayouts;
      }
      this.notifier = notifier;
      layoutNr = 0;
      timeOfLastOutput = 0;
    }
  }

  void outputLayouts(List<Layout> outputLayouts) {

    outputLayouts(outputLayouts, new Object());
  }

  void outputLayout (Layout layout) {

    outputLayouts(List.of(layout), new Object());
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

  public boolean outputReady () {

    return
      (timeDelayElapsed() || layoutNr == 0)
      && outputIsAvailable();
  }

  private boolean outputIsAvailable() {

    return
      outputLayouts != null
      && !lastLayoutConsumed();
  }

  private void popNextList () {

    synchronized (this) {
      outputLayouts = outputLayoutsBuffer.remove(0);
      layoutNr = 0;
    }
  }

  private boolean nextListReady () {

    return !outputLayoutsBuffer.isEmpty() && lastLayoutConsumed();
  }

  private boolean lastLayoutConsumed () {

    return layoutNr == outputLayouts.size();
  }

  private boolean lastLayout () {

    return layoutNr == outputLayouts.size()-1;
  }

  public int getWhitePip () {

    return outputLayouts.get(layoutNr).getWhitePip();
  }

  public int getBlackPip () {

    return outputLayouts.get(layoutNr).getBlackPip();
  }

  public int[] getOutputLayout () {

    if (nextListReady()) {
      popNextList();
    }
    if (outputReady()) {
      if (lastLayout()) {
        notifier
          .notifyAll();
      }
      timeOfLastOutput =
        System.currentTimeMillis();
      return
        outputLayouts.get(layoutNr++)
          .getFlippedLayout()
          .getPoint();
    }
    return null;
  }

}
