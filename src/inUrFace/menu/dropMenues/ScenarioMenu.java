package inUrFace.menu.dropMenues;


import sepuku.WinApp;
import util.Dialogs;
import util.time.Timeable;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.win;

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
    saveScenario.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK)
    );
    saveScenario.addActionListener((ActionEvent e) ->
      sepukuPlay.getInput().saveLayout(
        inputLayoutName(),
        WinApp.getCanvas().getDisplayedLayout()
      )
    );
  }

  private void setupRenameScenarioMenu() {

    add(renameScenario);
    renameScenario.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK)
    );
    renameScenario.addActionListener((ActionEvent e) ->
      sepukuPlay.getInput().renameSelectedLayout(inputLayoutName())
    );
  }

  private void setupRemoveScenarioMenu() {

    add(removeScenario);
    removeScenario.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)
    );
    removeScenario.addActionListener((ActionEvent e) -> {
      if (sepukuPlay.getScenarios().getSelectedScenariosNr() > 0 ) {
        if (Dialogs.confirmed("Really delete scenario?", win)) {
          sepukuPlay.getScenarios().deleteSelectedScenario();
        }
      }
    });
  }

  @Override
  public void timerUpdate () {

    removeScenario.setEnabled(
      sepukuPlay != null &&
        sepukuPlay.getScenarios().isEditing() &&
        sepukuPlay.getScenarios().getSelectedScenariosNr() > 0
    );
    removeScenario.setVisible(removeScenario.isEnabled());
    renameScenario.setEnabled(
      sepukuPlay != null &&
        sepukuPlay.getScenarios().isEditing() &&
        sepukuPlay.getScenarios().getSelectedScenariosNr() > 0
    );
    renameScenario.setVisible(renameScenario.isEnabled());
  }

}