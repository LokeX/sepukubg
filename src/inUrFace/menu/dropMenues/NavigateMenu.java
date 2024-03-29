package inUrFace.menu.dropMenues;

import sepuku.WinApp;
import util.time.Timeable;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.KeyStroke;
import static sepuku.WinApp.sepukuPlay;

public class NavigateMenu extends JMenu implements Timeable {

  private JMenuItem previousHumanTurn = new JMenuItem("Previous human turn");
  private JMenuItem nextHumanTurn = new JMenuItem("Next human turn");
  private JMenuItem latestHumanTurn = new JMenuItem("Latest human turn");
  private Separator navigateMenuSeparator = new JPopupMenu.Separator();
  private JMenuItem previousTurn = new JMenuItem("Previous turn");
  private JMenuItem nextTurn = new JMenuItem("Next turn");
  private JMenuItem previousMove = new JMenuItem("Previous move");
  private JMenuItem nextMove = new JMenuItem("Next move");
  private JMenuItem previousPartMove = new JMenuItem("Previous part move");
  private JMenuItem nextPartMove = new JMenuItem("Next part move");
  private JMenuItem previousScenario = new JMenuItem("Previous scenario");
  private JMenuItem nextScenario = new JMenuItem("Next scenario");

  public NavigateMenu () {

    super("Navigation");
    setupPreviousHumanTurn();
    setupNextHumanTurn();
    setupLatestHumanTurn();
    setupNavigateMenuSeparator();
    setupPreviousTurn();
    setupNextTurn();
    setupPreviousMove();
    setupNextMove();
    setupNextPartMove();
    setupPreviousPartMove();
    setupPreviousScenario();
    setupNextScenario();
  }

  private void setupPreviousHumanTurn () {

    add(previousHumanTurn);
    previousHumanTurn
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_LEFT,
            InputEvent.SHIFT_DOWN_MASK
          )
      );
    previousHumanTurn
      .addActionListener((ActionEvent e) -> {
        sepukuPlay.navigation().selectAndOutputPreviousHumanTurn();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectPreviousHumanTurn()
//              .getPlayedMove()
//          );
      });
  }

  private void setupNextHumanTurn () {

    add(nextHumanTurn);
    nextHumanTurn
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_RIGHT,
            InputEvent.SHIFT_DOWN_MASK
          )
      );
    nextHumanTurn
      .addActionListener((ActionEvent e) -> {
        sepukuPlay.navigation().selectAndOutputNextHumanTurn();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectNextHumanTurn()
//              .getPlayedMove()
//          );
      });
  }

  private void setupLatestHumanTurn () {

    add(latestHumanTurn);
    latestHumanTurn
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_END,
            0
          )
      );
    latestHumanTurn
      .addActionListener((ActionEvent e) -> {
        sepukuPlay.navigation().selectAndOutputLatestHumanTurn();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectLatestHumanTurn()
//              .getPlayedMove()
//          );
      });
  }

  private void setupNavigateMenuSeparator() {

    add(navigateMenuSeparator);
  }

  private void setupNextTurn () {

    add(nextTurn);
    nextTurn
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_RIGHT,
            InputEvent.CTRL_DOWN_MASK
          )
      );
    nextTurn.addActionListener((ActionEvent e) -> {
      if (sepukuPlay.gameIsPlaying()) {
        sepukuPlay.navigation().selectAndOutputNextTurn();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectNextTurn()
//              .getPlayedMove()
//          );
      }
    });
  }

  private void setupPreviousTurn () {

    add(previousTurn);
    previousTurn
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_LEFT,
            InputEvent.CTRL_DOWN_MASK
          )
      );
    previousTurn.addActionListener((ActionEvent e) -> {
      if (sepukuPlay.gameIsPlaying()) {
        sepukuPlay.navigation().selectAndOutputPreviousTurn();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectPreviousTurn()
//              .getPlayedMove()
//          );
      }
    });
  }

  private void setupPreviousMove () {

    add(previousMove);
    previousMove
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_UP,
            0
          )
      );
    previousMove.addActionListener((ActionEvent e) -> {
      if (sepukuPlay.gameIsPlaying()) {
        sepukuPlay.navigation().selectAndOutputPreviousMove();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectPreviousMove()
//          );
      }
    });
  }

  private void setupNextMove () {

    add(nextMove);
    nextMove
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_DOWN,
            0
          )
      );
    nextMove.addActionListener((ActionEvent e) -> {
        sepukuPlay.navigation().selectAndOutputNextMove();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectNextMove()
//          );
    });
  }

  private void setupPreviousPartMove () {

    add(previousPartMove);
    previousPartMove
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_LEFT,
            0
          )
      );
    previousPartMove.addActionListener((ActionEvent e) -> {
      if (sepukuPlay.gameIsPlaying()) {
        sepukuPlay.navigation().selectAndOutputPreviousPartMove();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectPreviousPartMove()
//          );
      }
    });
  }

  private void setupNextPartMove () {

    add(nextPartMove);
    nextPartMove
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_RIGHT,
            0
          )
      );
    nextPartMove.addActionListener((ActionEvent e) -> {
        sepukuPlay.navigation().selectAndOutputNextPartMove();
//          .getMoveOutput()
//          .setOutputLayout(
//            sepukuPlay
//              .getGameState()
//              .selectNextPartMove()
//          );
    });
  }

  private void setupPreviousScenario () {

    add(previousScenario);
    previousScenario
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_LEFT,
            0
          )
      );
    previousScenario.addActionListener((ActionEvent e) -> {
      sepukuPlay.scenarios().selectPreviousScenario();
      WinApp.getCanvas().setDisplayedLayout(
        sepukuPlay.scenarios().getSelectedScenariosLayout()
      );
//      new ScenarioOutput(scenarios).outputSelectedScenario();
    });
  }

  private void setupNextScenario () {

    add(nextScenario);
    nextScenario
      .setAccelerator(
        KeyStroke
          .getKeyStroke(
            KeyEvent.VK_RIGHT,
            0
          )
      );
    nextScenario.addActionListener((ActionEvent e) -> {
      sepukuPlay.scenarios().selectNextScenario();
      WinApp.getCanvas().setDisplayedLayout(
        sepukuPlay.scenarios().getSelectedScenariosLayout()
      );
//      new ScenarioOutput(scenarios).outputSelectedScenario();
    });
  }

  @Override
  public void timerUpdate() {

    previousHumanTurn.setEnabled(
      sepukuPlay != null
      && sepukuPlay.gameIsPlaying()
      && sepukuPlay.settings().humanPlayerExists()
    );
    previousHumanTurn.setVisible(previousHumanTurn.isEnabled());
    nextHumanTurn.setEnabled(previousHumanTurn.isEnabled());
    nextHumanTurn.setVisible(nextHumanTurn.isEnabled());
    latestHumanTurn.setEnabled(nextHumanTurn.isEnabled());
    latestHumanTurn.setVisible(latestHumanTurn.isEnabled());

    navigateMenuSeparator.setVisible(latestHumanTurn.isEnabled());

    previousTurn.setEnabled(sepukuPlay != null && sepukuPlay.gameIsPlaying());
    previousTurn.setVisible(previousTurn.isEnabled());
    nextTurn.setEnabled(previousTurn.isEnabled());
    nextTurn.setVisible(nextTurn.isEnabled());
    previousMove.setEnabled(nextTurn.isEnabled());
    previousMove.setVisible(previousMove.isEnabled());
    nextMove.setEnabled(previousMove.isEnabled());
    nextMove.setVisible(nextMove.isEnabled());
    nextPartMove.setEnabled(nextMove.isEnabled());
    nextPartMove.setVisible(nextPartMove.isEnabled());
    previousPartMove.setEnabled(nextPartMove.isEnabled());
    previousPartMove.setVisible(previousPartMove.isEnabled());

    previousScenario.setEnabled(sepukuPlay.stateOfPlay().nextPlayTitle().equals("Start match"));
    previousScenario.setVisible(previousScenario.isEnabled());
    nextScenario.setEnabled(previousScenario.isEnabled());
    nextScenario.setVisible(nextScenario.isEnabled());
  }

}