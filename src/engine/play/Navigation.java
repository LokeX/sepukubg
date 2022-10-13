package engine.play;

import engine.play.gamePlay.GamePlay;

public class Navigation {
  
  private MatchPlay matchPlay;
  private boolean isNavigating = false;
  
  public Navigation (MatchPlay matchPlay) {
    
    this.matchPlay = matchPlay;
  }
  
  private GamePlay gameState () {
    
    return
      matchPlay.gameState();
  }
  
  public boolean isNavigating () {
    
    return isNavigating;
  }
  
  public void setIsNavigating (boolean navigating) {
    
    isNavigating = navigating;
  }
  
  public void selectAndOutputNextTurn () {
    
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextTurn().getPlayedMove()
    );
  }
  
  public void selectAndOutputPreviousTurn () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousTurn().getPlayedMove()
    );
  }
  
  public void selectAndOutputNextHumanTurn () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextHumanTurn().getPlayedMove()
    );
  }
  
  public void selectAndOutputPreviousHumanTurn () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousHumanTurn().getPlayedMove()
    );
  }
  
  public void selectAndOutputLatestHumanTurn () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectLatestHumanTurn().getPlayedMove()
    );
  }
  
  public void selectAndOutputNextMove () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextMove()
    );
  }
  
  public void selectAndOutputPreviousMove () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousMove()
    );
  }
  
  public void selectAndOutputNextPartMove () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextPartMove()
    );
  }
  
  public void selectAndOutputPreviousPartMove () {
  
    isNavigating = true;
    matchPlay.humanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousPartMove()
    );
  }
  
}
