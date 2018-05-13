package bg.inUrFace.menu.dropMenues;

import bg.inUrFace.canvas.move.MoveOutput;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.util.time.Timeable;
import static bg.Main.scenarios;
import static bg.Main.settings;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.KeyStroke;
import static bg.Main.matchApi;

public class NavigateMenu extends JMenu implements Timeable {

  JMenuItem previousHumanTurn = new JMenuItem("Previous human turn");
  JMenuItem nextHumanTurn = new JMenuItem("Next human turn");
  JMenuItem latestHumanTurn = new JMenuItem("Latest human turn");
  Separator navigateMenuSeparator = new JPopupMenu.Separator();
  JMenuItem previousTurn = new JMenuItem("Previous turn");
  JMenuItem nextTurn = new JMenuItem("Next turn");
  JMenuItem previousMove = new JMenuItem("Previous move");
  JMenuItem nextMove = new JMenuItem("Next move");
  JMenuItem previousScenario = new JMenuItem("Previous scenario");
  JMenuItem nextScenario = new JMenuItem("Next scenario");

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
    setupPreviousScenario();
    setupNextScenario();
  }

  private void setupPreviousHumanTurn () {

    add(previousHumanTurn);
    previousHumanTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
    previousHumanTurn.addActionListener((ActionEvent e) -> {
      new MoveOutput(matchApi.selectPreviousHumanTurn().getPlayedMove()).outputMove();
    });
  }

  private void setupNextHumanTurn () {

    add(nextHumanTurn);
    nextHumanTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
    nextHumanTurn.addActionListener((ActionEvent e) -> {
      new MoveOutput(matchApi.selectNextHumanTurn().getPlayedMove()).outputMove();
    });
  }

  private void setupLatestHumanTurn () {

    add(latestHumanTurn);
    latestHumanTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
    latestHumanTurn.addActionListener((ActionEvent e) -> {
      new MoveOutput(matchApi.selectLatestHumanTurn().getPlayedMove()).outputMove();
    });
  }

  private void setupNavigateMenuSeparator() {

    add(navigateMenuSeparator);
  }

  private void setupPreviousTurn () {

    add(nextTurn);
    nextTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
    nextTurn.addActionListener((ActionEvent e) -> {
      if (matchApi.getSelectedTurn() != null) {
        new MoveOutput(matchApi.selectNextTurn().getPlayedMove()).outputMove();
      }
    });
  }

  private void setupNextTurn () {

    add(previousTurn);
    previousTurn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
    previousTurn.addActionListener((ActionEvent e) -> {
      if (matchApi.getSelectedTurn() != null) {
        new MoveOutput(matchApi.selectPreviousTurn().getPlayedMove()).outputMove();
      }
    });
  }

  private void setupPreviousMove () {

    add(previousMove);
    previousMove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
    previousMove.addActionListener((ActionEvent e) -> {
      if (matchApi.getSelectedTurn() != null) {
        new MoveOutput(matchApi.selectPreviousMove()).outputMove();
      }
    });
  }

  private void setupNextMove () {

    add(nextMove);
    nextMove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
    nextMove.addActionListener((ActionEvent e) -> {
      new MoveOutput(matchApi.selectNextMove()).outputMove();
    });
  }

  private void setupPreviousScenario () {

    add(previousScenario);
    previousScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
    previousScenario.addActionListener((ActionEvent e) -> {
      scenarios.selectPreviousScenario();
      new ScenarioOutput(scenarios).outputSelectedScenario();
    });
  }

  private void setupNextScenario () {

    add(nextScenario);
    nextScenario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
    nextScenario.addActionListener((ActionEvent e) -> {
      scenarios.selectNextScenario();
      new ScenarioOutput(scenarios).outputSelectedScenario();
    });
  }

  @Override
  public void timerUpdate() {

    previousHumanTurn.setEnabled(matchApi != null && matchApi.getSelectedTurn() != null && settings.humanPlayerExists());
    previousHumanTurn.setVisible(previousHumanTurn.isEnabled());
    nextHumanTurn.setEnabled(previousHumanTurn.isEnabled());
    nextHumanTurn.setVisible(nextHumanTurn.isEnabled());
    latestHumanTurn.setEnabled(nextHumanTurn.isEnabled());
    latestHumanTurn.setVisible(latestHumanTurn.isEnabled());

    navigateMenuSeparator.setVisible(latestHumanTurn.isEnabled());

    previousTurn.setEnabled(matchApi != null && matchApi.getSelectedTurn() != null);
    previousTurn.setVisible(previousTurn.isEnabled());
    nextTurn.setEnabled(previousTurn.isEnabled());
    nextTurn.setVisible(nextTurn.isEnabled());
    previousMove.setEnabled(nextTurn.isEnabled());
    previousMove.setVisible(previousMove.isEnabled());
    nextMove.setEnabled(previousMove.isEnabled());
    nextMove.setVisible(nextMove.isEnabled());

    previousScenario.setEnabled(matchApi == null || matchApi.getSelectedTurn() == null);
    previousScenario.setVisible(previousScenario.isEnabled());
    nextScenario.setEnabled(previousScenario.isEnabled());
    nextScenario.setVisible(nextScenario.isEnabled());
  }

}
