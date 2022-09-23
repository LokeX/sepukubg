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

    System.out.println("Computer handles cube:");
    System.out.println("Next player is computer: "+!playerIsHuman());
    System.out.println("Is Crawford game: "+getMatchBoard().isCrawfordGame());
    System.out.println("Cube was rejected: "+cubeWasRejected());
    System.out.println("Computer can offer cube: "+getGame().playerCanOfferCube());
    System.out.println("Computer should double: "+matchPlay.getSelectedMove().shouldDouble());

    if (nextPlayerIsComputer() && mayOfferCube() && shouldDouble()) {
      resolveCubeHandling();
    }
  }
  
//  void computerHandlesCube () {
//
//    System.out.println("Computer handles cube:");
//    System.out.println("Next player is computer: "+!playerIsHuman());
//    System.out.println("Is Crawford game: "+getMatchBoard().isCrawfordGame());
//    System.out.println("Cube was rejected: "+cubeWasRejected());
//    System.out.println("Computer can offer cube: "+getGame().playerCanOfferCube());
//    System.out.println("Computer should double: "+matchPlay.getSelectedMove().shouldDouble());
//    if (!playerIsHuman()) {
//      if (!getMatchBoard().isCrawfordGame() && !cubeWasRejected()) {
//        if (getGame().playerCanOfferCube() && matchPlay.getSelectedMove().shouldDouble()) {
//
//          resolveCubeHandling();
//        }
//      }
//    }
//  }
//
  private boolean mayOfferCube () {
    
    return
      matchPlay.gameIsPlaying()
      && getGame().playerCanOfferCube()
      && !getMatchBoard().isCrawfordGame();
  }
  
  public void humanHandlesCube () {

    if (!nextPlayerIsComputer() && mayOfferCube()) {
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
//  public void humanHandlesCube () {
//
//    if (playerIsHuman()) {
//      if (!getMatchBoard().isCrawfordGame() && matchPlay.getNrOfTurns() > 0) {
//        if (getGame().playerCanOfferCube() || getGame().getGameCube().getOwner() > 0) {
//          resolveCubeHandling();
//          if (cubeWasRejected()) {
//            showMessage(
//              "Opponent rejects the double"
//                + "\nand resigns!",
//              win
//            );
//          }
//        }
//      }
//      if (getGame().getGameCube().cubeWasRejected()) {
//        matchPlay.endTurn();
//      }
//    }
//  }

  private boolean nextPlayerIsComputer () {

    return
      matchPlay.settings()
        .playerIsComputer(
          matchPlay.getPlayerOnRollsID() == 0 ? 1 : 0
        );
  }

}
