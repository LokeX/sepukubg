package bg.api;

import bg.engine.Game;
import bg.engine.ScoreBoard;

import static bg.Main.settings;
import static bg.Main.win;
import static bg.util.Dialogs.confirmed;
import static bg.util.Dialogs.showMessage;

public class MatchCube {

  private EngineApi engineApi;

  MatchCube(EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  private Game getGame () {

    return engineApi.getGame();
  }

  private ScoreBoard getScoreBoard () {

    return engineApi.getMatchPlay().getScoreBoard();
  }

  private boolean turnsPlayerIsHuman () {

    return
      engineApi
        .turnsPlayerIsHuman(
          engineApi.getLatestTurn()
        );
  }

  boolean cubeWasRejected () {

    return
      engineApi
        .getGame()
        .getGameCube()
        .cubeWasRejected();
  }

  private void resolveCubeHandling () {

    if (turnsPlayerIsHuman()) {
      getGame().getGameCube().setCubeWasRejected(
        !confirmed(
          "Opponent doubles - accept?",win
        )
      );
    } else {
      getGame().getGameCube().setCubeWasRejected(
        !engineApi.getSelectedMove().shouldTake()
      );
    }
    if (!cubeWasRejected()) {
      getGame().playerDoubles();
    }
  }

  void computerHandlesCube () {

    if (nextPlayerIsComputer()) {
      if (!getScoreBoard().isCrawfordGame() && !cubeWasRejected()) {
        if (getGame().playerCanOfferCube() && engineApi.getSelectedMove().shouldDouble()) {
          resolveCubeHandling();
        }
      }
    }
  }

  public void humanHandlesCube () {

    if (!nextPlayerIsComputer()) {
      if (!getScoreBoard().isCrawfordGame() && engineApi.getNrOfTurns() > 0) {
        if (getGame().playerCanOfferCube() || getGame().getGameCube().getOwner() > 0) {
          resolveCubeHandling();
          if (cubeWasRejected()) {
            showMessage(
              "Opponent rejects the double"
                + "\nand resigns!",
              win
            );
          }
        }
      }
      if (getGame().getGameCube().cubeWasRejected()) {
        engineApi.getMatchPlay().endTurn();
      }
    }
  }

  private boolean nextPlayerIsComputer () {

    return
      settings
        .playerIsComputer(
          engineApi.getPlayerOnRollsID()
        );
  }

}
