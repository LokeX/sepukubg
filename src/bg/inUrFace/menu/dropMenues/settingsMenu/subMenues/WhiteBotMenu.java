package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import bg.engine.coreLogic.trainer.Bot;
import bg.engine.coreLogic.trainer.Trainer;
import bg.util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

import static bg.Main.settings;

public class WhiteBotMenu implements Listable {

  private int selectedItemNr = Trainer.whiteBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    settings.setWhiteBotOpponent(selectedItemNr);
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
