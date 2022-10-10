package engine.api;

import engine.core.Dice;
import engine.core.moves.Layout;
import engine.core.trainer.Trainer;
import engine.play.MatchPlay;
import engine.play.game.GameState;

public class StateEdit {
  
  private SepukuPlay sepukuPlay;
  
  public StateEdit (SepukuPlay sepukuPlay) {
    
    this.sepukuPlay = sepukuPlay;
  }

  public void inputStatScoreToWin (int newScore) {

    if (newScore < 100 && newScore % 2 == 1) {
      Trainer.statScoreToWin = newScore;
    }
  }

  private boolean layoutNameOK (String name) {

    return
      name != null
      && name.length() > 0
      && !sepukuPlay.scenarios().isMember(name);
  }

  public void saveLayout (String name, Layout layout) {

    if (layoutNameOK(name)) {
      sepukuPlay.scenarios().addNamedLayout(name, layout);
    }
  }

  public void renameSelectedLayout (String name) {

    if (layoutNameOK(name)) {
      sepukuPlay.scenarios().setSelectedScenariosName(name);
    }
  }
  
  private GameState gameState () {
    
    return
      sepukuPlay.matchPlay().gameState();
  }
  
  private MatchPlay matchPlay () {
    
    return
      sepukuPlay.matchPlay();
  }

  public void inputNewDice (String diceInput) {

    if (diceInput != null && sepukuPlay.gameIsPlaying()) {

      Dice dice = new Dice(diceInput);

      if (dice.diceAreValid()) {
        matchPlay().selectedTurn().setDice(dice.getDice());
        gameState().truncateTurns(gameState().getTurnNr());
        gameState().setMoveNr(matchPlay().getPlayedMoveNr());
        matchPlay().search().searchRolledMoves();
        matchPlay().move();
      }
    }
  }

  public void inputPlayToScore (int newScore) {

    if (newScore < 100 && newScore % 2 == 1) {
      sepukuPlay.matchBoard().setPlayToScore(newScore);
      sepukuPlay.settings().setScoreToWin(newScore);
    }
  }
  
  public void inputPlayerMatchScore (int playerID, int newScore) {
    
    if (newScore >= 0 && newScore < 100) {
      sepukuPlay.matchBoard().setPlayersMatchScore(playerID, newScore);
    }
  }

  public void inputCubeValue (int newCubeValue) {

    if (newCubeValue >= 2 && newCubeValue <= 64 && newCubeValue % 2 == 0) {
      gameState().gameCube().setValue(newCubeValue);
    }
  }

}
