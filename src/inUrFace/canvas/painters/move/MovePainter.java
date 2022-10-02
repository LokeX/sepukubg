package inUrFace.canvas.painters.move;

import sepuku.App;
import inUrFace.canvas.painters.Paintable;

import java.awt.*;

import static sepuku.App.playSepuku;
import static sepuku.App.win;

public class MovePainter implements Paintable {
    
  private boolean hasMoveOutput () {
  
    return
      playSepuku != null
        && playSepuku.getMoveOutput() != null
        && playSepuku.getMoveOutput().hasOutput();
  }
  
  public void paint (Graphics g) {
    
    if (hasMoveOutput()) {
      win.canvas.setDisplayedLayout(
        playSepuku
          .getMoveOutput()
          .getMovePointLayout()
      );
      App.sound.playSoundEffect(
        "Blop-Mark_DiAngelo"
      );
    }
  }
  
}
