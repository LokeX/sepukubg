package inUrFace.canvas.painters.move;

import sepuku.WinApp;
import inUrFace.canvas.painters.Paintable;

import java.awt.*;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.win;

public class MovePainter implements Paintable {
    
  private boolean hasMoveOutput () {
  
    return
      sepukuPlay != null
        && sepukuPlay.getMoveOutput() != null
        && sepukuPlay.getMoveOutput().hasOutput();
  }
  
  public void paint (Graphics g) {
    
    if (hasMoveOutput()) {
      win.canvas.setDisplayedLayout(
        sepukuPlay
          .getMoveOutput()
          .getMovePointLayout()
      );
      WinApp.sound.playSoundEffect(
        "Blop-Mark_DiAngelo"
      );
    }
  }
  
}