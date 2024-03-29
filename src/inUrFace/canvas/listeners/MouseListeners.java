package inUrFace.canvas.listeners;

import inUrFace.canvas.Canvas;
import sepuku.WinApp;
import inUrFace.canvas.BoardDim;
import inUrFace.canvas.painters.TextPanel;
import inUrFace.canvas.painters.CanvasCube;
import inUrFace.canvas.painters.CanvasPlayButton;
import inUrFace.canvas.painters.ScenarioEdit;
import util.Batch;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import static sepuku.WinApp.win;
import static util.Reflection.getFieldsList;

public class MouseListeners extends MouseAdapter {

  public MoveInputListener moveInputListener;
  public CanvasPlayButton canvasPlayButton;
  public ScenarioEdit scenarioEdit;
  public CanvasCube canvasCube;
  public TextPanel textPanel;
//  public MoveInputListener moveInputListener = new MoveInputListener();
//  public PlayButton playButton = getCanvas().getPaintJobs().playButton;
//  public ScenarioEdit scenarioEdit = getCanvas().getPaintJobs().scenarioEdit;
//  public Cube cube = getCanvas().getPaintJobs().cube;
//  public TextPanel textPanel = getCanvas().getPaintJobs().textPanel;
  private List<MouseListener> listeners = new ArrayList<>();
  private Canvas canvas;

  public MouseListeners(Canvas canvas) {

    this.canvas = canvas;
    moveInputListener = new MoveInputListener();
    canvasPlayButton = canvas.getPaintJobs().canvasPlayButton;
    scenarioEdit = canvas.getPaintJobs().scenarioEdit;
    canvasCube = canvas.getPaintJobs().canvasCube;
    textPanel = canvas.getPaintJobs().textPanel;
    canvas.addMouseListener(this);
    setupControllersList();
  }

  public MoveInputListener getMoveInputListener() {

    return moveInputListener;
  }

  private void setupControllersList () {

    getFieldsList(this, listeners);
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    listeners.forEach(listener -> listener.mouseClicked(e));
    canvas.repaint();
  }
  
  public Batch[] getRegularClickPoints () {

    Batch[] points = new Batch[24];
    BoardDim d = win.canvas.getDimensions();
    int offSet = d.rightPlayAreaOffsetX+d.rightPlayAreaWidth;
    int sign = -1;
    int count = 0;

    for (int y = 0; y < 2; y++) {
      for (int x = 1; x < 13; x++) {
        if (count == 6) {
          offSet -= (int)(d.barWidth*1.1);
        } else if (count == 18) {
          offSet += (int)(d.barWidth*1.1);
        } else if (count == 12) {
          sign = 1;
          offSet = (int)(d.leftPlayAreaOffsetX*1.075);
        }
        points[count] =
          new Batch(offSet + ((x-y) * d.chequerTotalSpace * sign),
            (y == 0 ? (d.frameOffsetY+d.boardInnerHeight)-(d.chequerSize*5) : d.frameOffsetY),
            d.chequerSize, d.chequerSize*5);
        points[count++].setComponent(WinApp.win.canvas);
      }
    }
    return points;
  }

}