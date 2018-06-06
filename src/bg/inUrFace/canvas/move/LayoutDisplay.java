package bg.inUrFace.canvas.move;

import bg.Main;
import bg.engine.moves.Layout;
import java.util.List;

import static bg.Main.matchApi;
import static bg.Main.settings;
import static bg.Main.win;
import static bg.util.ThreadUtil.threadSleep;

public class LayoutDisplay {

  public void showMovePoints (List<Layout> frames, final Object notifier) {

    new Thread(() -> {

      frames.forEach(frame -> {
        displayLayout(frame);
        Main.sound.playSoundEffect("Blop-Mark_DiAngelo");
        threadSleep(settings.getShowMoveDelay());
      });
      synchronized (notifier) {
        notifier.notifyAll();
      }
    }).start();
  }

  public void displayLayout(Layout layout) {

    win.canvas.
      setDisplayedLayout(
        matchApi.getSelectedTurn().getPlayerOnRollsID() == 0 ?
          new Layout(layout) :
          new Layout(layout).getFlippedLayout()
      );
  }

}
