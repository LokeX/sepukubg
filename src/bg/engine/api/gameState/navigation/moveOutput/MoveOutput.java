package bg.engine.api.gameState.navigation.moveOutput;

import bg.engine.api.gameState.navigation.Navigation;
import bg.engine.api.gameState.navigation.humanMove.MoveSelector;
import bg.engine.match.moves.Layout;

import java.util.ArrayList;
import java.util.List;

public class MoveOutput {

  private Navigation navigation;
  private MoveOutputLayouts moveOutputLayouts;

  public MoveOutput (Navigation navigation) {

    this.navigation = navigation;
  }

  public MoveOutputLayouts getMoveOutputLayouts() {

    if (isNewMove()) {
      moveOutputLayouts = newMoveLayouts();
    } else if (isHumanMove() && hasHumanOutput()) {
      moveOutputLayouts.setMoveLayouts(human());
    }
    return moveOutputLayouts;
  }

  private MoveOutputLayouts newMoveLayouts () {

    return
      isHumanMove()
        ? new MoveOutputLayouts(human())
        : new MoveOutputLayouts(computer());
  }

  private boolean isNewMove () {

    return moveOutputLayouts == null;
  }

  public void newMove () {

    moveOutputLayouts = null;
  }

  private boolean isHumanMove () {

    return
      navigation
        .getHumanMove()
        .inputReady();
  }

  private MoveSelector moveSelector () {

    return
      navigation
        .getHumanMove()
        .getMoveSelector();
  }

  private boolean hasHumanOutput() {

    return
      moveSelector().hasMoveLayouts();
  }

  private List<Layout> human () {

    return
      moveSelector().getMoveLayouts();
  }

  private List<Layout> computer () {

    return
      new ArrayList<>(
        navigation
          .selectedMove()
          .getMoveLayouts()
      );
  }

}
