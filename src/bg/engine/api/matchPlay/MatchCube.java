package bg.engine.api.matchPlay;

import bg.engine.api.EngineApi;
import bg.engine.api.score.MatchBoard;
import bg.engine.coreLogic.Game;

import static bg.Main.settings;
import static bg.Main.win;
import static bg.util.Dialogs.confirmed;
import static bg.util.Dialogs.showMessage;

public class MatchCube {

  private EngineApi engineApi;

  public MatchCube(EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  private Game getGame () {

    return engineApi.getGame();
  }

  private MatchBoard getMatchBoard () {

    return engineApi.getMatchState().getMatchBoard();
  }

  private boolean turnsPlayerIsHuman () {

    return
      engineApi
        .humanTurnSelected();
  }

  boolean cubeWasRejected () {

    return
      engineApi
        .gameIsPlaying()
      &&
        engineApi.getGame()
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

    System.out.println("Computer handles cube:");
    System.out.println("Next player is computer: "+nextPlayerIsComputer());
    System.out.println("Is Crawford game: "+getMatchBoard().isCrawfordGame());
    System.out.println("Cube was rejected: "+cubeWasRejected());
    System.out.println("Computer can offer cube: "+getGame().playerCanOfferCube());
    System.out.println("Computer should double: "+engineApi.getSelectedMove().shouldDouble());
    if (nextPlayerIsComputer()) {
      if (!getMatchBoard().isCrawfordGame() && !cubeWasRejected()) {
        if (getGame().playerCanOfferCube() && engineApi.getSelectedMove().shouldDouble()) {

          resolveCubeHandling();
        }
      }
    }
  }

  public void humanHandlesCube () {

    if (!nextPlayerIsComputer()) {
      if (!getMatchBoard().isCrawfordGame() && engineApi.getNrOfTurns() > 0) {
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
        engineApi.getMatchState().endTurn();
      }
    }
  }

  private boolean nextPlayerIsComputer () {

    return
      settings
        .playerIsComputer(
          engineApi.getPlayerOnRollsID() == 0 ? 1 : 0
        );
  }

}
