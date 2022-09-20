package bg.engine.api.matchPlay;

import bg.engine.api.EngineApi;
import bg.engine.api.moveInput.HumanMove;

public class ActionState {

  private EngineApi engineApi;
  private String[] playTitles =
    new String[] {
      "Start match",
      "New match",
      "New game",
      "Play move",
      "Roll dice",
      ""
    };

  public ActionState (EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  private MatchPlay matchState () {

    return engineApi.getMatchPlay();
  }

  private boolean lastTurnSelected () {

    return
      matchState().gameIsPlaying()
      && matchState().lastTurnSelected();
  }

  private boolean playerIsHuman () {

    return
      matchState().gameIsPlaying()
      && matchState().playerIsHuman();
  }

  private boolean newMatchPlay() {

    return
      matchState().gameIsPlaying()
      && matchState().matchOver()
      && lastTurnSelected();
  }

  private boolean newGamePlay() {

    return
      matchState().gameOver()
      && lastTurnSelected();
  }

  private boolean playedMoveSelected () {

    return
      matchState().gameIsPlaying()
      && matchState().playedMoveSelected();
  }

  private HumanMove humanMove () {

    return
      matchState()
        .getHumanMove();
  }

  private boolean humanInputComplete () {

    return
      humanMove().inputReady()
      &&
      matchState()
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
      !matchState().matchOver()
      && !matchState().gameOver()
      && !matchState().getMoveOutput().isBusy()
      && (lastTurnSelected() || !playedMoveSelected())
      && (!playerIsHuman() || humanInputComplete());
  }

  private boolean scenarioEdit () {

    return
      engineApi.getScenarios().isEditing();
//      matchState() == null;
//      !matchState().gameIsPlaying();
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
