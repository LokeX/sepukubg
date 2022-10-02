package inUrFace.canvas.listeners;

import sepuku.App;
import inUrFace.canvas.BoardDim;
import inUrFace.canvas.painters.BonusPainter;
import inUrFace.canvas.painters.CubePainter;
import inUrFace.canvas.painters.PlayButtonPainter;
import inUrFace.canvas.painters.ScenarioEditPainter;
import util.Batch;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import static sepuku.App.getCanvas;
import static sepuku.App.win;
import static util.Reflection.getFieldsList;

public class MouseListeners extends MouseAdapter {

  public MoveInputListener moveInputListener = new MoveInputListener();
  public PlayButtonPainter playButtonPainter = getCanvas().getPaintJobs().actionButton;
  public ScenarioEditPainter scenarioEditPainter = getCanvas().getPaintJobs().scenarioEditPainter;
  public CubePainter cubePainter = getCanvas().getPaintJobs().cubePainter;
  public BonusPainter bonusPainter = getCanvas().getPaintJobs().bonusPainter;
  private List<MouseListener> listeners = new ArrayList<>();

  public MouseListeners() {

    getCanvas().addMouseListener(this);
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
        points[count++].setComponent(App.win.canvas);
      }
    }
    return points;
  }

}
