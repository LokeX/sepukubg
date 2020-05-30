package bg.engine.match;

import bg.engine.api.gamePlay.navigation.humanMove.MoveSelector;
import bg.engine.match.moves.*;

public class Turn extends Moves {

  private int turnNr;
  private int playedMoveNr;
  private Cube turnCube;

  public Turn (Layout layout, Cube gameCube) {

    super(layout.getStartMoves());
    initTurn(0, gameCube);
  }

  public Turn (Turn turn, Cube gameCube) {

    super(turn.getPlayedMove().getNextMoves(new Dice().rollDice().getDice()));
    initTurn(turn.turnNr+1, gameCube);
  }

  private void initTurn (int turnNr, Cube gameCube) {

    this.turnNr = turnNr;
    this.turnCube = new Cube(gameCube);
    setPlayedMoveNr(0);
  }

  public EvaluatedMove getPlayedMove() {

    return getEvaluatedMove(playedMoveNr);
  }

  public Turn setPlayedMoveNr (int moveNr) {

    playedMoveNr = moveNr;
    return this;
  }

  public EvaluatedMove getMoveByNr (int moveNr) {

    return getEvaluatedMove(moveNr);
  }

  public int getPlayedMoveNr () {

    return playedMoveNr;
  }

  public Cube getTurnCube() {

    return new Cube(turnCube);
  }

  public int getTurnNr () {

    return turnNr;
  }

  public String getPlayerTitle () {

    return getPlayedMove().getPlayerTitle();
  }

  public void setDice (int[] dice) {

    generateMoves(getParentMoveLayout(), dice);
    setPlayedMoveNr(0);
  }

  public int getPlayerOnRollsID() {

    return getPlayedMove().getPlayerID();
  }

  public MoveSelector getMovePoints() {

    return new MoveSelector(this);
  }

}
