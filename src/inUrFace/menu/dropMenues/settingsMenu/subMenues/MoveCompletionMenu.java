package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static sepuku.App.playSepuku;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class MoveCompletionMenu extends JMenu {

  JRadioButtonMenuItem manualMoves =
    new JRadioButtonMenuItem(
      "Manual completion",
      !playSepuku.getSettings().isAutoCompleteMoves()
          && !playSepuku.getSettings().isAutoCompletePartMoves()
    );
  JRadioButtonMenuItem automateMoves =
    new JRadioButtonMenuItem(
      "Auto complete forced moves",
      playSepuku.getSettings().isAutoCompleteMoves()
    );
  JRadioButtonMenuItem automatePartMoves =
    new JRadioButtonMenuItem(
      "Auto complete forced partmoves",
      playSepuku.getSettings().isAutoCompletePartMoves()
    );
  ButtonGroup moveCompletionChoices = new ButtonGroup();

  public MoveCompletionMenu () {

    super("Moves Completion");
    setupButtonGroup();
    setupManualMoves();
    setupAutomateMoves();
    setupAutomatePartMoves();
  }

  private void setupButtonGroup () {

    moveCompletionChoices.add(manualMoves);
    moveCompletionChoices.add(automateMoves);
    moveCompletionChoices.add(automatePartMoves);
  }

  private void setupManualMoves () {

    add(manualMoves);
    manualMoves.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setAutoMoves(0);
    });
  }

  private void setupAutomateMoves () {

    add(automateMoves);
    automateMoves.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setAutoMoves(1);
    });
  }

  private void setupAutomatePartMoves () {

    add(automatePartMoves);
    automatePartMoves.addActionListener((ActionEvent e) -> {
        playSepuku.getSettings().setAutoMoves(2);
    });
  }

}
