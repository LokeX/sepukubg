package bg.engine.api.gameState.navigation.moveOutput;

import bg.engine.api.gameState.navigation.Navigation;
import bg.engine.match.moves.Layout;

import java.util.ArrayList;
import java.util.List;

public class MoveOutput {

  private Navigation navigation;
  private OutputLayouts outputLayouts;

  public MoveOutput (Navigation navigation) {

    this.navigation = navigation;
  }

  public OutputLayouts getOutputLayouts () {

    if (isNewMove()) {
      outputLayouts = newOutputLayouts();
    } else if (isHumanMove()) {
      outputLayouts = outputLayouts.getOutputLayouts(human());
    }
    return outputLayouts;
  }

  private OutputLayouts newOutputLayouts () {

    return
      isHumanMove()
        ? new OutputLayouts(human())
        : new OutputLayouts(computer());
  }

  private boolean isNewMove () {

    return outputLayouts == null;
  }

  public void reset () {

    outputLayouts = null;
  }

  private boolean isHumanMove () {

    return
      navigation
        .getHumanMove()
        .inputReady();
  }

  private List<Layout> human () {

    return
      navigation
        .getHumanMove()
        .getMoveLayouts();
  }

  private List<Layout> computer () {

    return new ArrayList<>(
      navigation
        .selectedMove()
        .getMoveLayoutsNew()
    );
  }

}
