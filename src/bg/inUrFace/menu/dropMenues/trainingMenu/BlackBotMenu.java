package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.engine.coreLogic.trainer.Bot;
import bg.engine.coreLogic.trainer.Trainer;
import bg.util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

import static bg.Main.engineApi;

public class BlackBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
    Trainer.blackBot = selectedItemNr;
    engineApi.getSettings().setTrainerBlackBot(selectedItemNr);
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Black bot";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}
