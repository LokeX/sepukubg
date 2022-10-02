package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static sepuku.App.playSepuku;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class TurnProgressionMenu extends JMenu {

  JRadioButtonMenuItem automateEndTurn =
    new JRadioButtonMenuItem(
      "Automatic",
      playSepuku.getSettings().isAutomatedEndTurn()
    );
  JRadioButtonMenuItem manualEndTurn =
    new JRadioButtonMenuItem(
      "Manual",
      !playSepuku.getSettings().isAutomatedEndTurn()
    );
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
        playSepuku.getSettings().setAutomateEndTurn(true);
    });
  }

  private void setupManualEndTurn () {

    add(manualEndTurn);
    manualEndTurn.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setAutomateEndTurn(false);
    });
  }

}
