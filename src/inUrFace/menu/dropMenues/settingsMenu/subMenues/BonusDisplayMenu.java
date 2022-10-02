package inUrFace.menu.dropMenues.settingsMenu.subMenues;

import static sepuku.App.playSepuku;

import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class BonusDisplayMenu extends JMenu {

  JRadioButtonMenuItem movingPlayer =
    new JRadioButtonMenuItem(
      "Show moving player",
      playSepuku.getSettings().getBonusDisplayMode() == 1
    );
  JRadioButtonMenuItem showOpponent =
    new JRadioButtonMenuItem(
      "Show opponent",
      playSepuku.getSettings().getBonusDisplayMode() == 2
    );
  JRadioButtonMenuItem showBothPlayers =
    new JRadioButtonMenuItem(
      "Show both players",
      playSepuku.getSettings().getBonusDisplayMode() == 0
    );
  JRadioButtonMenuItem showNone =
    new JRadioButtonMenuItem(
      "Show none",
      playSepuku.getSettings().getBonusDisplayMode() == 3
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
        playSepuku.getSettings().setBonusDisplayMode(1);
    });
  }

  private void setupShowOpponent () {

    add(showOpponent);
    showOpponent.addActionListener((ActionEvent e) ->  {
        playSepuku.getSettings().setBonusDisplayMode(2);
    });
  }

  private void setupShowBothPlayers () {

    add(showBothPlayers);
    showBothPlayers.addActionListener((ActionEvent e) ->  {
        playSepuku.getSettings().setBonusDisplayMode(0);
    });
  }

  private void setupShowNone () {

    add(showNone);
    showNone.addActionListener((ActionEvent e) ->  {
        playSepuku.getSettings().setBonusDisplayMode(3);
    });
  }

}