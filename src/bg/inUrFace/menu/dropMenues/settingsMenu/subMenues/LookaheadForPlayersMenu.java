package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.Main.settings;

public class LookaheadForPlayersMenu extends JMenu {

  JRadioButtonMenuItem allPlayers =
    new JRadioButtonMenuItem("All players", settings.getLookaheadForAllPlayers());
  JRadioButtonMenuItem botsOnly =
    new JRadioButtonMenuItem("Bots only", !settings.getLookaheadForAllPlayers());
  ButtonGroup playerChoices = new ButtonGroup();

  public LookaheadForPlayersMenu () {

    super("Lookahead for");
    setupButtonGroup();
    setupAllPlayers();
    setupBotsOnly();
  }

  private void setupButtonGroup () {

    playerChoices.add(allPlayers);
    playerChoices.add(botsOnly);
  }

  private void setupAllPlayers() {

    add(allPlayers);
    allPlayers.addActionListener((ActionEvent e) -> {
        settings.setLookaheadForAllPlayers(true);
    });
  }

  private void setupBotsOnly() {

    add(botsOnly);
    botsOnly.addActionListener((ActionEvent e) -> {
        settings.setLookaheadForAllPlayers(false);
    });
  }

}
