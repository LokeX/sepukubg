package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static bg.Main.engineApi;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class TurnProgressionMenu extends JMenu {

  JRadioButtonMenuItem automateEndTurn =
    new JRadioButtonMenuItem(
      "Automatic",
      engineApi.getSettings().isAutomatedEndTurn()
    );
  JRadioButtonMenuItem manualEndTurn =
    new JRadioButtonMenuItem(
      "Manual",
      !engineApi.getSettings().isAutomatedEndTurn()
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
        engineApi.getSettings().setAutomateEndTurn(true);
    });
  }

  private void setupManualEndTurn () {

    add(manualEndTurn);
    manualEndTurn.addActionListener((ActionEvent e) -> {
        engineApi.getSettings().setAutomateEndTurn(false);
    });
  }

}
