package bg.inUrFace.menu;

import bg.inUrFace.menu.dropMenues.ScenarioMenu;
import bg.inUrFace.menu.dropMenues.trainingMenu.TrainerMenu;
import bg.inUrFace.menu.dropMenues.NavigateMenu;
import bg.inUrFace.menu.dropMenues.EditMenu;
import bg.inUrFace.menu.dropMenues.MatchMenu;

import static bg.Main.timedTasks;
import bg.util.time.Timeable;
import bg.inUrFace.menu.dropMenues.settingsMenu.SettingsMenu;
import static bg.util.Reflection.getFieldsList;
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

    List<JMenu> menuList = new ArrayList();
    List<Timeable> timedTaskList = new ArrayList();

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
//    timedTasks.setMenuTimedTasks(
//      new MenuTimedTasks(timedTaskList)
//    );
  }

}