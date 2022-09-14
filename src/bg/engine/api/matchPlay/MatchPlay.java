package bg.engine.api.matchPlay;

import bg.Main;
import bg.engine.api.moveInput.HumanMove;
import bg.engine.api.score.ScoreBoard;
import bg.engine.coreLogic.moves.Layout;

import static bg.Main.*;
import static bg.Main.settings;

public class MatchPlay {

  private MatchState matchState;
  private ScoreBoard scoreBoard;
  private ActionState actionState;

  MatchPlay () {

    scoreBoard = new ScoreBoard();
    actionState = new ActionState(matchState);
  }

  public MatchState getMatchState () {

    return matchState;
  }

  public ScoreBoard getScoreBoard () {

    return scoreBoard.getScoreBoard(matchState);
  }

//  public ActionState getActionState() {
//
//    return actionState;
//  }

  public HumanMove humanMove () {

    return matchState.getHumanMove();
  }

  boolean matchIsPlaying () {

    return
      matchState != null;
  }

  public boolean gameIsPlaying () {

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
//      matchState.newGameNew();
    } else {
      matchState.newTurn();
    }
  }

}
