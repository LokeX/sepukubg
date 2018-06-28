package bg.inUrFace.canvas.scenario;

import bg.engine.api.Scenarios;
import bg.inUrFace.canvas.BonusPainter;

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

    BonusPainter text = getTextArea();

    text.clear();
    text.writeLine("Select a scenario:");
    text.writeLine("Scenario:  #"+
      (getSelectedScenariosNr()+1)+" / "+
      getScenarios().size()+":  "+
      getSelectedScenariosTitle());
    text.writeLine("");
    text.writeLine("Press left and right arrow keys to browse");
    text.writeLine("Press space to start the scenario");
  }

}
