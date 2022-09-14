package bg.inUrFace.menu.dropMenues.settingsMenu.subMenues;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static bg.Main.settings;

public class LookaheadToTurnMenu extends JMenu {

  JRadioButtonMenuItem searchIsOff =
    new JRadioButtonMenuItem("None", settings.getSearchToPly() == 0);
  JRadioButtonMenuItem searchToPly1 =
    new JRadioButtonMenuItem("1 turn", settings.getSearchToPly() == 1);
  JRadioButtonMenuItem searchToPly2 =
    new JRadioButtonMenuItem("2 turns", settings.getSearchToPly() == 2);
  JRadioButtonMenuItem searchToPly3 =
    new JRadioButtonMenuItem("3 turns", settings.getSearchToPly() == 3);
  ButtonGroup searchToPlyChoices = new ButtonGroup();

  public LookaheadToTurnMenu() {

    super("Lookahead");
    setupButtonGroup();
    setupSearchIsOff();
    setupSearchToPly1();
    setupSearchToPly2();
    setupSearchToPly3();
  }

  private void setupButtonGroup () {

    searchToPlyChoices.add(searchIsOff);
    searchToPlyChoices.add(searchToPly1);
    searchToPlyChoices.add(searchToPly2);
    searchToPlyChoices.add(searchToPly3);
  }

    private void setupSearchIsOff() {

    add(searchIsOff);
    searchIsOff.addActionListener((ActionEvent e) -> {
        settings.setSearchToPly(0);
    });
  }

  private void setupSearchToPly1() {

    add(searchToPly1);
    searchToPly1.addActionListener((ActionEvent e) -> {
        settings.setSearchToPly(1);
    });
  }

  private void setupSearchToPly2() {

    add(searchToPly2);
    searchToPly2.addActionListener((ActionEvent e) -> {
        settings.setSearchToPly(2);
    });
  }

  private void setupSearchToPly3() {

    add(searchToPly3);
    searchToPly3.addActionListener((ActionEvent e) -> {
        settings.setSearchToPly(3);
    });
  }

}
