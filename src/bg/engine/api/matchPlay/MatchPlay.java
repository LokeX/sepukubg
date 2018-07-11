package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.DisplayLayouts;
import bg.engine.api.gameState.humanMove.HumanMove;
import bg.engine.api.score.ScoreBoard;
import bg.engine.match.moves.Layout;

import static bg.Main.*;
import static bg.Main.settings;

public class MatchPlay {

  private MatchState matchState;
  private ScoreBoard scoreBoard;
  private ActionState actionState;
  private DisplayLayouts displayLayouts;

  MatchPlay () {

    scoreBoard = new ScoreBoard();
    actionState = new ActionState();
    displayLayouts = new DisplayLayouts();
  }

  public MatchState getMatchState () {

    return matchState;
  }

  public ScoreBoard getScoreBoard () {

    return scoreBoard.getScoreBoard(matchState);
  }

  public ActionState getActionState() {

    return actionState.getActionState(this);
  }

  public HumanMove humanMove () {

    return matchState.getHumanMove();
  }

  boolean matchIsPlaying () {

    return
      matchState != null;
  }

  boolean gameIsPlaying () {

    return
      matchIsPlaying()
      && matchState.gameIsPlaying();
  }

  boolean matchOver () {

    return
      matchIsPlaying()
      && matchState.matchOver();
  }

  boolean gameOver () {

    return
      matchIsPlaying()
      && !matchOver()
      && matchState.gameOver();
  }

  private Layout matchLayout () {

    Layout matchLayout;

    matchLayout = new Layout(win.canvas.getDisplayedLayout());
    matchLayout.setPlayerID(settings.getGameStartMode());
    matchLayout.setUseWhiteBot(settings.getWhiteBotOpponent());
    matchLayout.setUseBlackBot(settings.getBlackBotOpponent());
    return matchLayout;
  }

  private void startScenarioEdit () {

    matchState = null;
    getLayoutEditor().startEditor();
  }

  private void newMatch () {

    Main.getLayoutEditor().endEdit();
    matchState = new MatchState(matchLayout());
  }

  public void nextAction () {

    if (gameIsPlaying()) {
      engineApi.getMatchCube().computerHandlesCube();
      if (engineApi.getMatchCube().cubeWasRejected()) {
        matchState.endTurn();
        return;
      }
    }
    if (matchOver()) {
      startScenarioEdit();
    } else if (!matchIsPlaying()) {
      newMatch();
    } else if (gameOver()) {
      matchState.newGame();
    } else {
      matchState.newTurn();
    }
  }

}
