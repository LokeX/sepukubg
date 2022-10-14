package inUrFace.canvas;

import engine.core.moves.Layout;
import inUrFace.canvas.painters.Painters;

import javax.swing.*;
import java.awt.*;

import static sepuku.WinApp.sepukuPlay;

public class Canvas extends JPanel {

  private Layout displayedLayout = sepukuPlay.scenarios().getSelectedScenariosLayout();
  private Layout oldDisplayedLayout = null;
  private BoardDim dimensions = new BoardDim();
  private Painters painters = new Painters();
  private int mouseX;
  private int mouseY;

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
  
  private void saveDisplay () {
    
    oldDisplayedLayout = new Layout(displayedLayout);
    oldDisplayedLayout.generateHashCode();
  }
  
  private boolean displayHasChanged () {
    
    displayedLayout.generateHashCode();
    return
      !displayedLayout.isIdenticalTo(oldDisplayedLayout);
  }
  
  private boolean paintChequer () {
    
    return
      sepukuPlay
      .humanMove()
      .endingPointIsNext();
  }
  
  private boolean mouseMoved () {
    
    int newMouseX = (int) getMousePosition().getX();
    int newMouseY = (int) getMousePosition().getY();
    boolean mouseMoved = mouseX != newMouseX || mouseY != newMouseY;
    
    mouseX = newMouseX;
    mouseY = newMouseY;
    return
      mouseMoved;
  }
  
  private boolean updateCanvas () {
    
    if (oldDisplayedLayout == null) {
      saveDisplay();
    }
    return paintChequer()
      || displayHasChanged()
      || sepukuPlay.moveOutput().hasOutput()
      || (getMousePosition() != null && mouseMoved());
  }
  
  public void repaintCanvas () {
    
    if (updateCanvas()) {
      saveDisplay();
      repaint();
    }
  }

  public BoardDim getDimensions() {

    return dimensions;
  }

  public void setDisplayedLayout(Layout layOutToDisplay) {

    displayedLayout = layOutToDisplay;
    this.repaint();
  }

  public Layout getDisplayedLayout() {

    return displayedLayout;
  }

  public Painters getPaintJobs () {

    return painters;
  }

}