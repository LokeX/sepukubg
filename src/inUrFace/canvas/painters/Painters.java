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
  public Board board = new Board();
  public EndingPoints endingPoints = new EndingPoints();
  public ScenarioEdit scenarioEdit = new ScenarioEdit();
  public MatchScore matchScore = new MatchScore();
  public TextPanel textPanel = new TextPanel();
  public PlayButton playButton = new PlayButton();
  public Dice dice = new Dice();
  public Cube cube = new Cube();
  public Layout layout = new Layout();
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
