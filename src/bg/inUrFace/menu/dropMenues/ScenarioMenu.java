package bg.inUrFace.menu.dropMenues;

import static bg.Main.scenarios;

import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.util.time.Timeable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import static bg.Main.engineApi;
import static bg.Main.win;

public class ScenarioMenu extends JMenu implements Timeable {

  JMenuItem saveScenario = new JMenuItem("Save scenario");
  JMenuItem renameScenario = new JMenuItem("Rename scenario");
  JMenuItem removeScenario = new JMenuItem("Remove scenario");

  public ScenarioMenu () {

    super("Scenarios");
    setupSaveScenarioMenu();
    setupRenameScenarioMenu();
    setupRemoveScenarioMenu();
  }

  private void setupSaveScenarioMenu() {

    add(saveScenario);
    saveScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    saveScenario.addActionListener((ActionEvent e) -> {
      engineApi.getInput().saveLayout();
    });
  }

  private void setupRenameScenarioMenu() {

    add(renameScenario);
    renameScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
    renameScenario.addActionListener((ActionEvent e) -> {
      engineApi.getInput().renameSelectedLayout();
    });
  }

  private void setupRemoveScenarioMenu() {

    add(removeScenario);
    removeScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    removeScenario.addActionListener((ActionEvent e) -> {
      if (scenarios.getSelectedScenariosNr() > 0 ) {
        if (bg.util.Dialogs.confirmed("Really delete scenario?", win)) {
          scenarios.deleteSelectedScenario();
          new ScenarioOutput().outputSelectedScenario();
        }
      }
    });
  }

  @Override
  public void timerUpdate () {

    removeScenario.setEnabled(
      engineApi != null &&
        engineApi.getSelectedTurn() == null &&
        scenarios.getSelectedScenariosNr() > 0
    );
    removeScenario.setVisible(removeScenario.isEnabled());
    renameScenario.setEnabled(
      engineApi != null &&
        engineApi.getSelectedTurn() == null &&
        scenarios.getSelectedScenariosNr() > 0
    );
    renameScenario.setVisible(renameScenario.isEnabled());
  }

}
