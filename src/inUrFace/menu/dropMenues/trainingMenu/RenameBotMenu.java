package inUrFace.menu.dropMenues.trainingMenu;

import engine.core.trainer.Bot;
import engine.core.trainer.Trainer;
import util.Dialogs;
import util.menus.Listable;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

import static sepuku.App.win;

public class RenameBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction (int selectedItemNr) {

    if (selectedItemNr > 1) {

      String name = JOptionPane.showInputDialog(win, "Input bot name:");

      if (name != null && name.length() > 0 && !Trainer.isBotMember(name)) {
        Trainer.bots.get(selectedItemNr).setName(name);
      } else {
        Dialogs.showMessage("Bot name is already in use!", win);
      }
    }
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Rename bot";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}
