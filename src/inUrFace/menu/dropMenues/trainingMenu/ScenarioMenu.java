package inUrFace.menu.dropMenues.trainingMenu;

import sepuku.App;
import util.menus.Listable;
import java.util.List;

import static sepuku.App.playSepuku;

public class ScenarioMenu implements Listable {

  private int selectedItemNr = 0;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    App.trainer.setSelectedScenarioNr(selectedItemNr);
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

    return playSepuku.getScenarios().getLayoutTitles();
  }

}
