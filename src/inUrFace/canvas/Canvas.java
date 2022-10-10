package inUrFace.canvas;

import engine.core.moves.Layout;
import inUrFace.canvas.painters.Painters;

import javax.swing.*;
import java.awt.*;

import static sepuku.WinApp.sepukuPlay;

public class Canvas extends JPanel {

  private Layout displayedLayout = sepukuPlay.scenarios().getSelectedScenariosLayout();
  private BoardDim dimensions = new BoardDim();
  private Painters painters = new Painters();

  public Canvas() {

    setPreferredSize(
      new Dimension(
        sepukuPlay.settings().getCanvasWidth(),
        sepukuPlay.settings().getCanvasHeight()
      )
    );
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    painters.paint(g);
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

  public Painters getPaintJobs () {

    return painters;
  }

}