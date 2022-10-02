package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import engine.core.trainer.Bot;
import engine.core.trainer.Trainer;
import util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

import static sepuku.WinApp.sepukuPlay;


public class WhiteBotMenu implements Listable {

  private int selectedItemNr = Trainer.whiteBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    sepukuPlay.getSettings().setWhiteBotOpponent(selectedItemNr);
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "White bot opponent";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}