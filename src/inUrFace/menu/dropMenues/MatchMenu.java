package inUrFace.menu.dropMenues;

import util.time.Timeable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static sepuku.WinApp.*;
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
        sepukuPlay.startNewMatch();
      }
    });
  }

  private void setupAutoCompleteGame () {

    add(autoCompleteGame);
    autoCompleteGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    autoCompleteGame.addActionListener((ActionEvent e) -> {
      if (sepukuPlay.getAutoCompleteGame()) {
        autoCompleteGame.setText("Auto complete game");
        sepukuPlay.setAutoCompleteGame(false);
      } else {
        autoCompleteGame.setText("Stop auto complete");
        sepukuPlay.setAutoCompleteGame(true);
        new Thread(() ->
          sepukuPlay.execNextPlay()
        ).start();
      }
    });
  }

  private void setupNextAction () {

    add(nextAction);
    nextAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    nextAction.addActionListener((ActionEvent e) ->
      new Thread(() ->
        sepukuPlay.execNextPlay()
      ).start()
    );
  }

  public void timerUpdate () {

    resignGame.setEnabled(sepukuPlay != null && sepukuPlay.getMatchPlay().getNrOfTurns() > 0);
    resignGame.setVisible(resignGame.isEnabled());
    newMatch.setEnabled(resignGame.isEnabled());
    newMatch.setVisible(resignGame.isEnabled());
    autoCompleteGame.setEnabled(sepukuPlay.gameIsPlaying() && !sepukuPlay.gameOver());
    autoCompleteGame.setVisible(autoCompleteGame.isEnabled());
    nextAction.setText(sepukuPlay.getPlayState().nextPlayTitle());
    nextAction.setEnabled(
      sepukuPlay.getPlayState().nextPlayReady()
      && !sepukuPlay.getAutoCompleteGame()
      && !sepukuPlay.getPlayState().isSearching()
    );
    nextAction.setVisible(nextAction.isEnabled());
  }

}