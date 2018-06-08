package bg;

import bg.IO.FileHandler;
import bg.IO.Sound;
import bg.api.MatchApi;
import bg.engine.moves.Layout;
import bg.engine.trainer.Trainer;
import bg.engine.Scenarios;
import bg.inUrFace.canvas.Canvas;
import bg.inUrFace.canvas.DoublingCube;
import bg.inUrFace.canvas.ActionButton;
import bg.inUrFace.canvas.ScenarioEditor;
import bg.inUrFace.canvas.BonusPainter;
import bg.inUrFace.mouse.MouseApi;
import bg.inUrFace.mouse.MoveInput;
import bg.inUrFace.mouse.MoveInputApi;
import bg.inUrFace.windows.InformationBar;
import bg.util.time.TimedTasks;
import bg.inUrFace.windows.Window;

public class Main {

  static public TimedTasks timedTasks;
  static public FileHandler files;
  static public Scenarios scenarios;
  static public Settings settings;
  static public Window win;
  static public MouseApi mouse;
  static public MatchApi matchApi;
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

  public static MouseApi getMouse () {

    return mouse;
  }

  public static ActionButton getActionButton() {

    return getMouse().getActionButton();
  }

  public static DoublingCube getDoublingCube () {

    return getMouse().getDoublingCube();
  }

  public static ScenarioEditor getLayoutEditor () {

    return getMouse().getLayoutEditor();
  }

  public static Settings getSettings () {

    return settings;
  }

  public static MoveInput getMoveInput () {

    return mouse.getMoveInput();
  }

  public static InformationBar getInformationBar() {

    return win.informationBar;
  }

  public static MoveInputApi getMoveInputController () {

    return mouse.getMoveInputController();
  }

  public static BonusPainter getTextArea () {

    return win.getCanvas().getPaintJobs().bonusPainter;
  }


  public static void main (String[] args) {

    timedTasks = new TimedTasks();
    settings = new Settings();
    sound = new Sound();
    scenarios = new Scenarios();
    files = new FileHandler();
    trainer = new Trainer();

    if (nrOfStatGames == 0) {
      Window.runWindow();
      mouse = new MouseApi();
      matchApi = new MatchApi();
    } else {
      trainer.playMatches(nrOfStatGames);
    }
  }

}