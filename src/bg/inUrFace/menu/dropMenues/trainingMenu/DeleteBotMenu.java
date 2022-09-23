package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.engine.core.trainer.Bot;
import bg.engine.core.trainer.Trainer;
import bg.util.menus.Listable;
import java.util.List;
import java.util.stream.Collectors;

import static bg.Main.sepuku;
import static bg.util.Dialogs.confirmed;
import static bg.Main.win;

public class DeleteBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    if (confirmed("Really delete bot?", win)) {
      Trainer.bots.remove(selectedItemNr);
      if (Trainer.whiteBot >= selectedItemNr) {
        Trainer.whiteBot = 0;
      }
      if (Trainer.blackBot >= selectedItemNr) {
        Trainer.blackBot = 0;
      }
      if (sepuku.getSettings().getWhiteBotOpponent() >= selectedItemNr) {
        sepuku.getSettings().setWhiteBotOpponent(0);
      }
      if (sepuku.getSettings().getBlackBotOpponent() >= selectedItemNr) {
        sepuku.getSettings().setBlackBotOpponent(0);
      }
    }
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Delete bot";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}
