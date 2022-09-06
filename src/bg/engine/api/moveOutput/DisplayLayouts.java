package bg.engine.api.moveOutput;

import bg.engine.api.EngineApi;
import bg.engine.api.moveOutput.MoveOutputLayouts;
import bg.engine.coreLogic.moves.Layout;

public class DisplayLayouts {

  private EngineApi engineApi;
  private Layout outputLayout;
  private boolean endOfOutput;

  DisplayLayouts (EngineApi engineApi) {

    this.engineApi = engineApi;
  }

  private MoveOutputLayouts moveOutputLayouts () {

    return
      engineApi.gameIsPlaying()
        ? engineApi
            .getMatchState()
            .getGameState()
            .getMoveOutputLayouts()
        : null;
  }

  private void moveOutputLayout () {

    if (moveOutputLayouts() != null) {
      outputLayout = moveOutputLayouts().getNextLayout();
      endOfOutput = false;
    } else {
      endOfOutput = true;
      outputLayout = null;
    }
  }

}
