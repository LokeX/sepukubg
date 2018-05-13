package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import bg.Main;
import bg.inUrFace.canvas.move.MoveOutput;

import static bg.Main.matchApi;
import static bg.Main.settings;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class BonusDisplayMenu extends JMenu {

  JRadioButtonMenuItem movingPlayer =
    new JRadioButtonMenuItem(
      "Show moving player", settings.getBonusDisplayMode() == 1
    );
  JRadioButtonMenuItem showOpponent =
    new JRadioButtonMenuItem(
      "Show opponent", settings.getBonusDisplayMode() == 2
    );
  JRadioButtonMenuItem showBothPlayers =
    new JRadioButtonMenuItem(
      "Show both players", settings.getBonusDisplayMode() == 0
    );
  JRadioButtonMenuItem showNone =
    new JRadioButtonMenuItem(
      "Show none", settings.getBonusDisplayMode() == 3
    );
  ButtonGroup showBonusChoices = new ButtonGroup();

  public BonusDisplayMenu () {

    super("Bonus Display");
    setupButtonGroup();
    setupMovingPlayer();
    setupShowOpponent();
    setupShowBothPlayers();
    setupShowNone();
  }

  private void setupButtonGroup () {

    showBonusChoices.add(movingPlayer);
    showBonusChoices.add(showOpponent);
    showBonusChoices.add(showBothPlayers);
    showBonusChoices.add(showNone);
  }

    private void setupMovingPlayer () {

    add(movingPlayer);
    movingPlayer.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Main.settings.setBonusDisplayMode(1);
        new MoveOutput(matchApi.getSelectedMove()).writeMove();
//        Main.api.getSelectedTurn().getSelectedMove().writeMove();
      }
    });
  }

  private void setupShowOpponent () {

    add(showOpponent);
    showOpponent.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Main.settings.setBonusDisplayMode(2);
        new MoveOutput(matchApi.getSelectedMove()).writeMove();
//        Main.api.getSelectedTurn().getSelectedMove().writeMove();
      }
    });
  }

  private void setupShowBothPlayers () {

    add(showBothPlayers);
    showBothPlayers.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Main.settings.setBonusDisplayMode(0);
        new MoveOutput(matchApi.getSelectedMove()).writeMove();
//        Main.api.getSelectedTurn().getSelectedMove().writeMove();
      }
    });
  }

  private void setupShowNone () {

    add(showNone);
    showNone.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        settings.setBonusDisplayMode(3);
        new MoveOutput(matchApi.getSelectedMove()).writeMove();
//        Main.api.getSelectedTurn().getSelectedMove().writeMove();
      }
    });
  }

}