package inUrFace.canvas.painters;

import inUrFace.canvas.BoardDim;
import util.TextBatch;

import java.awt.*;
import java.util.List;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.win;

public class TextPanelPainter extends TextBatch implements Paintable {

  public TextPanelPainter() {

    setHeaderColor(Color.yellow);
    setTextColor(new Color(27,124,34));
    setBackgroundColor(new Color(30,30,30));
    setMarkerBackgroundColor(Color.red);
    setMarkerTextColor(Color.white);
    setActAsMenu(true);
    setActAsMarkup(true);
    setMenuFromLine(2);
  }
  
  private boolean displaySearch () {
    
    return
      sepukuPlay.getSettings().isSearchReportOn()
      && sepukuPlay.getSearch().okToSearch()
      && !sepukuPlay.getSearch().isSearching();
  }

  @Override
  public void paint(Graphics g) {

    BoardDim d = win.canvas.getDimensions();
    List<String> bonuses = sepukuPlay.getMatchPlay().getMoveBonuses();
    Font font = new Font("Ariel", Font.BOLD, (int) (10 * (d.factor * 1.0)));
    int offsetX = (int) ((d.boardOffsetX + d.boardWidth) * 1.03);
    int width = win.canvas.getWidth() - offsetX;
    int height = win.canvas.getHeight();

    if (bonuses != null && !displaySearch()) {
      clear();
      bonuses.forEach(this::writeLine);
    } else if (displaySearch()) {
      clear();
      sepukuPlay
        .getSearch()
        .getSearchReport()
        .forEach(this::writeLine);
    }
    setX(offsetX);
    setWidth(width);
    setY(1);
    setHeight(height);
    setFont(font);
    setLineHeight(g);
    drawBatch(g);
  }

  public void write (String s) {

    appendText(s);
  }

  public void writeLine(String s) {

    addText(s);
  }

  public void clear () {

    clearText();
  }

}