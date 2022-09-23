package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.engine.core.trainer.Bot;
import bg.engine.core.trainer.Trainer;
import bg.inUrFace.windows.BotEditor;
import bg.util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

public class EditBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    new BotEditor(Trainer.bots.get(selectedItemNr));
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Edit bot";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}
