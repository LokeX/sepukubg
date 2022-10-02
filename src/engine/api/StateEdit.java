package engine.api;

import engine.core.Dice;
import engine.core.moves.Layout;
import engine.core.trainer.Trainer;

public class StateEdit {
  
  private PlaySepuku playSepuku;
  
  public StateEdit (PlaySepuku playSepuku) {
    
    this.playSepuku = playSepuku;
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
      && !playSepuku.getScenarios().isMember(name);
  }

  public void saveLayout (String name, Layout layout) {

    if (layoutNameOK(name)) {
      playSepuku.getScenarios().addNamedLayout(name, layout);
    }
  }

  public void renameSelectedLayout (String name) {

    if (layoutNameOK(name)) {
      playSepuku.getScenarios().setSelectedScenariosName(name);
    }
  }

  public void inputNewDice (String diceInput) {

    if (diceInput != null && playSepuku.gameIsPlaying()) {

      Dice dice = new Dice(diceInput);

      if (dice.diceAreValid()) {
        playSepuku.getMatchPlay().getSelectedTurn().setDice(dice.getDice());
        playSepuku.getGame().truncateTurns(playSepuku.getGameState().getTurnNr());
        playSepuku.getGameState().setMoveNr(playSepuku.getMatchPlay().getPlayedMoveNr());
        playSepuku.getMatchPlay().getSearch().searchRolledMoves();
        playSepuku.getMatchPlay().move();
      }
    }
  }

  public void inputPlayToScore (int newScore) {

    if (newScore < 100 && newScore % 2 == 1) {
      playSepuku.getMatchBoard().setPlayToScore(newScore);
      playSepuku.getSettings().setScoreToWin(newScore);
    }
  }
  
  public void inputPlayerMatchScore (int playerID, int newScore) {
    
    if (newScore >= 0 && newScore < 100) {
      playSepuku.getMatchBoard().setPlayersMatchScore(playerID, newScore);
    }
  }

  public void inputCubeValue (int newCubeValue) {

    if (newCubeValue >= 2 && newCubeValue <= 64 && newCubeValue % 2 == 0) {
      playSepuku.getGame().getGameCube().setValue(newCubeValue);
    }
  }

}
