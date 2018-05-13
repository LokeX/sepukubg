package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.Main.settings;

public class LookaheadLimitMenu extends JMenu {

  JRadioButtonMenuItem limit3 =
    new JRadioButtonMenuItem("3 best moves", settings.getNrOfMovesToSearch() == 3);
  JRadioButtonMenuItem limit5 =
    new JRadioButtonMenuItem("5 best moves", settings.getNrOfMovesToSearch() == 5);
  JRadioButtonMenuItem limit10 =
    new JRadioButtonMenuItem("10 best moves", settings.getNrOfMovesToSearch() == 10);
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
    limit3.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setNrOfMovesToSearch(3);
      }
    });
  }

  private void setupLimit5() {

    add(limit5);
    limit5.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setNrOfMovesToSearch(5);
      }
    });
  }

  private void setupLimit10() {

    add(limit10);
    limit10.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setNrOfMovesToSearch(10);
      }
    });
  }

}
