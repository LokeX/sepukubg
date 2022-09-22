package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static bg.Main.engineApi;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class MoveCompletionMenu extends JMenu {

  JRadioButtonMenuItem manualMoves =
    new JRadioButtonMenuItem(
      "Manual completion",
      !engineApi.getSettings().isAutoCompleteMoves()
          && !engineApi.getSettings().isAutoCompletePartMoves()
    );
  JRadioButtonMenuItem automateMoves =
    new JRadioButtonMenuItem(
      "Auto complete forced moves",
      engineApi.getSettings().isAutoCompleteMoves()
    );
  JRadioButtonMenuItem automatePartMoves =
    new JRadioButtonMenuItem(
      "Auto complete forced partmoves",
      engineApi.getSettings().isAutoCompletePartMoves()
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
        engineApi.getSettings().setAutoMoves(0);
    });
  }

  private void setupAutomateMoves () {

    add(automateMoves);
    automateMoves.addActionListener((ActionEvent e) -> {
        engineApi.getSettings().setAutoMoves(1);
    });
  }

  private void setupAutomatePartMoves () {

    add(automatePartMoves);
    automatePartMoves.addActionListener((ActionEvent e) -> {
        engineApi.getSettings().setAutoMoves(2);
    });
  }

}
