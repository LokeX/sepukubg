package bg.engine.play;

import bg.engine.play.score.MatchBoard;
import bg.engine.core.Game;

import static bg.Main.win;
import static bg.util.Dialogs.confirmed;
import static bg.util.Dialogs.showMessage;

public class MatchCube {

  private MatchPlay matchPlay;

  public MatchCube(MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
  }

  private Game getGame () {

    return matchPlay.getGameState();
  }

  private MatchBoard getMatchBoard () {

    return matchPlay.getMatchBoard();
  }

  private boolean playerIsHuman() {

    return
      matchPlay
        .humanTurnSelected();
  }

  boolean cubeWasRejected () {

    return
      matchPlay
        .gameIsPlaying()
      &&
        matchPlay.getGameState()
        .getGameCube()
        .cubeWasRejected();
  }

  private void resolveCubeHandling () {

    if (playerIsHuman()) {
      getGame().getGameCube().setCubeWasRejected(
        !confirmed(
          "Opponent doubles - accept?",win
        )
      );
    } else {
      getGame().getGameCube().setCubeWasRejected(
        !matchPlay.getSelectedMove().shouldTake()
      );
    }
    if (!cubeWasRejected()) {
      getGame().playerDoubles();
    }
  }
  
  private boolean shouldDouble () {
    
    return
      matchPlay
        .getSelectedMove()
        .shouldDouble();
  }

  public void computerHandlesCube () {

    if (playerIsComputer() && mayOfferCube() && shouldDouble()) {
      resolveCubeHandling();
    }
  }
  
  private boolean mayOfferCube () {
    
    return
      matchPlay.gameIsPlaying()
      && getGame().playerCanOfferCube()
      && !getMatchBoard().isCrawfordGame();
  }
  
  public void humanHandlesCube () {

    if (!playerIsComputer() && mayOfferCube()) {
      resolveCubeHandling();
      if (cubeWasRejected()) {
        showMessage(
          "Opponent rejects the double"
            + "\nand resigns!",
          win
        );
        matchPlay.endTurn();
      }
    }
  }

  private boolean playerIsComputer() {

    return
      matchPlay.settings()
        .playerIsComputer(
          matchPlay.getPlayerOnRollsID() == 0 ? 1 : 0
        );
  }

}
