package bg.engine.api;

import bg.engine.coreLogic.Dice;
import bg.engine.coreLogic.moves.Layout;
import bg.engine.coreLogic.trainer.Trainer;

import javax.swing.*;

import static bg.Main.*;
import static bg.Main.win;
import static bg.util.Dialogs.getIntegerInput;
import static bg.util.Dialogs.showMessage;

public class StateEdit {

  public void inputStatScoreToWin () {

    int newScore = getIntegerInput(
      "Type the score required " +
      "to win a statistical match:",
      win
    );

    if (newScore > 0 && newScore < 100 && newScore % 2 == 1) {
      Trainer.statScoreToWin = newScore;
    }
  }

  public String inputLayoutName () {

    String name = JOptionPane.
      showInputDialog(win, "Input scenario name:");

    if (name != null && engineApi.getScenarios().isMember(name)) {
      name = null;
      showMessage("Name is already in use!", win);
    }
    return name;
  }

  public void saveLayout (Layout layout) {

    String name = inputLayoutName();

    if (name != null && name.length() > 0) {
      engineApi.getScenarios().addNamedLayout(name, layout);
    }
  }

  public void renameSelectedLayout () {

    String name = inputLayoutName();

    if (name != null && !engineApi.getScenarios().isMember(name)) {
      engineApi.getScenarios().setSelectedScenariosName(name);
    }
  }

  public void inputNewDice () {

    if (engineApi.getLatestTurn() != null) {

      String diceInput = JOptionPane.showInputDialog(win,
        "Input dice values with no separation" +
          "\nfor doubles type only one value"
      );

      if (diceInput != null) {

        Dice dice = new Dice(diceInput);

        if (dice.diceAreValid()) {
          dice.printDice();
          System.out.println("Input dice are valid");
          engineApi.getSelectedTurn().setDice(dice.getDice());
//          System.out.println("Input dice set");
          engineApi.getGame().truncateTurns(engineApi.getGameState().getTurnNr());
          System.out.println("Truncated from turn: "+engineApi.getGameState().getTurnNr());
          engineApi.getGameState().setMoveNr(engineApi.getPlayedMoveNr());
          System.out.println("moveNr set: "+engineApi.getPlayedMoveNr());
          engineApi.getMatchPlay().getSearch().searchRolledMoves();
          System.out.println("Moves searched");
          engineApi.getMatchPlay().move();
        }
      }
    }
  }

  public void inputPlayToScore () {

    int newScore = getIntegerInput(
      "Type the score required to win the game:" +
        "\nType 0 (or hit Enter) for money-game",win
    );

    if (newScore >= 0 && newScore < 100 && newScore % 2 == 1) {
      engineApi.getMatchBoard().setPlayToScore(newScore);
      engineApi.getSettings().setScoreToWin(newScore);
    }
  }

  public void inputPlayerMatchScore (int playerID) {

    String[] titles = new String[] {"White", "Black"};

    int newScore = getIntegerInput(
      "Type "+titles[playerID]+" players score:",win
    );

    if (newScore >= 0 && newScore < 100) {
      engineApi.getMatchBoard().setPlayersMatchScore(playerID, newScore);
    }
  }

  public void inputCubeValue () {

    int newCubeValue = getIntegerInput(
      "Type the new doublingCube value:",win
    );

    if (newCubeValue >= 2 && newCubeValue <= 64 && newCubeValue % 2 == 0) {
      engineApi.getGame().getGameCube().setValue(newCubeValue);
    }
  }

}
