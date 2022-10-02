package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static sepuku.App.playSepuku;

public class LookaheadForPlayersMenu extends JMenu {

  JRadioButtonMenuItem allPlayers =
    new JRadioButtonMenuItem(
      "All players",
      playSepuku.getSettings().getLookaheadForAllPlayers()
    );
  JRadioButtonMenuItem botsOnly =
    new JRadioButtonMenuItem(
      "Bots only",
      !playSepuku.getSettings().getLookaheadForAllPlayers()
    );
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
        playSepuku.getSettings().setLookaheadForAllPlayers(true);
    });
  }

  private void setupBotsOnly() {

    add(botsOnly);
    botsOnly.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setLookaheadForAllPlayers(false);
    });
  }

}
