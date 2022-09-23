package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.Main;
import bg.util.menus.Listable;
import java.util.List;

import static bg.Main.sepuku;

public class ScenarioMenu implements Listable {

  private int selectedItemNr = 0;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    Main.trainer.setSelectedScenarioNr(selectedItemNr);
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Play scenario";
  }

  @Override
  public List<String> getElementTitles() {

    return sepuku.getScenarios().getLayoutTitles();
  }

}
