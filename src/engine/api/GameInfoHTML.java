package engine.api;

import engine.play.gamePlay.GamePlayInfo;

import java.util.stream.IntStream;

public class GameInfoHTML {

  private String labelColor = "yellow";
  private String infoColor = "white";
  private String separatorColor = "red";

  private String[] labels;
  private String[] dataItems;

  private String HTMLFormattedDataString;

  public GameInfoHTML getGameDataHTML(GamePlayInfo gamePlayInfo) {

    if (gamePlayInfo.dataIsUpdated()) {
      labels = gamePlayInfo.labels();
      dataItems = gamePlayInfo.getDataItems();
      HTMLFormattedDataString = HTML_DataString();
    }
    return this;
  }

  private String HTML_Label (int index) {

    return
      "<font color="+labelColor+">"
        + labels[index]
        + "</font>";
  }

  private String HTML_dataItem (int index) {

    return
      "<font color="+infoColor+">"
        + dataItems[index]
        + "</font>";
  }

  private String HTML_Separator () {

    return
      "<font color="+separatorColor+">"
        + "|"
        +"</font>";
  }

  private String HTML_labeledDataItem(int index) {

    String spaces = "&nbsp&nbsp";
    String HTML_Separator = HTML_Separator();

    return
      (index == 0
        ? HTML_Separator + spaces
        : spaces
      )
        + HTML_Label(index)
        + spaces
        + HTML_dataItem(index)
        + spaces
        + HTML_Separator;
  }

  private String HTML_DataString () {

    return
        "<HTML>"
      + IntStream.range(0, labels.length)
          .mapToObj(this::HTML_labeledDataItem)
          .reduce(String::concat)
          .orElse("")
      + "</HTML>";
  }

  public String HTMLFormattedDataString (

    String labelColor,
    String infoColor,
    String separatorColor) {

    this.labelColor = labelColor;
    this.infoColor = infoColor;
    this.separatorColor = separatorColor;

    return HTML_DataString();
  }

  public String getHTMLFormattedDataString () {

    return HTMLFormattedDataString;
  }

}