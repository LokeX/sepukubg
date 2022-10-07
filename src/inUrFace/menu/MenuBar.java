package inUrFace.menu;

import inUrFace.menu.dropMenues.ScenarioMenu;
import inUrFace.menu.dropMenues.trainingMenu.TrainerMenu;
import inUrFace.menu.dropMenues.NavigateMenu;
import inUrFace.menu.dropMenues.EditMenu;
import inUrFace.menu.dropMenues.MatchMenu;

import static sepuku.WinApp.timedTasks;
import util.time.Timeable;
import inUrFace.menu.dropMenues.settingsMenu.SettingsMenu;

import static util.Reflection.getFieldsList;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {

    public MatchMenu matchMenu = new MatchMenu();
    public ScenarioMenu scenarioMenu = new ScenarioMenu();
    public EditMenu editMenu = new EditMenu();
    public NavigateMenu navigateMenu = new NavigateMenu();
    public SettingsMenu settingsMenu = new SettingsMenu();
    public TrainerMenu trainerMenu = new TrainerMenu();

  public MenuBar () {

    setupBarMenu();
  }

  private void setupBarMenu() {

    List<JMenu> menuList = new ArrayList<>();
    List<Timeable> timedTaskList = new ArrayList<>();

    getFieldsList(this, menuList);
    menuList.forEach((menu) -> {
      add(menu);
      if (menu instanceof Timeable) {
        timedTaskList.add((Timeable)menu);
      }
    });
    timedTasks.addTimedTask(
      new MenuTimedTasks(timedTaskList)
    );
  }

}