package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.engine.core.trainer.Bot;
import bg.engine.core.trainer.Trainer;
import bg.util.menus.Listable;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

import static bg.Main.win;

public class CloneBotMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    String name = JOptionPane.showInputDialog(win, "Input bot name:");

    if (name != null && name.length() > 0 && !Trainer.isBotMember(name)) {

      Bot newBot = new Bot(name);

      newBot.setElements(Trainer.bots.get(selectedItemNr));
      Trainer.bots.add(newBot);
    } else {
      bg.util.Dialogs.showMessage("Bot name is already in use!", win);
    }
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Clone bot";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}
