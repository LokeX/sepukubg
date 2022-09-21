package bg.inUrFace.canvas.move;

import bg.Main;
import bg.inUrFace.canvas.Paintable;

import java.awt.*;

import static bg.Main.engineApi;
import static bg.Main.win;

public class MovePainter implements Paintable {
    
  private boolean hasMoveOutput () {
  
    return
      engineApi != null
        && engineApi.getMoveOutput() != null
        && engineApi.getMoveOutput().hasOutput();
  }
  
  public void paint (Graphics g) {
    
    if (hasMoveOutput()) {
      win.canvas.setDisplayedLayout(
        engineApi
          .getMoveOutput()
          .getMovePointLayout()
      );
      Main.sound.playSoundEffect(
        "Blop-Mark_DiAngelo"
      );
    }
  }
  
}
