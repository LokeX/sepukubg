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
    
//    System.out.println("hasMoveOutput: "+hasMoveOutput());
//    System.out.println("hasOutput: "+engineApi.getMoveOutput().hasOutput());
    
    if (hasMoveOutput()) {
      System.out.println("Got moveLayout");
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
