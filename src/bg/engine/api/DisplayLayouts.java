package bg.engine.api;

import bg.engine.api.gameState.navigation.moveOutput.OutputLayouts;
import bg.engine.match.moves.Layout;

public class DisplayLayouts {

  private EngineApi engineApi;
  private OutputLayouts outputLayouts;

  DisplayLayouts (EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  public Layout getNextLayout () {

    if (engineApi.gameIsPlaying()) {
      this.outputLayouts =
        engineApi
        .getMatchState()
        .getGameState()
        .getOutputLayouts();
    } else {
      return null;
    }
    return
      outputLayouts.getNextLayout();
  }

  private boolean outputAvailable () {

    return
      engineApi.matchIsPlaying()
      && engineApi.gameIsPlaying()
      && outputLayouts.hasOutput();
  }

}
