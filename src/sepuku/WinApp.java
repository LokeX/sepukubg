package sepuku;

import inOut.FileHandler;
import inOut.Sound;
import engine.api.SepukuPlay;
import engine.core.moves.Layout;
import engine.core.trainer.Trainer;
import inUrFace.canvas.Canvas;
import inUrFace.canvas.painters.BonusPainter;
import inUrFace.canvas.listeners.MouseListeners;
import util.time.TimedTasks;
import inUrFace.windows.Window;

public class WinApp {

  static public TimedTasks timedTasks;
  static public FileHandler files;
  static public Window win;
  static public MouseListeners mouse;
  static public SepukuPlay sepukuPlay;
  static public Trainer trainer;
  static public Sound sound;

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
    sepukuPlay = new SepukuPlay();
    files = new FileHandler();
    trainer = new Trainer();

    Window.runWindow();
    mouse = new MouseListeners();
  }

}