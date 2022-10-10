package engine.play;

import engine.play.score.MatchBoard;
import engine.core.Game;

import static sepuku.WinApp.win;
import static util.Dialogs.confirmed;
import static util.Dialogs.showMessage;

public class MatchCube {

  private MatchPlay matchPlay;

  public MatchCube(MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
  }

  private Game getGame () {

    return matchPlay.gameState();
  }

  private MatchBoard getMatchBoard () {

    return matchPlay.matchBoard();
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
        matchPlay.gameState()
        .gameCube()
        .cubeWasRejected();
  }

  private void resolveCubeHandling () {

    if (playerIsHuman()) {
      getGame().gameCube().setCubeWasRejected(
        !confirmed(
          "Opponent doubles - accept?",win
        )
      );
    } else {
      getGame().gameCube().setCubeWasRejected(
        !matchPlay.selectedMove().shouldTake()
      );
    }
    if (!cubeWasRejected()) {
      getGame().playerDoubles();
    }
  }
  
  private boolean shouldDouble () {
    
    return
      matchPlay
        .selectedMove()
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