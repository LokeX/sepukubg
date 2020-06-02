package bg.engine.api.navigation.moveOutput;

import bg.engine.api.navigation.Navigation;
import bg.engine.api.navigation.moveInput.MoveSelector;
import bg.engine.coreLogic.moves.MoveLayout;

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

  private List<MoveLayout> human () {

    return
      moveSelector().getMovePointLayouts();
  }

  private List<MoveLayout> computer () {

    return
      new ArrayList<>(
        navigation
          .selectedMove()
          .getMovePointLayouts()
      );
  }

}
