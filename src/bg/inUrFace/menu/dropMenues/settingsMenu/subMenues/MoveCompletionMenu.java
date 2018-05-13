package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static bg.Main.settings;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class MoveCompletionMenu extends JMenu {

  JRadioButtonMenuItem manualMoves = new JRadioButtonMenuItem("Manual completion",!settings.isAutoCompleteMoves() && !settings.isAutoCompletePartMoves());
  JRadioButtonMenuItem automateMoves = new JRadioButtonMenuItem("Auto complete forced moves", settings.isAutoCompleteMoves());
  JRadioButtonMenuItem automatePartMoves = new JRadioButtonMenuItem("Auto complete forced partmoves (In test)", settings.isAutoCompletePartMoves());
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
    manualMoves.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setAutoMoves(0);
      }
    });
  }

  private void setupAutomateMoves () {

    add(automateMoves);
    automateMoves.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setAutoMoves(1);
      }
    });
  }

  private void setupAutomatePartMoves () {

    add(automatePartMoves);
    automatePartMoves.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setAutoMoves(2);
      }
    });
  }

}
