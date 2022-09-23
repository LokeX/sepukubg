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
        sepuku.newMatch();
      }
    });
  }

  private void setupAutoCompleteGame () {

    add(autoCompleteGame);
    autoCompleteGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    autoCompleteGame.addActionListener((ActionEvent e) -> {
      if (sepuku.getAutoCompleteGame()) {
        autoCompleteGame.setText("Auto complete game");
        sepuku.setAutoCompleteGame(false);
      } else {
        autoCompleteGame.setText("Stop auto complete");
        sepuku.setAutoCompleteGame(true);
        new Thread(() ->
          sepuku.execNextPlay()
        ).start();
      }
    });
  }

  private void setupNextAction () {

    add(nextAction);
    nextAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    nextAction.addActionListener((ActionEvent e) ->
      new Thread(() ->
        sepuku.execNextPlay()
      ).start()
    );
  }

  public void timerUpdate () {

    resignGame.setEnabled(sepuku != null && sepuku.getMatchPlay().getNrOfTurns() > 0);
    resignGame.setVisible(resignGame.isEnabled());
    newMatch.setEnabled(resignGame.isEnabled());
    newMatch.setVisible(resignGame.isEnabled());
    autoCompleteGame.setEnabled(sepuku.gameIsPlaying() && !sepuku.gameOver());
    autoCompleteGame.setVisible(autoCompleteGame.isEnabled());
    nextAction.setText(sepuku.getPlayState().nextPlayTitle());
    nextAction.setEnabled(
      sepuku.getPlayState().nextPlayReady()
      && !sepuku.getAutoCompleteGame()
      && !sepuku.getPlayState().isSearching()
    );
    nextAction.setVisible(nextAction.isEnabled());
  }

}
