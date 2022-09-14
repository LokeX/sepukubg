package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import bg.Main;
import bg.inUrFace.canvas.move.MoveOutput;

import static bg.Main.engineApi;
import static bg.Main.settings;

import java.awt.event.ActionEvent;
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
    movingPlayer.addActionListener((ActionEvent e) ->  {
        Main.settings.setBonusDisplayMode(1);
        new MoveOutput(engineApi.getSelectedMove()).writeMove();
    });
  }

  private void setupShowOpponent () {

    add(showOpponent);
    showOpponent.addActionListener((ActionEvent e) ->  {
        Main.settings.setBonusDisplayMode(2);
        new MoveOutput(engineApi.getSelectedMove()).writeMove();
    });
  }

  private void setupShowBothPlayers () {

    add(showBothPlayers);
    showBothPlayers.addActionListener((ActionEvent e) ->  {
        Main.settings.setBonusDisplayMode(0);
        new MoveOutput(engineApi.getSelectedMove()).writeMove();
    });
  }

  private void setupShowNone () {

    add(showNone);
    showNone.addActionListener((ActionEvent e) ->  {
        Main.settings.setBonusDisplayMode(3);
        new MoveOutput(engineApi.getSelectedMove()).writeMove();
    });
  }

}