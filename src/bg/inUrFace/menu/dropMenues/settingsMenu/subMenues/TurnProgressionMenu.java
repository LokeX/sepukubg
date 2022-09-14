package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static bg.Main.settings;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class TurnProgressionMenu extends JMenu {

  JRadioButtonMenuItem automateEndTurn = new JRadioButtonMenuItem("Automatic", settings.isAutomatedEndTurn());
  JRadioButtonMenuItem manualEndTurn = new JRadioButtonMenuItem("Manual", !settings.isAutomatedEndTurn());
  ButtonGroup endTurnChoices = new ButtonGroup();

  public TurnProgressionMenu () {

    super("Turn Progression");
    setupButtonGroup();
    setupAutomateEndTurn();
    setupManualEndTurn();
  }

  private void setupButtonGroup () {

    endTurnChoices.add(automateEndTurn);
    endTurnChoices.add(manualEndTurn);
  }

  private void setupAutomateEndTurn () {

    add(automateEndTurn);
    automateEndTurn.addActionListener((ActionEvent e) -> {
        settings.setAutomateEndTurn(true);
    });
  }

  private void setupManualEndTurn () {

    add(manualEndTurn);
    manualEndTurn.addActionListener((ActionEvent e) -> {
        settings.setAutomateEndTurn(false);
    });
  }

}
