package inUrFace.menu.dropMenues;

import sepuku.WinApp;
import util.time.Timeable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import static sepuku.WinApp.*;
import static util.Dialogs.getIntegerInput;

public class EditMenu extends JMenu implements Timeable {

  JMenuItem editDice = new JMenuItem("Input dice values");
  JMenuItem editMove = new JMenuItem("Input (undo) move");

  JMenuItem editPlayToScore = new JMenuItem("Input matchScore to win");

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
      sepukuPlay.humanMove().startMove();
    });
  }

  private void setupEditDice () {

    add(editDice);
    editDice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
    editDice.addActionListener((ActionEvent e) -> {
      sepukuPlay.stateEdit().inputNewDice(
        JOptionPane.showInputDialog(win,
          "Input dice values with no separation" +
            "\nfor doubles type only one value"
        )
      );
    });
  }

  private void setupEditPlayToScore () {

    add(editPlayToScore);
    editPlayToScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.ALT_MASK));
    editPlayToScore.addActionListener((ActionEvent e) -> {
      sepukuPlay.stateEdit().inputPlayToScore(
        getIntegerInput(
          "Type the score required to win the game:" +
            "\nType 0 (or hit Enter) for money-game",win
        )
      );
    });
  }

  private void setupEditMenuSeparator() {

    add(editMenuSeparator);
  }

  private void setupEditCubeValue () {

    add(editCubeValue);
    editCubeValue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    editCubeValue.addActionListener((ActionEvent e) -> {
      sepukuPlay.stateEdit().inputCubeValue(
        getIntegerInput(
          "Type the new doublingCube value:",win
        )
      );
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
      sepukuPlay.gameCube().setOwner(-1);
    });
  }

  private void setupEditWhitePlayerOwner () {

    editCubeOwner.add(whitePlayerOwner);
    whitePlayerOwner.addActionListener((ActionEvent e) -> {
      sepukuPlay.gameCube().setOwner(0);
    });
  }

  private void setupEditBlackPlayerOwner () {

    editCubeOwner.add(blackPlayerOwner);
    blackPlayerOwner.addActionListener((ActionEvent e) -> {
      sepukuPlay.gameCube().setOwner(1);
    });
  }

  private void setupEditPlayerScore () {

    add(editPlayerScore);
    setupEditWhitePlayerScore();
    setupEditBlackPlayerScore();
  }
  
  private int inputScore (int playerID) {
    
    return getIntegerInput(
      "Type "+(playerID == 0 ? "White" : "Black")+" players score:",win
    );
  }
  
  private void setupEditWhitePlayerScore () {

    editPlayerScore.add(whitePlayerScore);
    whitePlayerScore.addActionListener((ActionEvent e) -> {
      sepukuPlay.stateEdit().inputPlayerMatchScore(0, inputScore(0));
    });
  }

  private void setupEditBlackPlayerScore () {

    editPlayerScore.add(blackPlayerScore);
    blackPlayerScore.addActionListener((ActionEvent e) -> {
      sepukuPlay.stateEdit().inputPlayerMatchScore(1, inputScore(1));
    });
  }

  @Override
  public void timerUpdate() {

    editDice.setEnabled(sepukuPlay.gameIsPlaying());
    editDice.setVisible(editDice.isEnabled());
    editMove.setEnabled(sepukuPlay.gameIsPlaying());
    editMove.setVisible(editMove.isEnabled());
    editPlayerScore.setEnabled(editMove.isEnabled());
    editPlayerScore.setVisible(editPlayerScore.isEnabled());

    editMenuSeparator.setEnabled(
      mouse != null && WinApp.mouse.cube.isVisible()
    );
    editMenuSeparator.setVisible(editMenuSeparator.isEnabled());
    editCubeValue.setEnabled(editMenuSeparator.isEnabled());
    editCubeValue.setVisible(editMenuSeparator.isEnabled());
    editCubeOwner.setEnabled(editMenuSeparator.isEnabled());
    editCubeOwner.setVisible(editMenuSeparator.isEnabled());
  }

}