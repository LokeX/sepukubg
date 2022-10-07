package inUrFace.canvas.painters;

import inUrFace.canvas.painters.move.ChequerPainter;
import inUrFace.canvas.painters.move.EndingPointsPainter;
import inUrFace.canvas.painters.move.MovePainter;

import static util.Reflection.getFieldsList;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Painters {

  public MovePainter movePainter = new MovePainter();
  public BoardPainter boardPainter = new BoardPainter();
  public EndingPointsPainter endingPointsPainter = new EndingPointsPainter();
  public ScenarioEditPainter scenarioEditPainter = new ScenarioEditPainter();
  public MatchScorePainter matchScorePainter = new MatchScorePainter();
  public TextPanelPainter textPanelPainter = new TextPanelPainter();
  public PlayButtonPainter playButton = new PlayButtonPainter();
  public DicePainter dicePainter = new DicePainter();
  public CubePainter cubePainter = new CubePainter();
  public LayoutPainter layoutPainter = new LayoutPainter();
  public ChequerPainter chequerPainter = new ChequerPainter();

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
