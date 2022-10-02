package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static sepuku.App.playSepuku;

public class LookaheadReportMenu extends JMenu {

  JRadioButtonMenuItem reportOn =
    new JRadioButtonMenuItem(
      "On",
      playSepuku.getSettings().isSearchReportOn()
    );
  JRadioButtonMenuItem reportOff =
    new JRadioButtonMenuItem(
      "Off",
      !playSepuku.getSettings().isSearchReportOn()
    );
  ButtonGroup buttonGroup = new ButtonGroup();

  public LookaheadReportMenu() {

    super("Lookahead report");
    setupReportOn();
    setupReportOff();
    setupButtonGroup();
  }

  public void setupButtonGroup () {

    buttonGroup.add(reportOn);
    buttonGroup.add(reportOff);
  }

  public void setupReportOn () {

    add(reportOn);
    reportOn.addActionListener(
      (ActionEvent e) -> playSepuku.getSettings().setSearchReportOn(true)
    );
  }

  public void setupReportOff () {

    add(reportOff);
    reportOff.addActionListener(
      (ActionEvent e) -> playSepuku.getSettings().setSearchReportOn(false)
    );
  }

}
