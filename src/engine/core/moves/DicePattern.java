package engine.core.moves;

import java.util.Arrays;

public class DicePattern extends MoveLayout {
  
  public DicePattern (MoveLayout moveLayout) {
    
    super(moveLayout);
  }

  public int getNrOfLegalPartMoves () {
    
    return
      parentMoves.getNrOfLegalPartMoves();
  }
  
  private int position () {
    
    return (int)
      Arrays.stream(movePoints)
        .filter(point -> point != -1)
        .count();
  }
  
  private int[] doubleDicePattern () {
    
    int[] dicePattern = new int[4];
    int nrOfUsedDice =
      (position()/2)+((movePoints.length/2)-getNrOfLegalPartMoves());
    
    if (nrOfUsedDice > 0) {
      Arrays.fill(dicePattern,0,nrOfUsedDice,1);
    }
    return
      dicePattern;
  }
  
  private int validDieValue () {
    
    return
      dieValue(
        Arrays.copyOfRange(movePointLayouts
          .getMoveLayoutsList()
          .get(1)
          .getMovePoints(),
          0,
          2
        )
      );
  }
  
  private int invalidDiePosition () {
    
    return
      diePosOf(validDieValue()) == 0 ? 1 : 0;
  }
  
  private int dieValue (int[] movePoints) {
    
    return
      getPlayerID() == 0
        ? movePoints[0] - movePoints[1]
        : (movePoints[0] - movePoints[1])*-1;
  }
  
  private int usedDieValue () {
    
    return
      dieValue(Arrays
        .copyOfRange(
          movePoints,
          0,
          2
        )
      );
  }
  
  private int usedDiePosition () {
    
    return
      diePosOf(
        usedDieValue()
      );
  }
  
  private int diePosOf(int die) {
    
    return
      getDice()[0] == die ? 0 : 1;
  }
  
  private boolean noAvailableDice() {
    
    return
      position() == getNrOfLegalPartMoves()*2;
  }
  
  private boolean gotAnInvalidDie () {
    
    return
      position() < 2 && getNrOfLegalPartMoves() == 1;
  }
  
  private boolean oneDieIsUsed () {
    
    return
      position() > 1;
  }
  
  private int[] regularDicePattern () {
    
    int[] dicePattern = new int[2];
    
    if (noAvailableDice()) {
      Arrays.fill(dicePattern,1);
    } else if (gotAnInvalidDie()) {
      dicePattern[invalidDiePosition()] = 1;
    } else if (oneDieIsUsed()) {
      dicePattern[usedDiePosition()] = 1;
    }
    return
      dicePattern;
  }
  
  public int[] dicePattern () {
    
    return
      parentMoves.getDiceObj().areDouble()
        ? doubleDicePattern()
        : regularDicePattern();
  }
  
}
