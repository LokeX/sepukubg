package bg.inUrFace.menu.dropMenues;

import bg.util.time.Timeable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static bg.Main.*;
import static bg.util.Dialogs.confirmed;

public class MatchMenu extends JMenu implements Timeable {

  JMenuItem newMatch = new JMenuItem("New Match");
  JMenuItem resignGame = new JMenuItem("Resign game");
  JMenuItem autoCompleteGame = new JMenuItem("Auto complete game");
  JMenuItem nextAction = new JMenuItem("Next action");

  public MatchMenu () {

    super("Match");
    setupMatchMenu();
  }

  private void setupMatchMenu () {

    setupNewMatch();
    setupAutoCompleteGame();
    setupNextAction();
  }

  private void setupNewMatch() {

    add(newMatch);
    newMatch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
    newMatch.addActionListener((ActionEvent e) -> {
      if (confirmed("Start a new match?",win)) {
        engineApi.newMatch();
      }
    });
  }

  private void setupAutoCompleteGame () {

    add(autoCompleteGame);
    autoCompleteGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    autoCompleteGame.addActionListener((ActionEvent e) -> {
      if (engineApi.getAutoCompleteGame()) {
        autoCompleteGame.setText("Auto complete game");
        engineApi.setAutoCompleteGame(false);
      } else {
        autoCompleteGame.setText("Stop auto complete");
        engineApi.setAutoCompleteGame(true);
        new Thread(() ->
          engineApi.getMatchPlay().actionButtonClicked()
        ).start();
      }
    });
  }

  private void setupNextAction () {

    add(nextAction);
    nextAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    nextAction.addActionListener((ActionEvent e) ->
      new Thread(() ->
        engineApi.getMatchPlay().actionButtonClicked()
      ).start()
    );
  }

  public void timerUpdate () {

    resignGame.setEnabled(engineApi != null && engineApi.getNrOfTurns() > 0);
    resignGame.setVisible(resignGame.isEnabled());
    newMatch.setEnabled(resignGame.isEnabled());
    newMatch.setVisible(resignGame.isEnabled());
    autoCompleteGame.setEnabled(engineApi.gameIsPlaying() && !engineApi.gameOver());
    autoCompleteGame.setVisible(autoCompleteGame.isEnabled());
    nextAction.setText(engineApi.getPlayState().nextPlayTitle());
    nextAction.setEnabled(
      engineApi.getPlayState().nextPlayReady()
      && !engineApi.getAutoCompleteGame()
      && !engineApi.getPlayState().isSearching()
    );
    nextAction.setVisible(nextAction.isEnabled());
  }

}
