package bg.engine.match;

import bg.engine.match.moves.EvaluatedMove;
import bg.engine.match.moves.Layout;
import bg.engine.match.score.GameScore;

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
    gameCube = lastTurn().getTurnCube();
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

  public int lastTurnNr() {

    return turns.size()-1;
  }

  public Turn getTurnByNr (int turnNr) {

    return turns.get(turnNr);
  }

  public Turn lastTurn() {

    return turns.size() > 0 ? turns.get(turns.size()-1) : null;
  }

  public boolean gameOver() {

    return
      (turns.size() > 0)
      && ((turns.get(turns.size() - 1)
        .getPlayedMove()
        .rearPos() == 0)
      || gameCube.cubeWasRejected());
  }

  public Cube getGameCube() {

    return gameCube;
  }

  public int nrOfTurns() {

    return turns.size();
  }

  public GameScore getGameScore () {

    return new GameScore(this);
  }

  public boolean playerCanOfferCube () {

    return
      turns.size() > 0 && gameCube.playerCanOfferCube(
        lastTurn().getPlayerOnRollsID() == 0 ? 1 : 0
      );
  }

  public EvaluatedMove playedMove () {

    return lastTurn().getPlayedMove();
  }

  public void playerDoubles () {

    gameCube.playerDoubles(lastTurn().getPlayerOnRollsID());
  }

  public void computerHandlesCube () {

    if (playerCanOfferCube() && playedMove().shouldDouble()) {
      gameCube.setCubeWasRejected(!playedMove().shouldTake());
      if (!gameCube.cubeWasRejected()) {
        playerDoubles();
      }
    }
  }

}
