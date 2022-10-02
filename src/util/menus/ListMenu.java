package util.menus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ListMenu extends JMenu {

  List<JMenuItem> menuItems;
  Listable listMenu;

  public ListMenu(Listable listMenu) {

    super(listMenu.getMenuTitle());
    this.listMenu = listMenu;
    setupMenuItems();
  }

  public void setSelectedItemNr (int nr) {

    listMenu.itemSelectedAction(nr);
  }

  public int getSelectedItemNr () {

    return listMenu.getSelectedItemNr();
  }

  public List<JMenuItem> getItems () {

    return menuItems;
  }

  public boolean isChanged () {

    List<String> elementTitles = listMenu.getElementTitles();
    boolean changed = menuItems.size() != elementTitles.size();

    if (!changed) {
      for (int a = 0; a < menuItems.size(); a++) {
        if (!menuItems.get(a).getText().equals(elementTitles.get(a))) {
          return true;
        }
      }
    }
    return changed;
  }

  public void updateList () {

    if (isChanged()) {
      removeAll();
      setupMenuItems();
      validate();
    }
  }

  private int getMenuItemNr(String actionCommand) {

    for (int a = 0; a < menuItems.size(); a++) {
      if (actionCommand.equals(menuItems.get(a).getText())) {
        return a;
      }
    }
    return -1;
  }

  public List<JMenuItem> getMenuItems () {

    List<JMenuItem> menuItems = new ArrayList<>();

    listMenu.getElementTitles().forEach(title ->
      menuItems.add(new JMenuItem(title))
    );
    return menuItems;
  }

  public void setupMenuItems() {

    menuItems = getMenuItems();
    menuItems.forEach(menuItem -> {
      if (getMenuItemNr(menuItem.getText()) == listMenu.getSelectedItemNr()) {
        menuItem.setSelected(true);
      }
      add(menuItem);
      menuItem.setActionCommand(menuItem.getText());
      menuItem.addActionListener((ActionEvent e) -> {
        setSelectedItemNr(
          getMenuItemNr(
            e.getActionCommand()
          )
        );
      });
    });
  }
}
