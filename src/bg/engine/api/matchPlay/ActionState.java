package bg.engine.api.matchPlay;

public class ActionState {

  private MatchState matchState;

  ActionState (MatchState matchState) {

    this.matchState = matchState;
  }

  private MatchState matchState () {

    return matchState;
  }

  private boolean lastTurnSelected () {

    return
      matchState.gameIsPlaying()
      && matchState().lastTurnSelected();
  }

  private boolean playerIsHuman () {

    return
      matchState.gameIsPlaying()
      && matchState().playerIsHuman();
  }

  private boolean newMatchPlay() {

    return
      matchState.gameIsPlaying()
      && matchState.matchOver();
  }

  private boolean newGamePlay() {

    return
      matchState.gameOver();
  }

  private boolean playedMoveSelected () {

    return
      matchState.gameIsPlaying()
        && matchState().playedMoveSelected();
  }

  private boolean playHumanMove () {

    return
      playMove() && playerIsHuman();
  }

  private boolean playMove () {

    return
      !matchState.matchOver()
        && !matchState.gameOver()
        && (lastTurnSelected() || !playedMoveSelected());
  }

  private boolean scenarioEdit () {

    return matchState() == null;
  }

  public boolean nextPlayAvailable () {

    return
      nextPlay().length() == 0;
  }

  public String nextPlay() {

    return
        scenarioEdit()
      ? "Start match"
      : newMatchPlay()
      ? "New match"
      : newGamePlay()
      ? "New game"
      : playHumanMove()
      ? "Play move"
      : playMove()
      ? "Roll dice"
      : "";
  }

}
