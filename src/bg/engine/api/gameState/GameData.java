package bg.engine.api.gameState;

import static java.util.stream.IntStream.range;

public class GameData {

  private GameState gameState;

  GameData getGameData (GameState gameState) {

    this.gameState = gameState;
    return this;
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

  private String movePoints () {

    return
      gameState.getHumanMove().inputReady()
        ? gameState.getHumanMove().getMovePointsString()
        : gameState.selectedMove().getMovePointsString();
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

  public String[] dataItems () {

    return new String[] {
      playerDescription(),
      turnNr()+"/"+nrOfTurns(),
      dice(gameState.selectedTurn().getDice()),
      moveNr()+"/"+nrOfMoves(),
      movePoints()
    };
  }

}
