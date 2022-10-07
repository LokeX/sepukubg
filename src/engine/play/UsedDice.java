package engine.play;

import java.util.Arrays;

public class UsedDice {
  
  private MatchPlay matchPlay;
  private int[] noUsedDicePattern;
  private int[] allUsedDicePattern;
  
  public UsedDice(MatchPlay matchPlay) {
    
    this.matchPlay = matchPlay;
  }
  
  private int nrOfMovePointLayouts () {
    
    return
      matchPlay
        .selectedMove()
        .getMovePointLayouts()
        .size();
  }
  
  private int nrOfOutputLayouts () {
    
    return
      matchPlay.getMoveOutput().nrOfOutputLayouts();
  }
  
  private boolean allDiceAreUsed () {
    
    return
      !matchPlay.gameIsPlaying() || matchPlay.selectedMove().isIllegal()
      || (nrOfOutputLayouts() == 0 && !matchPlay.getSearch().isSearching());
  }
  
  private int outputLayoutNr () {
    
    return
      Math.max(nrOfMovePointLayouts() - 1 - nrOfOutputLayouts(), 0);
  }
  
  private int[] dicePattern () {
    
    return
      matchPlay
      .selectedMove()
      .dicePatterns()
      .get(outputLayoutNr());
  }
  
  private int[] dice () {
    
    return
      matchPlay.getSelectedTurn().getDice();
  }
  
  private boolean isInvalidDicePattern (int[] dicePattern) {
    
    return
      dicePattern == null || dicePattern.length != dice().length;
  }

  private int[] noUsedDicePattern () {
    
    if (isInvalidDicePattern(noUsedDicePattern)) {
      noUsedDicePattern = new int[dice().length];
    }
    return
      noUsedDicePattern;
  }
  
  private int[] allUsedDicePattern () {
    
    if (isInvalidDicePattern(allUsedDicePattern)) {
      allUsedDicePattern = new int[dice().length];
      Arrays.fill(allUsedDicePattern,1);
    }
    return
      allUsedDicePattern;
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
      && !matchPlay.selectedMove().isIllegal();
  }
  
  public int[] getUsedDicePattern () {
    
    return
      isLegalHumanMove()
      ? humanUsedDicePattern()
      : computerUsedDicePattern();
  }
  
}
