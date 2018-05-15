package bg.inUrFace.menu.dropMenues;

import bg.api.MatchApi;
import bg.util.time.Timeable;

import static bg.Main.*;
import static bg.util.Dialogs.confirmed;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

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
//    setupResignGame();
    setupNextAction();
  }

  private void setupNewMatch() {

    add(newMatch);
    newMatch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
    newMatch.addActionListener((ActionEvent e) -> {
      if (confirmed("Start a new MatchApi?",win)) {
        mouse.moveInputController.setAcceptMoveInput(false);
        matchApi = new MatchApi();
      }
    });
  }

//  private void setupResignGame () {
//
//    add(resignGame);
//    resignGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,0));
//    resignGame.addActionListener((ActionEvent e) -> {
//      if (api.confirmResignation()) {
//        api.newGame();
//      }
//    });
//  }

  private void setupAutoCompleteGame () {

    add(autoCompleteGame);
    autoCompleteGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    autoCompleteGame.addActionListener((ActionEvent e) -> {
      if (matchApi.getAutoCompleteGame()) {
        autoCompleteGame.setText("Auto complete game");
        matchApi.setAutoCompleteGame(false);
      } else {
        autoCompleteGame.setText("Stop auto complete");
        matchApi.setAutoCompleteGame(true);
        new Thread(() ->
          matchApi.actionButtonClicked()
        ).start();
      }
    });
  }

  private void setupNextAction () {

    add(nextAction);
    nextAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    nextAction.addActionListener((ActionEvent e) ->
      new Thread(() ->
        matchApi.actionButtonClicked()
      ).start()
    );
  }

  public void timerUpdate () {

    resignGame.setEnabled(matchApi != null && matchApi.getNrOfTurns() > 0);
    resignGame.setVisible(resignGame.isEnabled());
    newMatch.setEnabled(resignGame.isEnabled());
    newMatch.setVisible(resignGame.isEnabled());
    autoCompleteGame.setEnabled(matchApi != null && matchApi.getGame() != null && !matchApi.gameOver());
    autoCompleteGame.setVisible(autoCompleteGame.isEnabled());
    if (mouse != null && mouse.actionButton != null) {
      nextAction.setText(mouse.actionButton.getButtonText());
      nextAction.setEnabled(
        matchApi != null &&
          !getActionButton().buttonIsHidden() &&
          mouse.actionButton.showButton() &&
          !matchApi.getAutoCompleteGame() &&
          !getActionButton().showPleaseWaitButton()
      );
      nextAction.setVisible(nextAction.isEnabled());
    }
  }

}
