package bg.inUrFace.canvas.move;

import bg.Main;
import bg.inUrFace.canvas.Paintable;

import java.awt.*;

import static bg.Main.sepuku;
import static bg.Main.win;

public class MovePainter implements Paintable {
    
  private boolean hasMoveOutput () {
  
    return
      sepuku != null
        && sepuku.getMoveOutput() != null
        && sepuku.getMoveOutput().hasOutput();
  }
  
  public void paint (Graphics g) {
    
    if (hasMoveOutput()) {
      win.canvas.setDisplayedLayout(
        sepuku
          .getMoveOutput()
          .getMovePointLayout()
      );
      Main.sound.playSoundEffect(
        "Blop-Mark_DiAngelo"
      );
    }
  }
  
}
