package bg.engine.api;

import bg.engine.core.Dice;
import bg.engine.core.moves.Layout;
import bg.engine.core.trainer.Trainer;

public class StateEdit {
  
  private Sepuku sepuku;
  
  public StateEdit (Sepuku sepuku) {
    
    this.sepuku = sepuku;
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
      && !sepuku.getScenarios().isMember(name);
  }

  public void saveLayout (String name, Layout layout) {

    if (layoutNameOK(name)) {
      sepuku.getScenarios().addNamedLayout(name, layout);
    }
  }

  public void renameSelectedLayout (String name) {

    if (layoutNameOK(name)) {
      sepuku.getScenarios().setSelectedScenariosName(name);
    }
  }

  public void inputNewDice (String diceInput) {

    if (diceInput != null && sepuku.gameIsPlaying()) {

      Dice dice = new Dice(diceInput);

      if (dice.diceAreValid()) {
        sepuku.getMatchPlay().getSelectedTurn().setDice(dice.getDice());
        sepuku.getGame().truncateTurns(sepuku.getGameState().getTurnNr());
        sepuku.getGameState().setMoveNr(sepuku.getMatchPlay().getPlayedMoveNr());
        sepuku.getMatchPlay().getSearch().searchRolledMoves();
        sepuku.getMatchPlay().move();
      }
    }
  }

  public void inputPlayToScore (int newScore) {

    if (newScore < 100 && newScore % 2 == 1) {
      sepuku.getMatchBoard().setPlayToScore(newScore);
      sepuku.getSettings().setScoreToWin(newScore);
    }
  }
  
  public void inputPlayerMatchScore (int playerID, int newScore) {
    
    if (newScore >= 0 && newScore < 100) {
      sepuku.getMatchBoard().setPlayersMatchScore(playerID, newScore);
    }
  }

  public void inputCubeValue (int newCubeValue) {

    if (newCubeValue >= 2 && newCubeValue <= 64 && newCubeValue % 2 == 0) {
      sepuku.getGame().getGameCube().setValue(newCubeValue);
    }
  }

}
