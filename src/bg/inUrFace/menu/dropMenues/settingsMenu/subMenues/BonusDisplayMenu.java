package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import bg.Main;

import static bg.Main.engineApi;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class BonusDisplayMenu extends JMenu {

  JRadioButtonMenuItem movingPlayer =
    new JRadioButtonMenuItem(
      "Show moving player",
      engineApi.getSettings().getBonusDisplayMode() == 1
    );
  JRadioButtonMenuItem showOpponent =
    new JRadioButtonMenuItem(
      "Show opponent",
      engineApi.getSettings().getBonusDisplayMode() == 2
    );
  JRadioButtonMenuItem showBothPlayers =
    new JRadioButtonMenuItem(
      "Show both players",
      engineApi.getSettings().getBonusDisplayMode() == 0
    );
  JRadioButtonMenuItem showNone =
    new JRadioButtonMenuItem(
      "Show none",
      engineApi.getSettings().getBonusDisplayMode() == 3
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
        engineApi.getSettings().setBonusDisplayMode(1);
    });
  }

  private void setupShowOpponent () {

    add(showOpponent);
    showOpponent.addActionListener((ActionEvent e) ->  {
        engineApi.getSettings().setBonusDisplayMode(2);
    });
  }

  private void setupShowBothPlayers () {

    add(showBothPlayers);
    showBothPlayers.addActionListener((ActionEvent e) ->  {
        engineApi.getSettings().setBonusDisplayMode(0);
    });
  }

  private void setupShowNone () {

    add(showNone);
    showNone.addActionListener((ActionEvent e) ->  {
        engineApi.getSettings().setBonusDisplayMode(3);
    });
  }

}