package bg.inUrFace.mouse;

import bg.engine.moves.MovePointsInput;
import bg.inUrFace.canvas.move.LayoutDisplay;

import static bg.Main.*;

public class MoveInputNew {

  private MovePointsInput movePointsInput = matchApi.getMovePointsInput();
  private LayoutDisplay layoutDisplay = new LayoutDisplay();
//  private GameInformation gameInformation = new GameInformation();

  public MovePointsInput getMovePointsInput() {

    return movePointsInput;
  }

  private void outputMove () {

    layoutDisplay.displayLayout(
      movePointsInput.getMatchingLayout()
    );
//    gameInformation.writeMoveInput(
//      movePointsInput.getMovePointsString()
//    );
  }

  public void undoPointInput () {

    movePointsInput.deleteLatestInput();
    outputMove();
  }

  public void pointClicked (int clickedPoint) {

    movePointsInput.input(clickedPoint);
    outputMove();
  }

  public void initialAutoMove (Object notifier) {

    layoutDisplay.showMovePoints(movePointsInput.autoMove(), notifier);
  }

}
