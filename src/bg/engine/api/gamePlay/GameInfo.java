package bg.engine.api.gamePlay;

import bg.engine.api.matchPlay.MatchState;
import bg.engine.api.moveInput.HumanMove;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class GameInfo {

  private MatchState matchState;
  private String[] dataItems;
  private String movePoints = "";
  private boolean dataUpdate = false;
  private int selectedTurnNr;
  private int selectedMoveNr;

  public GameInfo(MatchState matchState) {

    this.matchState = matchState;
  }

  private GameState gameState () {

    return
      matchState.getGameState();
  }

  public GameInfo getGameData () {

    dataUpdate = updateRequired();
    if (dataUpdate) {
      dataItems = dataItems();
      selectedMoveNr = gameState().getMoveNr();
      selectedTurnNr = gameState().getTurnNr();
      movePoints = movePoints();
    }
    return this;
  }

  public boolean dataIsUpdated () {

    return dataUpdate;
  }

  private boolean updateRequired () {

    return
      dataItems == null
      || !movePoints.equals(movePoints())
      || selectedTurnNr != gameState().getTurnNr()
      || selectedMoveNr != gameState().getMoveNr();
  }

  private String moveNr () {

    return
      Integer.toString(
        gameState().getMoveNr()+1
      );
  }

  private String nrOfMoves () {

    return
      Integer.toString(
        gameState()
          .getTurnByNr(gameState().getTurnNr())
          .getNrOfMoves()
      );
  }

  private String nrOfTurns () {

    return
      Integer.toString(
          gameState().nrOfTurns()
        );
  }

  private String turnNr () {

    return
      Integer.toString(gameState().getTurnNr()+1);
  }

  private String playerDescription () {

    return
      gameState()
        .selectedTurn()
        .getPlayerTitle() +
          (gameState().humanTurnSelected()
            ? "[Human]"
            : "[Computer]");
  }

  private HumanMove humanMove () {

    return
      matchState
        .getHumanMove();
  }

  private String commaSeparated (int[] temp) {

    return
      Arrays.stream(temp)
        .mapToObj(Integer::toString)
        .collect(joining(","));
  }

  private String dice (int[] dice) {

    return
      commaSeparated(dice);
  }

  private String humanMovePoints() {

    return
      commaSeparated(
        humanMove()
          .getMoveSelection()
          .getMovePoints()
      );
  }

  private String selectedMovePointsString () {

    return
      gameState()
        .selectedMove()
        .getMovePointsString();
  }

  private String movePoints () {

    return
      humanMove().inputReady()
        ? humanMovePoints()
        : selectedMovePointsString();
  }

  private String[] dataItems () {

    return
      new String[] {
        playerDescription(),
        turnNr()+"/"+nrOfTurns(),
        dice(gameState().selectedTurn().getDice()),
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
