package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import bg.engine.trainer.Bot;
import bg.engine.trainer.Trainer;
import bg.util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

import static bg.Main.settings;

public class BlackBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    settings.setBlackBotOpponent(selectedItemNr);
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Black bot opponent";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::getName).collect(Collectors.toList());
  }

}
