package engine.api;

public class ScenarioInfoHTML {
  
  private PlaySepuku playSepuku;
  private String scenarioName = "";
  private String scenarioDataHTML;
  
  public ScenarioInfoHTML (PlaySepuku playSepuku) {
    
    this.playSepuku = playSepuku;
  }

  private String scenarioDataHTML() {

    String spaces = "&nbsp&nbsp";
    
    scenarioName = getScenarioName();
    
    return
      "<HTML>"
      + HTML_Separator()
      + spaces
      + HTML_Label("Scenario: ")
      + spaces
      + HTML_dataItem(scenarioName)
      + spaces
      + HTML_Separator()
      + "</HTML>";
  }
  
  private boolean updateRequired () {
    
    return
      !scenarioName.equals(getScenarioName());
  }
  
  private String getScenarioName () {
    
    return
      playSepuku.getScenarios().getSelectedScenariosTitle();
  }

  private String HTML_Label (String label) {

    return
      "<font color=yellow>"
      + label
      + "</font>";
  }

  private String HTML_dataItem (String dataItem) {

    return
      "<font color=white>"
      + dataItem
      + "</font>";
  }

  private String HTML_Separator () {

    return
      "<font color=red>"
      + "|"
      +"</font>";
  }
  
  public String getHTMLFormattedDataString() {
    
    if (updateRequired()) {
      scenarioDataHTML = scenarioDataHTML();
    }
    
    return
      scenarioDataHTML;
  }
  
}