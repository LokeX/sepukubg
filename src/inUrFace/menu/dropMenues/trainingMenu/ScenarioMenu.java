package inUrFace.menu.dropMenues.trainingMenu;

import sepuku.WinApp;
import util.menus.Listable;
import java.util.List;

import static sepuku.WinApp.sepukuPlay;

public class ScenarioMenu implements Listable {

  private int selectedItemNr = 0;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    WinApp.trainer.setSelectedScenarioNr(selectedItemNr);
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

    return sepukuPlay.getScenarios().getLayoutTitles();
  }

}