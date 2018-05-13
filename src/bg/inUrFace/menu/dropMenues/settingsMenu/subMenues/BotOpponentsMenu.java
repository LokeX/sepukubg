package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import bg.util.time.Timeable;
import bg.util.menus.RadioButtonListMenu;

import javax.swing.*;

public class BotOpponentsMenu extends JMenu implements Timeable {

  RadioButtonListMenu blackBot = new RadioButtonListMenu(new BlackBotMenu());
  RadioButtonListMenu whiteBot = new RadioButtonListMenu(new WhiteBotMenu());

  public BotOpponentsMenu() {

    super("Bot opponents");
    setupBotOpponentsMenu();
  }

  private void setupBotOpponentsMenu () {

    add(blackBot);
    add(whiteBot);
  }

  @Override
  public void timerUpdate() {

    blackBot.updateList();
    whiteBot.updateList();
  }
}
