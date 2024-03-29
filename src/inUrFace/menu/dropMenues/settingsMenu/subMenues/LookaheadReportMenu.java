package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static sepuku.WinApp.sepukuPlay;

public class LookaheadReportMenu extends JMenu {

  JRadioButtonMenuItem reportOn =
    new JRadioButtonMenuItem(
      "On",
      sepukuPlay.settings().isSearchReportOn()
    );
  JRadioButtonMenuItem reportOff =
    new JRadioButtonMenuItem(
      "Off",
      !sepukuPlay.settings().isSearchReportOn()
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
      (ActionEvent e) -> sepukuPlay.settings().setSearchReportOn(true)
    );
  }

  public void setupReportOff () {

    add(reportOff);
    reportOff.addActionListener(
      (ActionEvent e) -> sepukuPlay.settings().setSearchReportOn(false)
    );
  }

}