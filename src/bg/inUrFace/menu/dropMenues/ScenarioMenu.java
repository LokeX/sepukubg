package bg.inUrFace.menu.dropMenues;


import bg.Main;
import bg.util.time.Timeable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import static bg.Main.sepuku;
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
  
  public String inputLayoutName () {
    
    return JOptionPane.
      showInputDialog(win, "Input scenario name:");
  }
  
  private void setupSaveScenarioMenu() {

    add(saveScenario);
    saveScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    saveScenario.addActionListener((ActionEvent e) -> {
      sepuku.getInput().saveLayout(
        inputLayoutName(),
        Main.getCanvas().getDisplayedLayout()
      );
    });
  }

  private void setupRenameScenarioMenu() {

    add(renameScenario);
    renameScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
    renameScenario.addActionListener((ActionEvent e) -> {
      sepuku.getInput().renameSelectedLayout(inputLayoutName());
    });
  }

  private void setupRemoveScenarioMenu() {

    add(removeScenario);
    removeScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    removeScenario.addActionListener((ActionEvent e) -> {
      if (sepuku.getScenarios().getSelectedScenariosNr() > 0 ) {
        if (bg.util.Dialogs.confirmed("Really delete scenario?", win)) {
          sepuku.getScenarios().deleteSelectedScenario();
        }
      }
    });
  }

  @Override
  public void timerUpdate () {

    removeScenario.setEnabled(
      sepuku != null &&
        sepuku.gameIsPlaying() &&
        sepuku.getScenarios().getSelectedScenariosNr() > 0
    );
    removeScenario.setVisible(removeScenario.isEnabled());
    renameScenario.setEnabled(
      sepuku != null &&
        sepuku.gameIsPlaying() &&
        sepuku.getScenarios().getSelectedScenariosNr() > 0
    );
    renameScenario.setVisible(renameScenario.isEnabled());
  }

}
