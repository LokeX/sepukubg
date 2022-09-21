package bg.engine.api;

import bg.engine.coreLogic.moves.Layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Scenarios {

  private static List<NamedLayout> namedLayouts = new ArrayList<>();
  private Layout editedScenario = null;
  private int selectedScenario = 0;
  private boolean isEditing = true;

  static public class NamedLayout implements Serializable {

    private String name;
    private int[] point;

    public NamedLayout (String name, int[] point) {

      this.name = name;
      this.point = point.clone();
    }

    public String getName() {

      return name;
    }

    public void setName(String name) {

      this.name = name;
    }

    public Layout getLayout () {

      return new Layout(point);
    }

  }

  public Scenarios () {

    String[] scenarioTitles = new String[] {
      "New Game",
    };

    int[][] scenarioLayouts = new int[][] {
      {0,0,0,0,0,0,5,0,3,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,2,0,0,2,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,5,0,0,0,0,0,0},
    };

    if (namedLayouts.isEmpty()) {
      for (int a = 0; a < scenarioTitles.length; a++) {
        namedLayouts.add(new NamedLayout(scenarioTitles[a],scenarioLayouts[a]));
      }
    }
  }

  public Layout getMatchLayout () {

    return
      editedScenario != null
      ? editedScenario
      : namedLayouts.get(selectedScenario).getLayout();
  }

  public void setEditedScenario (Layout scenario) {

    editedScenario = scenario;
  }

  public boolean isEditing () {

    return isEditing;
  }

  public void setEditing (boolean editing) {

    isEditing = editing;
    editedScenario = null;
  }

  public Scenarios (Scenarios scenarios) {

    namedLayouts = new ArrayList<>(scenarios.getScenarios());
    selectedScenario = scenarios.getSelectedScenariosNr();
  }

  public List<String> getLayoutTitles () {

    return
      namedLayouts.stream()
        .map(NamedLayout::getName)
        .collect(Collectors.toList());
  }

  public void setSelectedScenariosName (String name) {

    namedLayouts.get(selectedScenario).setName(name);
  }

  public void deleteSelectedScenario () {

    if (selectedScenario > 0 && namedLayouts.size() > 0) {
      namedLayouts.remove(selectedScenario);
      if (selectedScenario > namedLayouts.size()-1) {
        selectedScenario = namedLayouts.size()-1;
      }
    }
  }

  public boolean isMember (String layoutName) {

    return namedLayouts.stream().
      map(NamedLayout::getName).
      anyMatch(name -> name.equals(layoutName));
  }

  public void addNamedLayout(String name, Layout layout) {

    namedLayouts.add(new NamedLayout(name, layout.getClone().point));
    selectedScenario = namedLayouts.size()-1;
  }

  public void selectNextScenario () {

    if (++selectedScenario > namedLayouts.size()-1) {
      selectedScenario = 0;
      editedScenario = null;
    }
  }

  public void selectPreviousScenario () {

    if (--selectedScenario < 0) {
      selectedScenario = namedLayouts.size()-1;
      editedScenario = null;
    }
  }

  public List<NamedLayout> getScenarios () {

    return namedLayouts;
  }

  public void setScenarios (List<NamedLayout> scenarios) {

    namedLayouts = scenarios;
  }

  public int getSelectedScenariosNr () {

    return selectedScenario;
  }

  public String getSelectedScenariosTitle () {

    return namedLayouts.get(selectedScenario).getName();
  }

  public Layout getSelectedScenariosLayout () {

    return namedLayouts.get(selectedScenario).getLayout();
  }

  public Layout getLayoutByNr (int nr) {

    return namedLayouts.get(nr).getLayout();
  }

}
