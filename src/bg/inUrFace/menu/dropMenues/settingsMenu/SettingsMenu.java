package bg.inUrFace.menu.dropMenues.settingsMenu;

import bg.inUrFace.menu.dropMenues.settingsMenu.subMenues.*;
import bg.util.time.Timeable;

import javax.swing.JMenu;

public class SettingsMenu extends JMenu implements Timeable {

  BotOpponentsMenu botOpponentsMenu = new BotOpponentsMenu();

  public SettingsMenu () {

    super("Settings");
    setupSettingsMenu();
  }

  private void setupSettingsMenu () {

    add(new PlayerStartMenu());
    add(new PlayersMenu());
    add(botOpponentsMenu);
    addSeparator();
    add(new BonusDisplayMenu());
    add(new MoveDelayMenu());
    add(new MoveCompletionMenu());
    add(new TurnProgressionMenu());
    addSeparator();
    add(new LookaheadForPlayersMenu());
    add(new LookaheadLimitMenu());
    add(new LookaheadToTurnMenu());
    add(new LookaheadReportMenu());
  }

  @Override
  public void timerUpdate() {

    botOpponentsMenu.timerUpdate();
  }
}
