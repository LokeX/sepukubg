package bg.util.time;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimedTasks extends TimerTask {


  private static Timer timer = new Timer(true);
  private final List<Timeable> timedTasks = new ArrayList<>();

  private TimedTasks (int startInMillis, int continueInMillis) {

    timer.scheduleAtFixedRate(this, startInMillis, continueInMillis);
  }

  public TimedTasks () {

    this(100,100);
  }

  @Override
  public void run () {

    synchronized (timedTasks) {
      timedTasks.forEach(Timeable::timerUpdate);
    }
  }

  public void addTimedTask (Timeable timeable) {

    synchronized (timedTasks) {
      timedTasks.add(timeable);
    }
  }

  public void removeTimedTask (Timeable timeable) {

    synchronized (timedTasks) {

      int index = timedTasks.indexOf(timeable);

      if (index > -1) {
        timedTasks.remove(timeable);
      }
    }
  }

}
