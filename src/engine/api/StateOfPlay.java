package engine.api;

import engine.play.MatchPlay;
import engine.play.humanMove.HumanMove;

public class StateOfPlay {

  private SepukuPlay sepukuPlay;
  private String[] playTitles = new String[] {
    "Resign",
    "Start match",
    "New match",
    "New game",
    "Play move",
    "Roll dice",
    ""
  };

  public boolean isSearching () {

    return
      sepukuPlay.matchPlay().search().isSearching();
  }

  public StateOfPlay(SepukuPlay engineApi) {

    this.sepukuPlay = engineApi;
  }
  
  private boolean resign () {
    
    return matchPlay().cubeOffered();
  }

  private MatchPlay matchPlay () {

    return sepukuPlay.matchPlay();
  }

  private boolean lastTurnSelected () {

    return
      matchPlay().gameIsPlaying()
      && matchPlay().lastTurnSelected();
  }

  private boolean playerIsHuman () {

    return
      matchPlay().gameIsPlaying()
      && matchPlay().playerIsHuman();
  }

  private boolean newMatchPlay() {

    return
      matchPlay().gameIsPlaying()
      && matchPlay().matchOver()
      && lastTurnSelected()
      && moveComplete();
  }
  
  private boolean moveComplete () {
    
    return
      !playerIsHuman()
      || humanInputComplete()
      || isNavigating() ;
  }

  private boolean newGamePlay() {

    return
      matchPlay().gameOver()
      && lastTurnSelected()
      && moveComplete();
  }

  private HumanMove humanMove () {

    return
      matchPlay()
        .humanMove();
  }
  
  private boolean illegalMove () {
    
    return
      matchPlay().selectedMove().isIllegal();
  }

  private boolean humanInputComplete () {

    return
      illegalMove()
      || humanMove().inputReady()
      && matchPlay()
        .humanMove()
        .getMoveSelection()
        .endOfInput();
  }

  private boolean playHumanMove () {

    return
      playerIsHuman()
      && playMove();
  }
  
  private boolean isNavigating () {
    
    return
      sepukuPlay
        .matchPlay()
        .navigation()
        .isNavigating();
  }

  private boolean playMove () {

    return
      !matchPlay().matchOver()
      && !matchPlay().gameOver()
      && !matchPlay().getMoveOutput().isBusy()
      && moveComplete();
  }

  private boolean scenarioEdit () {

    return
      sepukuPlay.scenarios().isEditing();
  }

  public boolean nextPlayReady () {

    return
      playTitles[nextPlayTitleNr()].length() != 0;
  }

  public String nextPlayTitle () {

    return
      playTitles[nextPlayTitleNr()];
  }

  public int nextPlayTitleNr() {

    return
      resign()
      ? 0
      : scenarioEdit()
      ? 1
      : newMatchPlay()
      ? 2
      : newGamePlay()
      ? 3
      : playHumanMove()
      ? 4
      : playMove()
      ? 5
      : 6;
  }

}
