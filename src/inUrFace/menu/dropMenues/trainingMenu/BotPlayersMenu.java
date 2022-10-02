package inUrFace.menu.dropMenues.trainingMenu;

import engine.core.trainer.Trainer;
import util.time.Timeable;
import util.menus.RadioButtonListMenu;

import javax.swing.*;

public class BotPlayersMenu extends JMenu implements Timeable {

  RadioButtonListMenu selectWhiteBot = new RadioButtonListMenu(new WhiteBotMenu());
  RadioButtonListMenu selectBlackBot = new RadioButtonListMenu(new BlackBotMenu());

  public BotPlayersMenu(String title) {

    super(title);
    setupBotPlayersMenu();
  }

  public void setupBotPlayersMenu () {

    setupSelectWhiteBot();
    setupSelectBlackBot();
  }

  public void setupSelectWhiteBot () {

    add(selectWhiteBot);
  }

  public void setupSelectBlackBot () {

    add(selectBlackBot);
  }

  @Override
  public void timerUpdate() {

    selectBlackBot.setEnabled(!Trainer.running);
    selectWhiteBot.setEnabled(!Trainer.running);
    selectBlackBot.setVisible(!Trainer.running);
    selectWhiteBot.setVisible(!Trainer.running);
    selectWhiteBot.updateList();
    selectBlackBot.updateList();
  }

}
