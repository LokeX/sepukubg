package bg.util.menus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class RadioButtonListMenu extends JMenu {

  ButtonGroup buttonGroup;
  List<JRadioButtonMenuItem> jRadioButtonMenuItems;
  Listable listMenu;

  public RadioButtonListMenu(Listable listMenu) {

    super(listMenu.getMenuTitle());
    this.listMenu = listMenu;
    setupRadioButtons();
  }

  public List<JRadioButtonMenuItem> getRadioButtons () {

    return jRadioButtonMenuItems;
  }

  public void setSelectedItemNr (int nr) {

    listMenu.itemSelectedAction(nr);
  }

  public int getSelectedItemNr () {

    return listMenu.getSelectedItemNr();
  }

  public boolean isChanged () {

    List<String> elementTitles = listMenu.getElementTitles();
    boolean changed = jRadioButtonMenuItems.size() != elementTitles.size();

    if (!changed) {
      for (int a = 0; a < jRadioButtonMenuItems.size(); a++) {
        if (!jRadioButtonMenuItems.get(a).getText().equals(elementTitles.get(a))) {
          return true;
        }
      }
    }
    return changed;
  }

  public void updateList () {

    if (isChanged()) {
      removeAll();
      setupRadioButtons();
      validate();
    }
  }

  private int getMenuItemNr(String actionCommand) {

    for (int a = 0; a < jRadioButtonMenuItems.size(); a++) {
      if (actionCommand.equals(jRadioButtonMenuItems.get(a).getText())) {
        return a;
      }
    }
    return -1;
  }

  public List<JRadioButtonMenuItem> getMenuItems () {

    List<JRadioButtonMenuItem> menuItems = new ArrayList<>();

    listMenu.getElementTitles().forEach(title ->
      menuItems.add(new JRadioButtonMenuItem(title))
    );
    return menuItems;
  }

  public void setupRadioButtons () {

    buttonGroup = new ButtonGroup();
    jRadioButtonMenuItems = getMenuItems();
    jRadioButtonMenuItems.forEach(jRadioButtonMenuItem -> {
      if (getMenuItemNr(jRadioButtonMenuItem.getText()) == listMenu.getSelectedItemNr()) {
        jRadioButtonMenuItem.setSelected(true);
      }
      add(jRadioButtonMenuItem);
      jRadioButtonMenuItem.setActionCommand(jRadioButtonMenuItem.getText());
      buttonGroup.add(jRadioButtonMenuItem);
      jRadioButtonMenuItem.addActionListener((ActionEvent e) -> {
        setSelectedItemNr(
          getMenuItemNr(
            buttonGroup.getSelection().getActionCommand()
          )
        );
      });
    });
  }
}
