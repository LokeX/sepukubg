package bg.engine.api.moveOutput;

import bg.engine.api.gamePlay.GameState;
import bg.engine.api.moveInput.MoveSelection;
import bg.engine.coreLogic.moves.MoveLayout;

import java.util.ArrayList;
import java.util.List;

public class MoveOutput {

  private GameState gameState;
  private MoveOutputLayouts moveOutputLayouts;

  public MoveOutput (GameState gameState) {

    this.gameState = gameState;
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
      gameState
        .getHumanMove()
        .inputReady();
  }

  private MoveSelection moveSelector () {

    return
      gameState
        .getHumanMove()
        .getMoveSelection();
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
        gameState
          .selectedMove()
          .getMovePointLayouts()
      );
  }

}
