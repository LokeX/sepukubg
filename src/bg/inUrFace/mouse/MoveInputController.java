package bg.inUrFace.mouse;

import bg.inUrFace.canvas.BoardDim;
import bg.util.Batch;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static bg.Main.getActionButton;

public class MoveInputController implements MouseListener {

  public MoveInput moveInput;
  private boolean acceptMoveInput = false;

  @Override
  public void mouseClicked (MouseEvent e) {

    if (acceptsMoveInput()) {
      if (e.getButton() == MouseEvent.BUTTON3) {
        getActionButton().setHideActionButton(true);
        moveInput.undoPointSelection();
      } else {
        moveInput.pointClicked();
      }
    }
  }

  public void setAcceptMoveInput(boolean ready) {

    acceptMoveInput = ready;
  }

  public boolean acceptsMoveInput() {

    return acceptMoveInput;
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

}
