package bg.inUrFace.canvas;

import static bg.Main.getCanvas;
import static bg.Main.mouse;
import static bg.Main.win;

import bg.Main;
import bg.engine.coreLogic.moves.Layout;
import bg.util.Batch;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScenarioEditor extends MouseAdapter implements Paintable {

  private Layout layout;
  private Batch[] clickPoints = new Batch[28];
  private int player = 0;
  private boolean showEditor = false;

  @Override
  public void paint(Graphics g) {

    if (mouse != null && mouse.scenarioEditor != null && showEditor()) {
      for (Batch clickPoint : clickPoints) {
        if (clickPoint != null) {
          clickPoint.drawBatch(g);
        }
      }
    }
  }

  public boolean showEditor () {

    return showEditor;
  }

  public void setShowEditor (boolean show) {

    showEditor = show;
  }

  private int getClickedPoint() {

    for (int a = 0; a < clickPoints.length; a++) {
      if (clickPoints[a] != null) {
        if (clickPoints[a].mouseOnBatch()) {
          return a;
        }
      }
    }
    return -1;
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (bg.Main.win.canvas.getPaintJobs().scenarioEditor.showEditor()) {

      int clickedPoint = getClickedPoint();
      int button;

      layout = Main.win.canvas.getDisplayedLayout();
      if (clickedPoint >= 0) {
        if (clickedPoint == 0 || clickedPoint < 25 && layout.point[clickedPoint] > 0 && layout.point[clickedPoint + 26] == 0) {
          clickPoints[0].setBackgroundColor(Color.yellow);
          clickPoints[27].setBackgroundColor(Color.black);
          player = 0;
        } else if (clickedPoint == 27 || clickedPoint < 25 && layout.point[clickedPoint + 26] > 0 && layout.point[clickedPoint] == 0) {
          clickPoints[0].setBackgroundColor(Color.black);
          clickPoints[27].setBackgroundColor(Color.red);
          player = 26;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
          button = -1;
        } else {
          button = 1;
        }
        if (clickedPoint == 25) {
          if (button < 0 && layout.point[25] > 0 || button > 0 && layout.point[0] > 0) {
            layout.point[25] += button;
            layout.point[0] += button * -1;
          }
        } else if (clickedPoint == 26) {
          if (button < 0 && layout.point[26] > 0 || button > 0 && layout.point[51] > 0) {
            layout.point[26] += button;
            layout.point[51] += button * -1;
          }
        } else if (clickedPoint != 27 && player > 0 && layout.point[clickedPoint] == 0 || player == 0 && layout.point[clickedPoint + 26] == 0) {
          if (button < 0 && layout.point[clickedPoint + player] > 0 || button > 0 && layout.point[player > 0 ? 51 : 0] > 0) {
            layout.point[clickedPoint + player] += button;
            if (player > 0) {
              layout.point[51] += button * -1;
            } else {
              layout.point[0] += button * -1;
            }
          }
        }
      }
    }
  }

  public void endEdit() {

    getCanvas().getPaintJobs().scenarioEditor.setShowEditor(false);
  }

  public void startEditor() {

    generateClickPoints();
    layout = getCanvas().getDisplayedLayout();
    getCanvas().getPaintJobs().scenarioEditor.setShowEditor(true);
  }

  final public void generateClickPoints () {

    BoardDim d = win.canvas.getDimensions();
    Batch[] regularPoints = mouse.getRegularClickPoints();
    final Color backgroundColor = new Color(0, 0, 0, 0);
    final Color frameColor = new Color(0, 0, 0, 15);

    clickPoints[0] = new Batch(
      d.bottomLeftBearOffOffsetX,
      d.boardOffsetY+d.boardHeight+5,
      d.chequerSize,
      d.chequerSize
    );
    clickPoints[0].setComponent(Main.win.canvas);
    clickPoints[0].setDrawFrame(true);
    clickPoints[0].setFrameColor(frameColor);
    clickPoints[0].setBackgroundColor(player == 0 ? Color.yellow : Color.black);
    for (int a = 0; a < regularPoints.length; a++) {
      clickPoints[a+1] = regularPoints[a];
      clickPoints[a+1].setFrameColor(frameColor);
      clickPoints[a+1].setBackgroundColor(backgroundColor);
      clickPoints[a+1].setDrawFrame(true);
    }
    clickPoints[25] = new Batch(
      d.leftPlayAreaOffsetX+d.leftPlayAreaWidth,
      d.frameOffsetY,
      d.barWidth,
      d.boardInnerHeight/2
    );
    clickPoints[26] = new Batch(
      d.leftPlayAreaOffsetX+d.leftPlayAreaWidth,
      d.frameOffsetY+(d.boardInnerHeight/2),
      d.barWidth,
      d.boardInnerHeight/2
    );
    clickPoints[27] = new Batch(
      d.bottomLeftBearOffOffsetX,
      d.boardOffsetY-d.chequerSize-5,
      d.chequerSize,
      d.chequerSize
    );
    for (int a = 25; a < 28; a++) {
      clickPoints[a].setComponent(Main.win.canvas);
      clickPoints[a].setDrawFrame(true);
      clickPoints[a].setFrameColor(frameColor);
      clickPoints[a].setBackgroundColor(
        a == 27 ? (player == 26 ? Color.red : Color.black) : backgroundColor
      );
    }
  }

  public Batch[] getClickPoints () {

    return clickPoints;
  }

  public void setLayout(Layout layout) {

    this.layout = layout;
  }

  public Layout getLayout() {

    return layout;
  }

}
