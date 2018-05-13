package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static bg.Main.settings;

public class LookaheadReportMenu extends JMenu {

  JRadioButtonMenuItem reportOn = new JRadioButtonMenuItem("On", settings.isSearchReportOn());
  JRadioButtonMenuItem reportOff = new JRadioButtonMenuItem("Off", !settings.isSearchReportOn());
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
    reportOn.addActionListener((ActionEvent e) -> settings.setSearchReportOn(true));
  }

  public void setupReportOff () {

    add(reportOff);
    reportOff.addActionListener((ActionEvent e) -> settings.setSearchReportOn(false));
  }

}
