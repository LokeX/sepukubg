package bg.inUrFace.canvas;

import bg.inUrFace.canvas.move.ChequerPainter;
import bg.inUrFace.canvas.move.EndingPointsPainter;

import static bg.util.Reflection.getFieldsList;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class PaintJobs {

  public BoardPainter boardPainter = new BoardPainter();
  public LayoutPainter layoutPainter = new LayoutPainter();
  public ScenarioEditor scenarioEditor = new ScenarioEditor();
  public MatchScorePainter matchScorePainter = new MatchScorePainter();
  public BonusPainter bonusPainter = new BonusPainter();
  public ActionButton actionButton = new ActionButton();
  public DicePainter dicePainter = new DicePainter();
  public DoublingCube doublingCube = new DoublingCube();
  public EndingPointsPainter endingPointsPainter = new EndingPointsPainter();
  public ChequerPainter chequerPainter = new ChequerPainter();

  private List<Paintable> paintJobs = new ArrayList();

  public PaintJobs () {

    setupPaintJobs();
  }

  private void setupPaintJobs () {

    getFieldsList(this, paintJobs);
  }

  public final void addJob (Paintable job) {

    synchronized (this) {
      paintJobs.add(job);
    }
  }

  public void removeJob (Paintable job) {

    synchronized (this) {
      paintJobs.remove(job);
    }
  }

  public void paint (Graphics g) {

    synchronized (this) {
      paintJobs.forEach(job -> job.paint(g));
    }
  }

}
