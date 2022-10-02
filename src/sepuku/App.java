package sepuku;

import IO.FileHandler;
import IO.Sound;
import engine.api.PlaySepuku;
import engine.core.moves.Layout;
import engine.core.trainer.Trainer;
import inUrFace.canvas.Canvas;
import inUrFace.canvas.painters.BonusPainter;
import inUrFace.canvas.listeners.MouseListeners;
import util.time.TimedTasks;
import inUrFace.windows.Window;

public class App {

  static public TimedTasks timedTasks;
  static public FileHandler files;
  static public Window win;
  static public MouseListeners mouse;
  static public PlaySepuku playSepuku;
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
    playSepuku = new PlaySepuku();
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