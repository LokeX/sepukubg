package bg.engine.api.matchPlay;

public class ActionState {

  private MatchPlay matchPlay;

  public ActionState getActionState (MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
    return this;
  }

  private MatchState matchState () {

    return matchPlay.getMatchState();
  }

  private boolean lastTurnSelected () {

    return
      matchPlay.matchIsPlaying()
      && matchState().lastTurnSelected();
  }

  private boolean playerIsHuman () {

    return
      matchPlay.matchIsPlaying()
      && matchState().playerIsHuman();
  }

  private boolean newMatchPlay() {

    return
      matchPlay.matchIsPlaying()
      && matchPlay.matchOver();
  }

  private boolean newGamePlay() {

    return
      matchPlay.gameOver();
  }

  private boolean playedMoveSelected () {

    return
      matchPlay.matchIsPlaying()
        && matchState().playedMoveSelected();
  }

  private boolean playHumanMove () {

    return
      playMove()
        && playerIsHuman();
  }

  private boolean playMove () {

    return
      !matchPlay.matchOver()
        && !matchPlay.gameOver()
        && (lastTurnSelected() || !playedMoveSelected());
  }

  private boolean scenarioEdit () {

    return matchState() == null;
  }

  public boolean nextPlayAvailable () {

    return
      nextPlayState().length() == 0;
  }

  public String nextPlayState () {

    return
      scenarioEdit()
        ? "Start match"
        : newMatchPlay()
        ? "New Match"
        : newGamePlay()
        ? "New game"
        : playHumanMove()
        ? "Play move"
        : playMove()
        ? "Roll dice"
        : "";
  }

}
