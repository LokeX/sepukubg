package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static bg.Main.sepuku;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class PlayerStartMenu extends JMenu {

  JRadioButtonMenuItem whitePlayerStart =
    new JRadioButtonMenuItem(
      "White player starts",
      sepuku.getSettings().getGameStartMode() == 0
    );
  JRadioButtonMenuItem blackPlayerStart =
    new JRadioButtonMenuItem(
      "Black player starts",
      sepuku.getSettings().getGameStartMode() == 1
    );
  JRadioButtonMenuItem throwDiceStart =
    new JRadioButtonMenuItem(
      "Throw dice",
      sepuku.getSettings().getGameStartMode() == 2
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
        sepuku.getSettings().setGameStartMode(0);
    });
  }

  private void setupBlackPlayerStart () {

    add(blackPlayerStart);
    blackPlayerStart.addActionListener((ActionEvent e) -> {
        sepuku.getSettings().setGameStartMode(1);
    });
  }

  private void setupThrowDiceStart () {

    add(throwDiceStart);
    throwDiceStart.addActionListener((ActionEvent e) -> {
        sepuku.getSettings().setGameStartMode(2);
    });
  }

}
