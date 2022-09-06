package bg.engine.api.gamePlay;

import bg.engine.api.moveInput.HumanMove;

import static java.util.stream.IntStream.range;

public class GameInfo {

  private GameState gameState;
  private String[] dataItems;
  private boolean dataUpdate = false;
  private int selectedTurnNr;
  private int selectedMoveNr;

  GameInfo(GameState gameState) {

    this.gameState = gameState;
  }

  GameInfo getGameData () {

    dataUpdate = updateRequired();
    if (dataUpdate) {
      dataItems = dataItems();
      selectedMoveNr = gameState.getMoveNr();
      selectedTurnNr = gameState.getTurnNr();
    }
    return this;
  }

  public boolean dataIsUpdated () {

    return dataUpdate;
  }

  private boolean updateRequired () {

    return
      dataItems == null
      || selectedTurnNr != gameState.getTurnNr()
      || selectedMoveNr != gameState.getMoveNr();
  }

  private String moveNr () {

    return
      Integer.toString(
        gameState.getMoveNr()+1
      );
  }

  private String nrOfMoves () {

    return
      Integer.toString(
        gameState
          .getTurnByNr(gameState.getTurnNr())
          .getNrOfMoves()
      );
  }

  private String nrOfTurns () {

    return
      Integer.toString(
          gameState.nrOfTurns()
        );
  }

  private String turnNr () {

    return
      Integer.toString(gameState.getTurnNr()+1);
  }

  private String playerDescription () {

    return
      gameState
        .selectedTurn()
        .getPlayerTitle()
        + (gameState.humanTurnSelected()
        ? "[Human]"
        : "[Computer]");
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

  private HumanMove humanMove () {

    return
      gameState
        .getHumanMove();
  }

  private String humanMovePointsString () {

    return
      humanMove()
        .getMoveSelection()
        .getMatchingMoveLayout()
        .getMovePointsString();
  }

  private String selectedMovePointsString () {

    return
      gameState
        .selectedMove()
        .getMovePointsString();
  }

  private String movePoints () {

    return
      humanMove().inputReady()
        ? humanMovePointsString()
        : selectedMovePointsString();
  }

  private String[] dataItems () {

    return new String[] {
      playerDescription(),
      turnNr()+"/"+nrOfTurns(),
      dice(gameState.selectedTurn().getDice()),
      moveNr()+"/"+nrOfMoves(),
      movePoints()
    };
  }

  public String[] labels () {

    return new String[] {
      "Player:  ",
      "Turn:  ",
      "Dice:  ",
      "Move:  ",
      "Points:  "
    };
  }

  public String[] getDataItems () {

    return dataItems;
  }

}
