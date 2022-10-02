package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static sepuku.App.playSepuku;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class PlayersMenu extends JMenu {

  JMenu whitePlayerMenu = new JMenu("White player");
  JRadioButtonMenuItem whiteHuman =
    new JRadioButtonMenuItem(
      "Human",
      playSepuku.getSettings().playerStatus[0] == 0
    );
  JRadioButtonMenuItem whiteComputer =
    new JRadioButtonMenuItem(
      "Computer",
      playSepuku.getSettings().playerStatus[0] == 1
    );
  ButtonGroup whitePlayer = new ButtonGroup();
  JMenu blackPlayerMenu = new JMenu("Black player");
  JRadioButtonMenuItem blackHuman =
    new JRadioButtonMenuItem(
      "Human",
      playSepuku.getSettings().playerStatus[1] == 0
    );
  JRadioButtonMenuItem blackComputer =
    new JRadioButtonMenuItem(
      "Computer",
      playSepuku.getSettings().playerStatus[1] == 1
    );
  ButtonGroup blackPlayer = new ButtonGroup();

  public PlayersMenu () {

    super("Players");
    setupbuttonGroup();
    setupPlayersMenu();
    setupWhiteHuman();
    setupWhiteComputer();
    setupBlackHuman();
    setupBlackComputer();
  }

  private void setupbuttonGroup () {

    whitePlayer.add(whiteHuman);
    whitePlayer.add(whiteComputer);
    blackPlayer.add(blackHuman);
    blackPlayer.add(blackComputer);
  }
  private void setupPlayersMenu () {

    add(whitePlayerMenu);
    add(blackPlayerMenu);
  }

  private void setupWhiteHuman () {

    whitePlayerMenu.add(whiteHuman);
    whiteHuman.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setPlayerStatus(0, 0);
    });
  }

  private void setupWhiteComputer () {

    whitePlayerMenu.add(whiteComputer);
    whiteComputer.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setPlayerStatus(0, 1);
    });
  }

  private void setupBlackHuman () {

    blackPlayerMenu.add(blackHuman);
    blackHuman.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setPlayerStatus(1, 0);
    });
  }

  private void setupBlackComputer () {

    blackPlayerMenu.add(blackComputer);
    blackComputer.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setPlayerStatus(1, 1);
    });
  }

}
