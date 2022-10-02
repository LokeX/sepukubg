package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static sepuku.App.playSepuku;

public class LookaheadLimitMenu extends JMenu {

  JRadioButtonMenuItem limit3 =
    new JRadioButtonMenuItem(
      "3 best moves",
      playSepuku.getSettings().getNrOfMovesToSearch() == 3
    );
  JRadioButtonMenuItem limit5 =
    new JRadioButtonMenuItem(
      "5 best moves",
      playSepuku.getSettings().getNrOfMovesToSearch() == 5
    );
  JRadioButtonMenuItem limit10 =
    new JRadioButtonMenuItem(
      "10 best moves",
      playSepuku.getSettings().getNrOfMovesToSearch() == 10
    );
  ButtonGroup limitChoices = new ButtonGroup();

  public LookaheadLimitMenu() {

    super("Lookahead Limit");
    setupButtonGroup();
    setupLimit3();
    setupLimit5();
    setupLimit10();
  }

  private void setupButtonGroup () {

    limitChoices.add(limit3);
    limitChoices.add(limit5);
    limitChoices.add(limit10);
  }

    private void setupLimit3() {

    add(limit3);
    limit3.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setNrOfMovesToSearch(3);
    });
  }

  private void setupLimit5() {

    add(limit5);
    limit5.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setNrOfMovesToSearch(5);
    });
  }

  private void setupLimit10() {

    add(limit10);
    limit10.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setNrOfMovesToSearch(10);
    });
  }

}
