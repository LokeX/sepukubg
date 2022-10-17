package engine.play;

import engine.play.score.MatchBoard;
import engine.core.Game;

import static sepuku.WinApp.win;
import static util.Dialogs.confirmed;
import static util.Dialogs.showMessage;

public class MatchCube {

  private MatchPlay matchPlay;
  private boolean cubeIsOffered = false;

  public MatchCube(MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
  }

  private Game getGame () {

    return matchPlay.gamePlay();
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
        matchPlay.gamePlay()
        .gameCube()
        .cubeWasRejected();
  }

  private void resolveCubeHandling () {

    if (playerIsHuman()) {
      cubeIsOffered = true;
//      getGame().gameCube().setCubeWasRejected(
//        !confirmed(
//          "Opponent doubles - accept?",win
//        )
//      );
    } else {
      getGame().gameCube().setCubeWasRejected(
        !matchPlay.selectedMove().shouldTake()
      );
      if (!cubeWasRejected()) {
        getGame().playerDoubles();
      }
    }
  }
  
  private boolean shouldDouble () {
    
    return
      matchPlay
        .selectedMove()
        .shouldDouble();
  }
  
  private boolean computerShouldOfferCube() {
    
    return
      !matchPlay.autoCompleteGame()
      && matchPlay.gameIsPlaying()
      && !cubeIsOffered
      && !matchPlay.gameOver()
      && playerIsComputer()
      && mayOfferCube()
      && shouldDouble();
  }

  public void computerHandlesCube () {

    if (computerShouldOfferCube()) {
      resolveCubeHandling();
    }
  }
  
  public boolean cubeIsOffered() {
    
    return
      cubeIsOffered;
  }
  
  private boolean mayOfferCube () {
    
    return
      matchPlay.gameIsPlaying()
      && getGame().playerCanOfferCube()
      && !getMatchBoard().isCrawfordGame();
  }
  
  public void humanAcceptsCube () {
    
    cubeIsOffered = false;
    getGame().gameCube().setCubeWasRejected(false);
    getGame().playerDoubles();
  }
  
  public void humanHandlesCube () {

    if (!playerIsComputer() && mayOfferCube()) {
      resolveCubeHandling();
//      if (cubeWasRejected()) {
//        showMessage(
//          "Opponent rejects the double"
//            + "\nand resigns!",
//          win
//        );
//        matchPlay.endTurn();
//      }
    }
  }

  private boolean playerIsComputer() {

    return
      matchPlay.settings()
        .playerIsComputer(
          matchPlay.playerOnRollsID() == 0 ? 1 : 0
        );
  }

  void setCubeIsOffered (boolean offered) {
    
    cubeIsOffered = offered;
  }
  
}