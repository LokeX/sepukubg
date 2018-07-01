package bg.engine.api.matchPlay;

import static bg.Main.engineApi;

public class MatchPlay {

  MatchState matchState;

  private boolean matchIsPlaying () {

    return
      matchState != null;
  }

  private boolean matchOver () {

    return
      matchIsPlaying()
      && matchState.matchOver();
  }

  private boolean gameOver () {

    return
      matchIsPlaying()
      && !matchOver()
      && matchState.gameOver();
  }

  private boolean lastTurnSelected () {

    return
      matchIsPlaying()
      && matchState
          .lastTurnSelected();
  }

  private boolean playerIsHuman () {

    return
      matchIsPlaying()
      && matchState.playerIsHuman();
  }

  private boolean newMatch () {

    return
      !matchIsPlaying() || matchOver();
  }

  private boolean newGame () {

    return
      matchIsPlaying()
      && !matchOver()
      && matchState.gameOver();
  }

  private boolean playedMoveSelected () {

    return
      matchIsPlaying()
      && matchState.playedMoveSelected();
  }

  private HumanMove humanMove () {

    return matchState.getHumanMove();
  }

  private boolean playHumanMove () {

    return
      playMove()
      && playerIsHuman()
      && humanMove().endOfInput();
  }

  private boolean playMove () {

    return
      !matchOver()
      && !gameOver()
      && (lastTurnSelected() || !playedMoveSelected());
  }

  private String nextPlayState () {

    return
        newMatch()
      ? "New Match"
      : newGame()
      ? "New game"
      : playHumanMove()
      ? "Play move"
      : playMove()
      ? "Roll dice"
      : "";
  }

  private boolean nextPlayAvailable () {

    return
      nextPlayState().length() == 0;
  }

  public void actionButtonClicked () {

    if (matchState.gameIsPlaying()) {
      engineApi.getMatchCube().computerHandlesCube();
      if (engineApi.getMatchCube().cubeWasRejected()) {
        matchState.endTurn();
        return;
      }
    }
    if (matchOver()) {
      matchState = new MatchState();
    } else if (gameOver()) {
      matchState.newGame();
    } else {
      matchState.newTurn();
    }
  }


}
