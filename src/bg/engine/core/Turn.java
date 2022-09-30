package bg.engine.core;

import bg.engine.play.humanMove.MoveSelection;
import bg.engine.core.moves.*;

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

    System.out.println("playedMoveNr set to: "+moveNr);
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

    System.out.println("Setting dice");
    new Dice(dice).printDice();
    generateMoves(getParentMoveLayout(), dice);
    setPlayedMoveNr(0);
  }
  
  public void setTurnCube (Cube cube) {
    
    turnCube.setOwner(cube.getOwner());
    turnCube.setValue(cube.getValue());
  }

  public int getPlayerOnRollsID() {

    return getPlayedMove().getPlayerID();
  }

  public MoveSelection getMovePoints() {

    return new MoveSelection(this);
  }

}
