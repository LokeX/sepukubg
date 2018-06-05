package bg.inUrFace.mouse;

import bg.Main;

import static bg.Main.getCanvas;
import static bg.Main.win;

import bg.inUrFace.canvas.*;
import bg.inUrFace.canvas.BonusPainter;
import bg.util.Batch;
import static bg.util.Reflection.getFieldsList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class MouseController extends MouseAdapter {

  public ScenarioEditor scenarioEditor = getCanvas().getPaintJobs().scenarioEditor;
  public ActionButton actionButton = getCanvas().getPaintJobs().actionButton;
  public DoublingCube doublingCube = getCanvas().getPaintJobs().doublingCube;
  public MoveInputController moveInputController = new MoveInputController();
  public BonusPainter bonusPainter = getCanvas().getPaintJobs().bonusPainter;
  private List<MouseListener> controllers = new ArrayList();

  public MouseController() {

    getCanvas().addMouseListener(this);
    setupControllersList();
  }

  public MoveInputController getMoveInputController () {

    return moveInputController;
  }

  public ScenarioEditor getLayoutEditor() {

    return scenarioEditor;
  }

  public BonusPainter getBonusPainter() {

    return bonusPainter;
  }

  public ActionButton getActionButton() {

    return actionButton;
  }

  public DoublingCube getDoublingCube () {

    return doublingCube;
  }

  public MoveInput getMoveInput () {

    return moveInputController.moveInput;
  }

  public void setMoveInput (MoveInput moveInput) {

    this.moveInputController.moveInput = moveInput;
  }

  public void setAcceptMoveInput (boolean acceptMoveInput) {

    moveInputController.setAcceptMoveInput(acceptMoveInput);
  }

  private void setupControllersList () {

    getFieldsList(this, controllers);
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    synchronized (this) {
      controllers.forEach(control -> control.mouseClicked(e));
    }
  }

  public void addController (MouseAdapter controller) {

    synchronized (this) {
      controllers.add(controller);
    }
  }

  public void removeController (MouseAdapter controller) {

    synchronized (this) {
      controllers.remove(controller);
    }
  }

  static public Batch[] getRegularPoints () {

    Batch[] points = new Batch[24];
    BoardDim d = win.canvas.getDimensions();
    int offSet = d.rightPlayAreaOffsetX+d.rightPlayAreaWidth;
    int sign = -1;
    int count = 0;

    for (int y = 0; y < 2; y++) {
      for (int x = 1; x < 13; x++) {
        if (count == 6) {
          offSet -= (int)(d.barWidth*1.1);
        } else if (count == 18) {
          offSet += (int)(d.barWidth*1.1);
        } else if (count == 12) {
          sign = 1;
          offSet = (int)(d.leftPlayAreaOffsetX*1.075);
        }
        points[count] =
          new Batch(offSet + ((x-y) * d.chequerTotalSpace * sign),
            (y == 0 ? (d.frameOffsetY+d.boardInnerHeight)-(d.chequerSize*5) : d.frameOffsetY),
            d.chequerSize, d.chequerSize*5);
        points[count++].setComponent(Main.win.canvas);
      }
    }
    return points;
  }

}
