package inUrFace.menu.dropMenues.trainingMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static sepuku.WinApp.trainer;
import static sepuku.WinApp.win;

public class PlayMatchesMenu extends JMenu {

  JMenuItem play100Matches = new JMenuItem("100 Matches");
  JMenuItem play1000Matches = new JMenuItem("1.000 Matches");
  JMenuItem play10000Matches = new JMenuItem("10.000 Matches");
  JMenuItem play100000Matches = new JMenuItem("100.000 Matches");


  public PlayMatchesMenu(String title) {

    super(title);
    setupPlayMatchesMenu();
  }

  private void setupPlayMatchesMenu () {

    setupPlay100Matches();
    setupPlay1000Matches();
    setupPlay10000Matches();
    setupPlay100000Matches();
  }

  private void setupPlay100Matches () {

    add(play100Matches);
    play100Matches.addActionListener((ActionEvent e) -> {
      trainer.playMatches(100);
      win.progressBar.startProgressMonitor(0, 100);
    });
  }

  private void setupPlay1000Matches () {

    add(play1000Matches);
    play1000Matches.addActionListener((ActionEvent e) -> {
      trainer.playMatches(1000);
      win.progressBar.startProgressMonitor(0, 1000);
    });
  }

  private void setupPlay10000Matches () {

    add(play10000Matches);
    play10000Matches.addActionListener((ActionEvent e) -> {
      trainer.playMatches(10000);
      win.progressBar.startProgressMonitor(0, 10000);
    });
  }

  private void setupPlay100000Matches () {

    add(play100000Matches);
    play100000Matches.addActionListener((ActionEvent e) -> {
      trainer.playMatches(100000);
      win.progressBar.startProgressMonitor(0, 100000);
    });
  }

}