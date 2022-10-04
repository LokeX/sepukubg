package engine.play;

import java.util.Arrays;

public class UsedDice {
  
  private MatchPlay matchPlay;
  
  public UsedDice(MatchPlay matchPlay) {
    
    this.matchPlay = matchPlay;
  }
  
  private int nrOfMovePointLayouts () {
    
    return
      matchPlay
        .getSelectedMove()
        .getMovePointLayouts()
        .size();
  }
  
  private int nrOfOutputLayouts () {
    
    return
      matchPlay.getMoveOutput().nrOfOutputLayouts();
  }
  
  private boolean allDiceAreUsed () {
    
    return
      !matchPlay.gameIsPlaying() || matchPlay.getSelectedMove().isIllegal()
      || (nrOfOutputLayouts() == 0 && !matchPlay.getSearch().isSearching());
  }
  
  private int[] dicePattern () {
    
    return
      matchPlay
      .getSelectedMove()
      .dicePatterns()
      .get(nrOfMovePointLayouts() - nrOfOutputLayouts());
  }

  private int[] noUsedDicePattern () {
    
    return
      new int[matchPlay.getSelectedTurn().getDice().length];
  }
  
  private int[] allUsedDicePattern () {
    
    int[] dicePattern = noUsedDicePattern();
    
    Arrays.fill(dicePattern,1);
    return
      dicePattern;
  }
  
  private boolean outputtingMove () {
    
    return
      matchPlay.getMoveOutput().nrOfOutputLayouts() > 0;
  }
  
  private int[] computerUsedDicePattern () {
    
    return
      outputtingMove()
      ? dicePattern()
      : allDiceAreUsed()
      ? allUsedDicePattern()
      : noUsedDicePattern();
  }
  
  private int[] humanUsedDicePattern () {
    
    return
      matchPlay
      .getHumanMove()
      .getMoveSelection()
      .dicePattern();
  }
  
  private boolean isLegalHumanMove () {
    
    return
      matchPlay.playerIsHuman()
      && matchPlay.getHumanMove().getMoveSelection() != null
      && !matchPlay.getSelectedMove().isIllegal();
  }
  
  public int[] getUsedDicePattern () {
    
    return
      isLegalHumanMove()
      ? humanUsedDicePattern()
      : computerUsedDicePattern();
  }
  
}
