package bg.inUrFace.canvas;

import bg.engine.api.Displayable;
import bg.engine.coreLogic.moves.Layout;
import bg.Main;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import static bg.Main.engineApi;
import static bg.Main.settings;

public class Canvas extends JPanel implements Displayable {

  private Layout displayedLayout = Main.scenarios.getSelectedScenariosLayout();
  private BoardDim dimensions = new BoardDim();
  private PaintJobs paintJobs = new PaintJobs();

  public Canvas() {

    setPreferredSize(
      new Dimension(
        settings.getCanvasWidth(),
        settings.getCanvasHeight()
      )
    );
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    showMove();
    paintJobs.paint(g);
  }

  private boolean hasMoveOutput () {

    return
      engineApi != null
      && engineApi.getMoveOutput() != null
      && engineApi.getMoveOutput().hasOutput();
  }

  private void showMove () {

    if (hasMoveOutput()) {
      setDisplayedLayout(
        engineApi
          .getMoveOutput()
          .getMovePointLayout()
      );
      Main.sound.playSoundEffect(
        "Blop-Mark_DiAngelo"
      );
    }
  }

  public BoardDim getDimensions() {

    return dimensions;
  }

  public void setDisplayedLayout(Layout layOutToDisplay) {

    displayedLayout = layOutToDisplay;
  }

  public Layout getDisplayedLayout() {

    return displayedLayout;
  }

  public PaintJobs getPaintJobs () {

    return paintJobs;
  }

}
