package bg.inUrFace.canvas.scenario;

import bg.engine.Scenarios;
import bg.inUrFace.canvas.TextArea;

import static bg.Main.getTextArea;
import static bg.Main.scenarios;
import static bg.Main.win;

public class ScenarioOutput extends Scenarios {


  public ScenarioOutput (Scenarios scenarios) {

    super(scenarios);
  }

  public ScenarioOutput () {

    super(scenarios);
  }

  public void outputSelectedScenario() {

    writeScenario();
    win.canvas.setDisplayedLayout(getSelectedScenariosLayout());
  }

  public void writeScenario () {

    TextArea text = getTextArea();

    text.clear();
    text.nlWrite("Select a scenario:");
    text.nlWrite("Scenario:  #"+
      (getSelectedScenariosNr()+1)+" / "+
      getScenarios().size()+":  "+
      getSelectedScenariosTitle());
    text.nlWrite("");
    text.nlWrite("Press left and right arrow keys to browse");
    text.nlWrite("Press space to start the scenario");
  }

}
