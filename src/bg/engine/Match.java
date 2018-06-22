package bg.engine;

import bg.engine.moves.Layout;
import bg.engine.trainer.Trainer;

public class Match {

  protected Score score;
  protected Game game;
  protected Layout matchLayout;

  protected Match () {

  }

//  public Match (EngineApi engineApi) {
//
//    game = engineApi.game;
//  }
//
  public Match (Layout matchLayout, int playToScore) {

    this.matchLayout = new Layout(matchLayout);
    this.matchLayout.setUseBlackBot(Trainer.blackBot);
    this.matchLayout.setUseWhiteBot(Trainer.whiteBot);
    this.matchLayout.setPlayerID(2);
    score = new Score(playToScore);
  }

  public Score getScore () {

    return score;
  }

  public boolean matchOver () {

    return score.matchOver();
  }

  public Game getGame () {

    return game;
  }

  public Turn getLatestTurn () {

    return game != null ? game.getLatestTurn() : null;
  }

  public Match getMatch() {

    game = new Game(matchLayout);
    while (!score.matchOver()) {
      if (!score.isCrawfordGame()) {
        game.computerHandlesCube();
      }
      if (game.gameOver()) {
        score.writeScore(game);
        game = new Game(matchLayout);
      } else {
        game.nextTurn();
      }
    }
    return this;
  }

}
