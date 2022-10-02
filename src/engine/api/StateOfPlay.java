package engine.api;

import engine.play.MatchPlay;
import engine.play.humanMove.HumanMove;

public class StateOfPlay {

  private SepukuPlay sepukuPlay;
  private boolean autoComplete = false;
  private String[] playTitles = new String[] {
    "Start match",
    "New match",
    "New game",
    "Play move",
    "Roll dice",
    ""
  };

  public boolean isSearching () {

    return
      sepukuPlay.getMatchPlay().getSearch().isSearching();
  }

  void setAutoComplete (boolean complete) {

    autoComplete = complete;
  }

  public StateOfPlay(SepukuPlay engineApi) {

    this.sepukuPlay = engineApi;
  }

  private MatchPlay matchPlay () {

    return sepukuPlay.getMatchPlay();
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
      && lastTurnSelected();
  }

  private boolean newGamePlay() {

    return
      matchPlay().gameOver()
      && lastTurnSelected();
  }

  private boolean playedMoveSelected () {

    return
      matchPlay().gameIsPlaying()
      && matchPlay().playedMoveSelected();
  }

  private HumanMove humanMove () {

    return
      matchPlay()
        .getHumanMove();
  }
  
  private boolean illegalMove () {
    
    return
      matchPlay().getSelectedMove().isIllegal();
  }

  private boolean humanInputComplete () {

    return
      illegalMove()
      || humanMove().inputReady()
      && matchPlay()
        .getHumanMove()
        .getMoveSelection()
        .endOfInput();
  }

  private boolean playHumanMove () {

    return
      playerIsHuman()
      && playMove();
  }

  private boolean playMove () {

    return
      !matchPlay().matchOver()
      && !matchPlay().gameOver()
      && !matchPlay().getMoveOutput().isBusy()
      && (lastTurnSelected() || !playedMoveSelected())
      && (!playerIsHuman() || humanInputComplete());
  }

  private boolean scenarioEdit () {

    return
      sepukuPlay.getScenarios().isEditing();
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
      scenarioEdit()
      ? 0
      : newMatchPlay()
      ? 1
      : newGamePlay()
      ? 2
      : playHumanMove()
      ? 3
      : playMove()
      ? 4
      : 5;
  }

}
