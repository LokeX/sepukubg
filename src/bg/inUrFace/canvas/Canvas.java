package bg.inUrFace.canvas;

import bg.engine.core.moves.Layout;
import javax.swing.*;
import java.awt.*;

import static bg.Main.sepuku;

public class Canvas extends JPanel {

  private Layout displayedLayout = sepuku.getScenarios().getSelectedScenariosLayout();
  private BoardDim dimensions = new BoardDim();
  private PaintJobs paintJobs = new PaintJobs();

  public Canvas() {

    setPreferredSize(
      new Dimension(
        sepuku.getSettings().getCanvasWidth(),
        sepuku.getSettings().getCanvasHeight()
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
