package bg.api;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bg.Main.engineApi;
import static java.util.stream.IntStream.range;

class TurnInfoForm {

  private String labelColor = "yellow";
  private String infoColor = "white";
  private String separatorColor = "red";

  private String[] labels () {

    return new String[] {
      "Player:  ",
      "Turn:  ",
      "Dice:  ",
      "move:  ",
      "Points:  "
    };
  }

  private String[] info () {

    if (engineApi != null && engineApi.gameIsPlaying()) {

      int turnNr = engineApi.getSelectedTurnNr();
      int moveNr = engineApi.getSelectedMoveNr()+1;
      int nrOfMoves = engineApi.getTurnByNr(turnNr).getNrOfMoves();
      String nrOfTurns = Integer.toString(engineApi.getNrOfTurns());
      String nrOfTurn = Integer.toString(turnNr+1);

      return new String[] {
        engineApi.getPlayerDescription(),
        nrOfTurn + "/" + nrOfTurns,
        dice(engineApi.getTurnByNr(turnNr).getDice()),
        moveNr + "/" + nrOfMoves,
        movePoints()
      };
    } else {
      return new String[] {"N/A", "N/A", "N/A", "N/A", "N/A"};
    }
  }

  private String movePoints () {

    return
      engineApi.humanInputReady()
        ? engineApi.humanMove.getMovePointsString()
        : engineApi.getSelectedMove().getMovePointsString();
  }

  private String dieWithComma (int[] dice, int dieNr) {

    return dice[dieNr]+(dieNr == dice.length-1 ? "" : ",");
  }

  private String dice (int[] dice) {

    return range(0, dice.length)
      .mapToObj(dieNr -> dieWithComma(dice, dieNr))
      .reduce(String::concat)
      .orElse("N/A");
  }

  Stream<String> getInfoItemStream () {

    return
      IntStream.range(0, labels().length)
        .mapToObj(index -> labels()[index]+info()[index]);
  }

  private String HTML_Label (int index) {


    return
      "<font color="+labelColor+">"
      + labels()[index]
      + "</font>";
  }

  private String HTML_Info (int index) {

    return
      "<font color="+infoColor+">"
      + info()[index]
      + "</font>";
  }

  private String HTML_Separator () {

    return
      "<font color="+separatorColor+">"
      + "|"
      +"</font>";
  }

  private String HTML_Item (int index) {

    String spaces = "&nbsp&nbsp";

    return
      (index == 0
        ? HTML_Separator() + spaces
        : spaces
      )
      + HTML_Label(index)
      + spaces
      + HTML_Info(index)
      + spaces
      + HTML_Separator();
  }

  private String HTML_InfoString () {

    return
        "<HTML>"
      + IntStream.range(0, labels().length)
        .mapToObj(this::HTML_Item)
        .reduce(String::concat)
        .orElse("")
      + "</HTML>";
  }

  String HTMLFormattedInfoString (

    String labelColor,
    String infoColor,
    String separatorColor) {

    this.labelColor = labelColor;
    this.infoColor = infoColor;
    this.separatorColor = separatorColor;

    return HTML_InfoString();
  }

  String HTMLFormattedInfoString () {

    return HTML_InfoString();
  }

}
