package inUrFace.menu;

import util.time.Timeable;

import java.util.List;

public class MenuTimedTasks implements Timeable {

  List<Timeable> timedTasks;

  public MenuTimedTasks (List<Timeable> timedTasks) {

    this.timedTasks = timedTasks;
  }

  public void runTasks () {

    timedTasks.forEach(Timeable::timerUpdate);
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
