package engine.play;

import engine.play.game.GameState;

public class Navigation {
  
  private MatchPlay matchPlay;
  private boolean isNavigating = false;
  
  public Navigation (MatchPlay matchPlay) {
    
    this.matchPlay = matchPlay;
  }
  
  private GameState gameState () {
    
    return
      matchPlay.getGameState();
  }
  
  public boolean isNavigating () {
    
    return isNavigating;
  }
  
  public void setIsNavigating (boolean navigating) {
    
    isNavigating = navigating;
  }
  
  public void selectAndDisplayNextTurn() {
    
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextTurn().getPlayedMove()
    );
  }
  
  public void selectAndDisplayPreviousTurn() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousTurn().getPlayedMove()
    );
  }
  
  public void selectAndDisplayNextHumanTurn() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextHumanTurn().getPlayedMove()
    );
  }
  
  public void selectAndDisplayPreviousHumanTurn() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousHumanTurn().getPlayedMove()
    );
  }
  
  public void selectAndDisplayLatestHumanTurn() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectLatestHumanTurn().getPlayedMove()
    );
  }
  
  public void selectAndDisplayNextMove() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextMove()
    );
  }
  
  public void selectAndDisplayPreviousMove() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousMove()
    );
  }
  
  public void selectAndDisplayNextPartMove() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectNextPartMove()
    );
  }
  
  public void selectAndDisplayPreviousPartMove() {
  
    isNavigating = true;
    matchPlay.getHumanMove().endMove();
    matchPlay.getMoveOutput().setOutputLayout(
      gameState().selectPreviousPartMove()
    );
  }
  
}
