package bg.api;

import bg.engine.moves.Layout;

import java.util.List;

public interface Displayable {

  void displayLayout (Layout layout);

  void displayLayouts (List<Layout> layouts, final Object notifier);

}
