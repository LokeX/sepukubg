package bg.inUrFace.mouse;

import bg.Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static bg.Main.getActionButton;

public class MoveInputController implements MouseListener {

  public MoveInput moveInput;
  private boolean acceptMoveInput = false;

  @Override
  public void mouseClicked (MouseEvent e) {

//    System.out.println("Move input controller accepts input: "+acceptMoveInput);
    if (acceptsMoveInput()) {
//      System.out.println("Move input accepted");
      if (e.getButton() == MouseEvent.BUTTON3) {
        getActionButton().setHideActionButton(true);
        moveInput.undoPointSelection();
      } else {
//        System.out.println("Point clicked");
        moveInput.pointClicked();
      }
    }
  }

  public void setAcceptMoveInput(boolean ready) {

    acceptMoveInput = ready;
  }

  public boolean acceptsMoveInput() {

    return acceptMoveInput; //&& !api.isNavigating();
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
