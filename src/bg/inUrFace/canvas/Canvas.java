package bg.inUrFace.canvas;

import bg.engine.coreLogic.moves.Layout;
import javax.swing.*;
import java.awt.*;

import static bg.Main.engineApi;

public class Canvas extends JPanel {

  private Layout displayedLayout = engineApi.getScenarios().getSelectedScenariosLayout();
  private BoardDim dimensions = new BoardDim();
  private PaintJobs paintJobs = new PaintJobs();

  public Canvas() {

    setPreferredSize(
      new Dimension(
        engineApi.getSettings().getCanvasWidth(),
        engineApi.getSettings().getCanvasHeight()
      )
    );
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    paintJobs.paint(g);
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
