package inUrFace.menu.dropMenues;

import util.time.Timeable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static sepuku.App.*;
import static util.Dialogs.confirmed;

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
        playSepuku.startNewMatch();
      }
    });
  }

  private void setupAutoCompleteGame () {

    add(autoCompleteGame);
    autoCompleteGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    autoCompleteGame.addActionListener((ActionEvent e) -> {
      if (playSepuku.getAutoCompleteGame()) {
        autoCompleteGame.setText("Auto complete game");
        playSepuku.setAutoCompleteGame(false);
      } else {
        autoCompleteGame.setText("Stop auto complete");
        playSepuku.setAutoCompleteGame(true);
        new Thread(() ->
          playSepuku.execNextPlay()
        ).start();
      }
    });
  }

  private void setupNextAction () {

    add(nextAction);
    nextAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    nextAction.addActionListener((ActionEvent e) ->
      new Thread(() ->
        playSepuku.execNextPlay()
      ).start()
    );
  }

  public void timerUpdate () {

    resignGame.setEnabled(playSepuku != null && playSepuku.getMatchPlay().getNrOfTurns() > 0);
    resignGame.setVisible(resignGame.isEnabled());
    newMatch.setEnabled(resignGame.isEnabled());
    newMatch.setVisible(resignGame.isEnabled());
    autoCompleteGame.setEnabled(playSepuku.gameIsPlaying() && !playSepuku.gameOver());
    autoCompleteGame.setVisible(autoCompleteGame.isEnabled());
    nextAction.setText(playSepuku.getPlayState().nextPlayTitle());
    nextAction.setEnabled(
      playSepuku.getPlayState().nextPlayReady()
      && !playSepuku.getAutoCompleteGame()
      && !playSepuku.getPlayState().isSearching()
    );
    nextAction.setVisible(nextAction.isEnabled());
  }

}
