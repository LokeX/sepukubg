package inUrFace.canvas.painters;

import inUrFace.canvas.painters.move.Chequer;
import inUrFace.canvas.painters.move.EndingPoints;
import inUrFace.canvas.painters.move.Move;

import static util.Reflection.getFieldsList;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Painters {

  public Move move = new Move();
  public CanvasBoard canvasBoard = new CanvasBoard();
  public ScenarioEdit scenarioEdit = new ScenarioEdit();
  public CanvasMatchScore canvasMatchScore = new CanvasMatchScore();
  public TextPanel textPanel = new TextPanel();
  public CanvasPlayButton canvasPlayButton = new CanvasPlayButton();
  public CanvasDice canvasDice = new CanvasDice();
  public CanvasCube canvasCube = new CanvasCube();
  public CanvasLayout canvasLayout = new CanvasLayout();
  public EndingPoints endingPoints = new EndingPoints();
  public Chequer chequer = new Chequer();

  private List<Paintable> painters = new ArrayList<>();

  public Painters() {

    setupPaintJobs();
  }

  private void setupPaintJobs () {

    getFieldsList(this, painters);
  }

  public void paint (Graphics g) {

    painters.forEach(painter -> painter.paint(g));
  }

}
