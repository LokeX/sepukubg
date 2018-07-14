package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static bg.Main.settings;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class MoveDelayMenu extends JMenu {

  JRadioButtonMenuItem milli500 = new JRadioButtonMenuItem("500 milliseconds", settings.getShowMoveDelay() == 500);
  JRadioButtonMenuItem milli300 = new JRadioButtonMenuItem("300 milliseconds", settings.getShowMoveDelay() == 300);
  JRadioButtonMenuItem milli150 = new JRadioButtonMenuItem("150 milliseconds", settings.getShowMoveDelay() == 150);
  JRadioButtonMenuItem milli0 = new JRadioButtonMenuItem("Show move immediately", settings.getShowMoveDelay() == 0);
  ButtonGroup showMoveChoices = new ButtonGroup();

  public MoveDelayMenu() {

    super("MoveSelector delay");
    setupButtonGroup();
    setupMilli500();
    setupMilli300();
    setupMilli150();
    setupMilli0();
  }

  private void setupButtonGroup () {

    showMoveChoices.add(milli500);
    showMoveChoices.add(milli300);
    showMoveChoices.add(milli150);
    showMoveChoices.add(milli0);
  }

    private void setupMilli500 () {

    add(milli500);
    milli500.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setShowMoveDelay(500);
      }
    });
  }

  private void setupMilli300 () {

    add(milli300);
    milli300.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setShowMoveDelay(300);
      }
    });
  }

  private void setupMilli150 () {

    add(milli150);
    milli150.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setShowMoveDelay(150);
      }
    });
  }

  private void setupMilli0 () {

    add(milli0);
    milli0.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setShowMoveDelay(0);
      }
    });
  }

}
