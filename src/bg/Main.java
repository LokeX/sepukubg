package bg;

import bg.IO.FileHandler;
import bg.IO.Sound;
import bg.engine.api.EngineApi;
import bg.engine.coreLogic.moves.Layout;
import bg.engine.coreLogic.trainer.Trainer;
import bg.engine.api.Scenarios;
import bg.inUrFace.canvas.Canvas;
import bg.inUrFace.canvas.BonusPainter;
import bg.inUrFace.mouse.MouseApi;
import bg.util.time.TimedTasks;
import bg.inUrFace.windows.Window;

public class Main {

  static public TimedTasks timedTasks;
  static public FileHandler files;
//  static public Scenarios scenarios;
//  static public Settings settings;
  static public Window win;
  static public MouseApi mouse;
  static public EngineApi engineApi;
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
//    settings = new Settings();
//    scenarios = new Scenarios();
    engineApi = new EngineApi();
    files = new FileHandler();
    trainer = new Trainer();

    if (nrOfStatGames == 0) {
      Window.runWindow();
      mouse = new MouseApi();
    } else {
      trainer.playMatches(nrOfStatGames);
    }
  }

}