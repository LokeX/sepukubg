package bg.api;

import bg.engine.Dice;
import bg.engine.trainer.Trainer;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import javax.swing.*;

import static bg.Main.*;
import static bg.Main.win;
import static bg.util.Dialogs.getIntegerInput;
import static bg.util.Dialogs.showMessage;

public class Input extends MatchApi {

  public Input(MatchApi matchApi) {

    super(matchApi);
  }

  public void inputStatScoreToWin () {

    int newScore = getIntegerInput(
      "Type the scoreBoard required " +
      "to win a statistical matchApi:",
      win
    );

    if (newScore > 0 && newScore < 100 && newScore % 2 == 1) {
      Trainer.statScoreToWin = newScore;
    }
  }

  public String inputLayoutName () {

    String name = JOptionPane.
      showInputDialog(win, "Input scenario name:");

    if (name != null && scenarios.isMember(name)) {
      name = null;
      showMessage("Name is already in use!", win);
    }
    return name;
  }

  public void saveLayout () {

    String name = inputLayoutName();

    if (name != null && name.length() > 0) {
      scenarios.addDisplayedLayout(name);
      if (matchApi.getLatestTurn() == null) {
        new ScenarioOutput().outputSelectedScenario();
      }
    }
  }

  public void renameSelectedLayout () {

    String name = inputLayoutName();

    if (name != null && !scenarios.isMember(name)) {
      scenarios.setSelectedScenariosName(name);
    }
  }

  public void inputNewDice () {

    if (getLatestTurn() != null) {

      String diceInput = JOptionPane.showInputDialog(win,
        "Input dice values with no seperation" +
          "\nfor doubles type only one value"
      );

      if (diceInput != null) {

        Dice dice = new Dice(diceInput);

        if (dice.diceAreValid()) {
          getSelectedTurn().setDice(dice.getDice());
          getGame().truncateTurns(selectedTurn);
          selectedMove = getPlayedMoveNr();
          searchRolledMoves();
          Move();
        }
      }
    }
  }

  public void inputPlayToScore () {

    int newScore = getIntegerInput(
      "Type the scoreBoard required to win the game:" +
        "\nType 0 (or hit Enter) for moneygame",win
    );

    if (newScore >= 0 && newScore < 100 && newScore % 2 == 1) {
      scoreBoard.setPlayToScore(newScore);
      settings.setScoreToWin(newScore);
    }
  }

  public void inputPlayerMatchScore (int playerID) {

    String[] titles = new String[] {"White", "Black"};

    int newScore = getIntegerInput(
      "Type "+titles[playerID]+" players scoreBoard:",win
    );

    if (newScore >= 0 && newScore < 100) {
      scoreBoard.setPlayersMatchScore(playerID, newScore);
    }
  }

  public void inputCubeValue () {

    int newCubeValue = getIntegerInput(
      "Type the new doublingCube value:",win
    );

    if (newCubeValue >= 2 && newCubeValue <= 64 && newCubeValue % 2 == 0) {
      getGame().getGameCube().setValue(newCubeValue);
    }
  }

}
