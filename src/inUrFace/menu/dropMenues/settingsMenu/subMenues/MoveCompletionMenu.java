package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static sepuku.WinApp.sepukuPlay;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class MoveCompletionMenu extends JMenu {

  JRadioButtonMenuItem manualMoves =
    new JRadioButtonMenuItem(
      "Manual completion",
      !sepukuPlay.getSettings().isAutoCompleteMoves()
          && !sepukuPlay.getSettings().isAutoCompletePartMoves()
    );
  JRadioButtonMenuItem automateMoves =
    new JRadioButtonMenuItem(
      "Auto complete forced moves",
      sepukuPlay.getSettings().isAutoCompleteMoves()
    );
  JRadioButtonMenuItem automatePartMoves =
    new JRadioButtonMenuItem(
      "Auto complete forced partmoves",
      sepukuPlay.getSettings().isAutoCompletePartMoves()
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
        sepukuPlay.getSettings().setAutoMoves(0);
    });
  }

  private void setupAutomateMoves () {

    add(automateMoves);
    automateMoves.addActionListener((ActionEvent e) -> {
        sepukuPlay.getSettings().setAutoMoves(1);
    });
  }

  private void setupAutomatePartMoves () {

    add(automatePartMoves);
    automatePartMoves.addActionListener((ActionEvent e) -> {
        sepukuPlay.getSettings().setAutoMoves(2);
    });
  }

}