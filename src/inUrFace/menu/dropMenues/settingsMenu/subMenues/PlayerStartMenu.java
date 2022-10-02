package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static sepuku.App.playSepuku;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class PlayerStartMenu extends JMenu {

  JRadioButtonMenuItem whitePlayerStart =
    new JRadioButtonMenuItem(
      "White player starts",
      playSepuku.getSettings().getGameStartMode() == 0
    );
  JRadioButtonMenuItem blackPlayerStart =
    new JRadioButtonMenuItem(
      "Black player starts",
      playSepuku.getSettings().getGameStartMode() == 1
    );
  JRadioButtonMenuItem throwDiceStart =
    new JRadioButtonMenuItem(
      "Throw dice",
      playSepuku.getSettings().getGameStartMode() == 2
    );
  ButtonGroup startMethods = new ButtonGroup();

  public PlayerStartMenu () {

    super("Player start");
    setupButtonGroup();
    setupWhitePlayerStart();
    setupBlackPlayerStart();
    setupThrowDiceStart();
  }

  private void setupButtonGroup () {

    startMethods.add(whitePlayerStart);
    startMethods.add(blackPlayerStart);
    startMethods.add(throwDiceStart);
  }

  private void setupWhitePlayerStart () {

    add(whitePlayerStart);
    whitePlayerStart.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setGameStartMode(0);
    });
  }

  private void setupBlackPlayerStart () {

    add(blackPlayerStart);
    blackPlayerStart.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setGameStartMode(1);
    });
  }

  private void setupThrowDiceStart () {

    add(throwDiceStart);
    throwDiceStart.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setGameStartMode(2);
    });
  }

}
