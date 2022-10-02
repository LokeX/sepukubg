package inUrFace.menu.dropMenues.trainingMenu;

import engine.core.trainer.Bot;
import engine.core.trainer.Trainer;
import util.menus.Listable;
import java.util.List;
import java.util.stream.Collectors;

import static sepuku.WinApp.sepukuPlay;
import static util.Dialogs.confirmed;
import static sepuku.WinApp.win;

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
      if (sepukuPlay.getSettings().getWhiteBotOpponent() >= selectedItemNr) {
        sepukuPlay.getSettings().setWhiteBotOpponent(0);
      }
      if (sepukuPlay.getSettings().getBlackBotOpponent() >= selectedItemNr) {
        sepukuPlay.getSettings().setBlackBotOpponent(0);
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