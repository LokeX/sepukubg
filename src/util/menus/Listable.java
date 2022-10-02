package util.menus;

import java.util.List;

public interface Listable {

  void itemSelectedAction(int selectedItemNr);
  int getSelectedItemNr ();
  String getMenuTitle();
  List<String> getElementTitles();

}
