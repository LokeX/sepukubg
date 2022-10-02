package engine.play;

import engine.play.score.MatchBoard;
import engine.core.Game;

import static sepuku.App.win;
import static util.Dialogs.confirmed;
import static util.Dialogs.showMessage;

public class MatchCube {

  private PlayMatch playMatch;

  public MatchCube(PlayMatch playMatch) {

    this.playMatch = playMatch;
  }

  private Game getGame () {

    return playMatch.getGameState();
  }

  private MatchBoard getMatchBoard () {

    return playMatch.getMatchBoard();
  }

  private boolean playerIsHuman() {

    return
      playMatch
        .humanTurnSelected();
  }

  boolean cubeWasRejected () {

    return
      playMatch
        .gameIsPlaying()
      &&
        playMatch.getGameState()
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
        !playMatch.getSelectedMove().shouldTake()
      );
    }
    if (!cubeWasRejected()) {
      getGame().playerDoubles();
    }
  }
  
  private boolean shouldDouble () {
    
    return
      playMatch
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
      playMatch.gameIsPlaying()
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
        playMatch.endTurn();
      }
    }
  }

  private boolean playerIsComputer() {

    return
      playMatch.settings()
        .playerIsComputer(
          playMatch.getPlayerOnRollsID() == 0 ? 1 : 0
        );
  }

}
