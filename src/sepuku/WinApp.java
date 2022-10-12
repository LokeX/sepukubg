package sepuku;

import inOut.FileHandler;
import inOut.Sound;
import engine.api.SepukuPlay;
import engine.core.trainer.Trainer;
import inUrFace.canvas.Canvas;
import util.time.TimedTasks;
import inUrFace.windows.Window;

public class WinApp {

  static public TimedTasks timedTasks;
  static public FileHandler files;
  static public Window win;
  static public SepukuPlay sepukuPlay;
  static public Trainer trainer;
  static public Sound sound;

  public static Canvas getCanvas () {

    return win.getCanvas();
  }

  public static void main (String[] args) {

    timedTasks = new TimedTasks();
    sound = new Sound();
    sepukuPlay = new SepukuPlay();
    files = new FileHandler();
    trainer = new Trainer();

    Window.runWindow();
  }

}