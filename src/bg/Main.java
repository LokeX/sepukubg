package bg;

import bg.IO.FileHandler;
import bg.IO.Sound;
import bg.engine.api.Sepuku;
import bg.engine.core.moves.Layout;
import bg.engine.core.trainer.Trainer;
import bg.inUrFace.canvas.Canvas;
import bg.inUrFace.canvas.BonusPainter;
import bg.inUrFace.canvas.MouseListeners;
import bg.util.time.TimedTasks;
import bg.inUrFace.windows.Window;

public class Main {

  static public TimedTasks timedTasks;
  static public FileHandler files;
  static public Window win;
  static public MouseListeners mouse;
  static public Sepuku sepuku;
  static public Trainer trainer;
  static public Sound sound;

  static public final int nrOfStatGames = 0;

  public static Window getWin () {

    return win;
  }

  public static Layout getDisplayedLayout () {

    return win.canvas.getDisplayedLayout();
  }

  public static Canvas getCanvas () {

    return getWin().getCanvas();
  }

  public static BonusPainter getTextArea () {

    return win.getCanvas().getPaintJobs().bonusPainter;
  }
  
  public static void main (String[] args) {

    timedTasks = new TimedTasks();
    sound = new Sound();
    sepuku = new Sepuku();
    files = new FileHandler();
    trainer = new Trainer();

    if (nrOfStatGames == 0) {
      Window.runWindow();
      mouse = new MouseListeners();
    } else {
      trainer.playMatches(nrOfStatGames);
    }
  }

}