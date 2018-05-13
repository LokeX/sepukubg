package bg.engine;

import bg.engine.moves.EvaluatedMove;
import bg.engine.moves.Layout;
import java.util.ArrayList;
import java.util.List;

public class Game {

  private List<Turn> turns = new ArrayList();
  private Cube gameCube = new Cube();
  private Layout gameLayout;

  public Game (Layout layout) {

    gameLayout = new Layout(layout);
  }

  public void truncateTurns (int turnNr) {

    while (turnNr < turns.size()-1) {
      turns.remove(turnNr+1);
    }
    gameCube = getLatestTurn().getTurnCube();
  }

  public void nextTurn (int turnNr, int moveNr) {

    if (turns.size() == 0) {
      turns.add(new Turn(gameLayout, gameCube));
    } else {
      if (turnNr < turns.size()-1) {
        truncateTurns(turnNr);
      }
      turns.add(new Turn(turns.get(turnNr).setPlayedMoveNr(moveNr), gameCube));
    }
  }

  public void nextTurn () {

    if (turns.size() == 0) {
      turns.add(new Turn(gameLayout, gameCube));
    } else {
      turns.add(new Turn(turns.get(turns.size()-1), gameCube));
    }
  }

  public int getLatestTurnNr () {

    return turns.size()-1;
  }

  public Turn getTurnByNr (int turnNr) {

    return turns.get(turnNr);
  }

  public Turn getLatestTurn () {

    return turns.size() > 0 ? turns.get(turns.size()-1) : null;
  }

  public boolean gameOver() {

    return (turns.size() > 0) &&
      ((turns.get(turns.size() - 1).getPlayedMove().rearPos() == 0) || gameCube.cubeWasRejected());
  }

  public Cube getGameCube() {

    return gameCube;
  }

  public int[] getDice () {

    return new Dice(turns.get(0).getMoveByNr(0).getDice()).getDice();
  }

  public int getNrOfTurns () {

    return turns.size();
  }

  public boolean playerCanOfferCube () {

    return turns.size() > 0 && gameCube.playerCanOfferCube(getLatestTurn().getPlayerOnRollsID());
  }

  public EvaluatedMove getPlayedMove () {

    return getLatestTurn().getPlayedMove();
  }

  public void playerDoubles () {

    gameCube.playerDoubles(getLatestTurn().getPlayerOnRollsID());
  }

  public void computerHandlesCube () {

    if (playerCanOfferCube() && getPlayedMove().shouldDouble()) {
      gameCube.setCubeWasRejected(!getPlayedMove().shouldTake());
      if (!gameCube.cubeWasRejected()) {
        playerDoubles();
      }
    }
  }


}
