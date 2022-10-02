package inUrFace.menu.dropMenues.trainingMenu;

import static sepuku.App.*;
import static util.Dialogs.getIntegerInput;

import engine.core.trainer.Trainer;
import util.time.Timeable;
import util.menus.ListMenu;
import util.menus.RadioButtonListMenu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class TrainerMenu extends JMenu implements Timeable {

  private PlayMatchesMenu playMatchesMenu = new PlayMatchesMenu("Play");
  private BotPlayersMenu botPlayersMenu = new BotPlayersMenu("Bot players");
  private RadioButtonListMenu selectScenarioMenu = new RadioButtonListMenu(new ScenarioMenu());
  private JMenuItem setScoreToWin = new JMenuItem("Set scoreBoard to win");

  private JPopupMenu.Separator trainerMenuSeparator = new JPopupMenu.Separator();

  private ListMenu editBotMenu = new ListMenu(new EditBotMenu());
  private ListMenu deleteBotMenu = new ListMenu(new DeleteBotMenu());
  private ListMenu cloneBotMenu = new ListMenu(new CloneBotMenu());
  private ListMenu randomizeBotMenu = new ListMenu(new RandomizeBotMenu());
  private ListMenu renameBotMenu = new ListMenu(new RenameBotMenu());
  private ListMenu botReportMenu = new ListMenu(new BotReportMenu());

  private JMenuItem printStatReport = new JMenuItem("Print progress report");
  private JMenuItem killRun = new JMenuItem("Stop playing");


  public TrainerMenu() {

    super("Trainer");
    setupPlayMatchesMenu();
    setupBotPlayersMenu();
    setupChosenScenario();
    setupSetScoreToWin();
    setupSeparator();
    setupEditBotMenu();
    setupDeleteBotMenu();
    setupCloneBotMenu();
    setupRandomizeBotMenu();
    setupRenameBotMenu();
    setupBotReportMenu();
    setupPrintStatReport();
    setupKillRun();
  }

  private void setupPlayMatchesMenu () {

    add(playMatchesMenu);
  }

  private void setupBotPlayersMenu () {

    add(botPlayersMenu);
  }

  private void setupChosenScenario () {

    add(selectScenarioMenu);
  }

  private void setupSetScoreToWin () {

    add(setScoreToWin);
    setScoreToWin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
    setScoreToWin.addActionListener((ActionEvent e) -> {
      playSepuku.getInput().inputStatScoreToWin(
        getIntegerInput(
          "Type the score required " +
            "to win a statistical match:",
          win
        )
      );
    });
  }

  private void setupSeparator () {

    add(trainerMenuSeparator);
  }

  private void setupEditBotMenu () {

    add(editBotMenu);
  }

  private void setupDeleteBotMenu () {

    add(deleteBotMenu);
  }

  private void setupCloneBotMenu () {

    add(cloneBotMenu);
  }

  private void setupRandomizeBotMenu () {

    add(randomizeBotMenu);
  }

  private void setupRenameBotMenu () {

    add(renameBotMenu);
  }

  private void setupBotReportMenu () {

    add(botReportMenu);
  }

  private void setupPrintStatReport () {

    add(printStatReport);
    printStatReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
    printStatReport.addActionListener((ActionEvent e) -> {
      trainer.printReport();
    });
  }

  private void setupKillRun () {

    add(killRun);
    killRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    killRun.addActionListener((ActionEvent e) -> {
      Trainer.killRun = true;
    });
  }

  @Override
  public void timerUpdate() {

    setScoreToWin.setText("Set scoreBoard to win ("+Trainer.statScoreToWin+")");

    playMatchesMenu.setEnabled(!Trainer.running);
    playMatchesMenu.setVisible(!Trainer.running);
    botPlayersMenu.setEnabled(!Trainer.running);
    botPlayersMenu.setVisible(!Trainer.running);
    botPlayersMenu.timerUpdate();

    selectScenarioMenu.setEnabled(!Trainer.running);
    selectScenarioMenu.setVisible(!Trainer.running);
    selectScenarioMenu.updateList();

    setScoreToWin.setEnabled(!Trainer.running);
    setScoreToWin.setVisible(!Trainer.running);

    trainerMenuSeparator.setVisible(!Trainer.running);

    editBotMenu.setEnabled(!Trainer.running && editBotMenu.getMenuItems().size() > 1);
    editBotMenu.setVisible(editBotMenu.isEnabled());
    editBotMenu.updateList();
    editBotMenu.getItems().get(0).setEnabled(false);
    editBotMenu.getItems().get(0).setVisible(false);

    cloneBotMenu.setEnabled(!Trainer.running);
    cloneBotMenu.setVisible(!Trainer.running);
    cloneBotMenu.updateList();

    botReportMenu.setEnabled(!Trainer.running);
    botReportMenu.setVisible(!Trainer.running);
    botReportMenu.updateList();

    renameBotMenu.setEnabled(!Trainer.running && renameBotMenu.getMenuItems().size() > 1);
    renameBotMenu.setVisible(!Trainer.running && renameBotMenu.isEnabled());
    renameBotMenu.updateList();
    renameBotMenu.getItems().get(0).setEnabled(false);
    renameBotMenu.getItems().get(0).setVisible(false);

    randomizeBotMenu.setEnabled(!Trainer.running && randomizeBotMenu.getMenuItems().size() > 1);
    randomizeBotMenu.setVisible(!Trainer.running && randomizeBotMenu.isEnabled());
    randomizeBotMenu.updateList();
    randomizeBotMenu.getItems().get(0).setEnabled(false);
    randomizeBotMenu.getItems().get(0).setVisible(false);

    deleteBotMenu.setEnabled(!Trainer.running && deleteBotMenu.getMenuItems().size() > 1);
    deleteBotMenu.setVisible(!Trainer.running && deleteBotMenu.isEnabled());
    deleteBotMenu.updateList();
    deleteBotMenu.getItems().get(0).setEnabled(false);
    deleteBotMenu.getItems().get(0).setVisible(false);

    printStatReport.setEnabled(Trainer.running);
    printStatReport.setVisible(Trainer.running);
    killRun.setEnabled(Trainer.running);
    killRun.setVisible(Trainer.running);
  }

}
