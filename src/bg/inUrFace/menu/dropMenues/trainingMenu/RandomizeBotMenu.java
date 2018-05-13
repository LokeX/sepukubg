package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.engine.trainer.Bot;
import bg.engine.trainer.Trainer;
import bg.util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

public class RandomizeBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    this.selectedItemNr = selectedItemNr;
//    Trainer.blackBot = selectedItemNr;
    Trainer.bots.add(new Bot(Trainer.bots.get(selectedItemNr)));
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Randomize bot";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::getName).collect(Collectors.toList());
  }

}
