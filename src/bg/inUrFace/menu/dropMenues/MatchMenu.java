package bg.inUrFace.menu.dropMenues;

import bg.engine.api.EngineApi;
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
      if (confirmed("Start a new match?",win)) {
        engineApi = new EngineApi();
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
    autoCompleteGame.setEnabled(engineApi != null && engineApi.getGame() != null && !engineApi.gameOver());
    autoCompleteGame.setVisible(autoCompleteGame.isEnabled());
    if (mouse != null && mouse.actionButton != null) {
      nextAction.setText(mouse.actionButton.getButtonText());
      nextAction.setEnabled(
        engineApi != null &&
          !getActionButton().buttonIsHidden() &&
          mouse.actionButton.showButton() &&
          !engineApi.getAutoCompleteGame() &&
          !getActionButton().showPleaseWaitButton()
      );
      nextAction.setVisible(nextAction.isEnabled());
    }
  }

}
