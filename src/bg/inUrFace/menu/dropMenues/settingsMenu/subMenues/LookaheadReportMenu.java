package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static bg.Main.sepuku;

public class LookaheadReportMenu extends JMenu {

  JRadioButtonMenuItem reportOn =
    new JRadioButtonMenuItem(
      "On",
      sepuku.getSettings().isSearchReportOn()
    );
  JRadioButtonMenuItem reportOff =
    new JRadioButtonMenuItem(
      "Off",
      !sepuku.getSettings().isSearchReportOn()
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
      (ActionEvent e) -> sepuku.getSettings().setSearchReportOn(true)
    );
  }

  public void setupReportOff () {

    add(reportOff);
    reportOff.addActionListener(
      (ActionEvent e) -> sepuku.getSettings().setSearchReportOn(false)
    );
  }

}
