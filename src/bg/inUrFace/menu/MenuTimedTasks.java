package bg.inUrFace.menu;

import bg.util.time.Timeable;
import java.util.ArrayList;
import java.util.List;

public class MenuTimedTasks implements Timeable {

  List<Timeable> timedTasks = new ArrayList();

  public MenuTimedTasks (List<Timeable> timedTasks) {

    this.timedTasks = timedTasks;
  }

  public void runTasks () {

    timedTasks.forEach(task -> task.timerUpdate());
  }

  public void addTask (Timeable task) {

    timedTasks.add(task);
  }

  public void setTaskList (List<Timeable> tasks) {

    timedTasks = tasks;
  }

  @Override
  public void timerUpdate() {

    runTasks();
  }
}
