package bg.inUrFace.menu.dropMenues;

import bg.Main;
import bg.util.time.Timeable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import static bg.Main.sepuku;
import static bg.Main.mouse;

public class EditMenu extends JMenu implements Timeable {

  JMenuItem editDice = new JMenuItem("Input dice values");
  JMenuItem editMove = new JMenuItem("Input (undo) move");

  JMenuItem editPlayToScore = new JMenuItem("Input matchScore to win engineApi");

  JPopupMenu.Separator editMenuSeparator = new JPopupMenu.Separator();

  JMenuItem editCubeValue = new JMenuItem("Input cube value");

  JMenu editCubeOwner = new JMenu("Set cube owner");
  JMenuItem noOwner = new JMenuItem("None");
  JMenuItem whitePlayerOwner = new JMenuItem("White player");
  JMenuItem blackPlayerOwner = new JMenuItem("Black player");

  JMenu editPlayerScore = new JMenu("Input player scoreBoard");
  JMenuItem whitePlayerScore = new JMenuItem("White player");
  JMenuItem blackPlayerScore = new JMenuItem("Black player");

  public EditMenu () {

    super("Edit");
    setupEditMove();
    setupEditDice();
    setupEditPlayToScore();
    setupEditPlayerScore();
    setupEditMenuSeparator();
    setupEditCubeValue();
    setupEditCubeOwner();
  }

  private void setupEditMove () {

    add(editMove);
    editMove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0));
    editMove.addActionListener((ActionEvent e) -> {
      sepuku.getHumanMove().startMove();
    });
  }

  private void setupEditDice () {

    add(editDice);
    editDice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
    editDice.addActionListener((ActionEvent e) -> {
      sepuku.getInput().inputNewDice();
    });
  }

  private void setupEditPlayToScore () {

    add(editPlayToScore);
    editPlayToScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.ALT_MASK));
    editPlayToScore.addActionListener((ActionEvent e) -> {
      sepuku.getInput().inputPlayToScore();
    });
  }

  private void setupEditMenuSeparator() {

    add(editMenuSeparator);
  }

  private void setupEditCubeValue () {

    add(editCubeValue);
    editCubeValue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    editCubeValue.addActionListener((ActionEvent e) -> {
      sepuku.getInput().inputCubeValue();
    });
  }

  private void setupEditCubeOwner () {

    add(editCubeOwner);
    setupEditNoCubeOwner();
    setupEditWhitePlayerOwner();
    setupEditBlackPlayerOwner();
  }

  private void setupEditNoCubeOwner () {

//    add(editCubeOwner);
    editCubeOwner.add(noOwner);
    noOwner.addActionListener((ActionEvent e) -> {
      sepuku.getGame().getGameCube().setOwner(-1);
    });
  }

  private void setupEditWhitePlayerOwner () {

    editCubeOwner.add(whitePlayerOwner);
    whitePlayerOwner.addActionListener((ActionEvent e) -> {
      sepuku.getGame().getGameCube().setOwner(0);
    });
  }

  private void setupEditBlackPlayerOwner () {

    editCubeOwner.add(blackPlayerOwner);
    blackPlayerOwner.addActionListener((ActionEvent e) -> {
      sepuku.getGame().getGameCube().setOwner(1);
    });
  }

  private void setupEditPlayerScore () {

    add(editPlayerScore);
    setupEditWhitePlayerScore();
    setupEditBlackPlayerScore();
  }

  private void setupEditWhitePlayerScore () {

    editPlayerScore.add(whitePlayerScore);
    whitePlayerScore.addActionListener((ActionEvent e) -> {
      sepuku.getInput().inputPlayerMatchScore(0);
    });
  }

  private void setupEditBlackPlayerScore () {

    editPlayerScore.add(blackPlayerScore);
    blackPlayerScore.addActionListener((ActionEvent e) -> {
      sepuku.getInput().inputPlayerMatchScore(1);
    });
  }

  @Override
  public void timerUpdate() {

    editDice.setEnabled(sepuku != null && sepuku.getGame() != null && sepuku.gameIsPlaying());
    editDice.setVisible(editDice.isEnabled());
    editMove.setEnabled(sepuku != null && sepuku.gameIsPlaying());
    editMove.setVisible(editMove.isEnabled());
    editPlayerScore.setEnabled(editMove.isEnabled());
    editPlayerScore.setVisible(editPlayerScore.isEnabled());

    editMenuSeparator.setEnabled(
      mouse != null && Main.mouse.doublingCube.isVisible()
    );
    editMenuSeparator.setVisible(editMenuSeparator.isEnabled());
    editCubeValue.setEnabled(editMenuSeparator.isEnabled());
    editCubeValue.setVisible(editMenuSeparator.isEnabled());
    editCubeOwner.setEnabled(editMenuSeparator.isEnabled());
    editCubeOwner.setVisible(editMenuSeparator.isEnabled());
  }

}
