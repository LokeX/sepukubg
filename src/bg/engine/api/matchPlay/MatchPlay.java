package bg.engine.api.matchPlay;

public class MatchPlay {

  MatchState matchState = new MatchState();

  private boolean okToSetText() {

    return
      matchState != null
      && gameIsPlaying()
      && matchState
        .getGameState()
        .lastTurn() != null;
  }

  private boolean isLastTurn () {

    return
      gameIsPlaying()
      && matchState
        .getGameState()
        .lastTurnSelected();
  }

  private boolean playerIsHuman () {

    return
      gameIsPlaying()
      && matchState
        .getGameState()
        .humanTurnSelected();
  }

  private boolean newMatch () {

    return
      isLastTurn()
      && matchState
        .getScoreBoard()
        .matchOver();
  }

  private boolean newGame () {

    return
      isLastTurn()
      && matchState
        .getGameState()
        .gameOver();
  }

  private String buttonText () {

    return
        newMatch()
      ? "New Match"
      : newGame()
      ? "New game"
      : playerIsHuman()
      ? "Play move"
      : "Roll dice";
  }

  private boolean playedMoveSelected () {

    return
      gameIsPlaying()
      && matchState
        .getGameState()
        .playedMoveSelected();
  }

  private boolean gameIsPlaying () {

    return
      matchState
        .gameIsPlaying();
  }

  private boolean gameOver () {

    return
      gameIsPlaying()
      && matchState
        .getGameState()
        .gameOver();
  }

  public boolean showButton() {

    boolean showButton = false;

    if (okToSetText()) {
      if (isLastTurn()) {
//        showButton = !hideActionButton;
      } else if (!playedMoveSelected()) {
        showButton = true;
      } else {
        showButton = false;
      }
    }
    return
      showButton
      || gameIsPlaying()
      || gameOver()
      && isLastTurn();
  }

}
