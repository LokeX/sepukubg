package engine.api;

import engine.core.Dice;
import engine.core.moves.Layout;
import engine.core.trainer.Trainer;

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
      && !sepukuPlay.getScenarios().isMember(name);
  }

  public void saveLayout (String name, Layout layout) {

    if (layoutNameOK(name)) {
      sepukuPlay.getScenarios().addNamedLayout(name, layout);
    }
  }

  public void renameSelectedLayout (String name) {

    if (layoutNameOK(name)) {
      sepukuPlay.getScenarios().setSelectedScenariosName(name);
    }
  }

  public void inputNewDice (String diceInput) {

    if (diceInput != null && sepukuPlay.gameIsPlaying()) {

      Dice dice = new Dice(diceInput);

      if (dice.diceAreValid()) {
        sepukuPlay.getMatchPlay().getSelectedTurn().setDice(dice.getDice());
        sepukuPlay.getGame().truncateTurns(sepukuPlay.getGameState().getTurnNr());
        sepukuPlay.getGameState().setMoveNr(sepukuPlay.getMatchPlay().getPlayedMoveNr());
        sepukuPlay.getMatchPlay().getSearch().searchRolledMoves();
        sepukuPlay.getMatchPlay().move();
      }
    }
  }

  public void inputPlayToScore (int newScore) {

    if (newScore < 100 && newScore % 2 == 1) {
      sepukuPlay.getMatchBoard().setPlayToScore(newScore);
      sepukuPlay.getSettings().setScoreToWin(newScore);
    }
  }
  
  public void inputPlayerMatchScore (int playerID, int newScore) {
    
    if (newScore >= 0 && newScore < 100) {
      sepukuPlay.getMatchBoard().setPlayersMatchScore(playerID, newScore);
    }
  }

  public void inputCubeValue (int newCubeValue) {

    if (newCubeValue >= 2 && newCubeValue <= 64 && newCubeValue % 2 == 0) {
      sepukuPlay.getGame().getGameCube().setValue(newCubeValue);
    }
  }

}
