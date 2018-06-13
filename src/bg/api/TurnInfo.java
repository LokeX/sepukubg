package bg.api;

import java.util.List;
import java.util.stream.IntStream;

import static bg.Main.matchApi;
import static java.util.stream.IntStream.range;

public class TurnInfo {

  private final String red = "red>";
  private final String yellow = "yellow>";
  private final String fontTag = "<font color = ";
  private final String yellowTag = fontTag + yellow;
  private final String redTag = fontTag + red;
  private final String endFontTag = "</font>";
  private final String spaces = "&nbsp&nbsp";

  private String moveNumbers;
  private String turnNumbers;
  private String movePoints;
  private String playerDescription;
  private String dice;

  public TurnInfo updateInfo () {

    final String NA = "N/A";

    playerDescription = NA;
    moveNumbers = NA;
    turnNumbers = NA;
    dice = NA;

    if (matchApi != null && matchApi.gameIsPlaying()) {
      int turnNr = matchApi.getSelectedTurnNr();
      int moveNr = matchApi.getSelectedMoveNr()+1;
      int nrOfMoves = matchApi.getTurnByNr(turnNr).getNrOfMoves();
      String nrOfTurns = Integer.toString(matchApi.getNrOfTurns());
      String nrOfTurn = Integer.toString(turnNr+1);

      playerDescription = matchApi.getPlayerDescription();
      moveNumbers = moveNr + "/" + nrOfMoves;
      turnNumbers = nrOfTurn + "/" + nrOfTurns;
      dice = dice(matchApi.getTurnByNr(turnNr).getDice());
      movePoints = getMovePointsString();
    }
    return this;
  }

  private String getMovePointsString () {

    return
      matchApi.humanInputReady()
        ? matchApi.moveInputNew.getMovePointsString()
        : matchApi.getSelectedMove().getMovePointsString();
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

  public List<String> getTurnInfoList () {

    final String white = "white>";
    final String whiteTag = fontTag + white;

    return List.of (
      whiteTag + "Player:"+ spaces + endFontTag + playerDescription,
      whiteTag + "Turn:" + spaces + endFontTag + turnNumbers,
      whiteTag + "Dice:" + spaces + endFontTag + dice,
      whiteTag + "Move:" + spaces + endFontTag + moveNumbers,
      whiteTag + "Points:" + spaces + endFontTag + movePoints
    );
  }

  public String getTurnInfoString (char separator) {

    List<String> turnInfo = getTurnInfoList();

    return IntStream.range(0, turnInfo.size())
      .mapToObj(index ->
          (index == 0 ? "<html>" + redTag + separator + spaces + endFontTag : "")
        + yellowTag
        + turnInfo.get(index)
        + endFontTag
        + redTag
        + spaces
        + separator
        + endFontTag
        + (index == turnInfo.size()-1 ? "</html>" : spaces)
      )
      .reduce(String::concat)
      .orElse("N/A");
  }

  public String getTurnInfoString() {

    return getTurnInfoString('|');
  }

}
