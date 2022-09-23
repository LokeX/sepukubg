package bg.inUrFace.canvas;

import bg.util.TextBatch;

import java.awt.*;
import java.util.List;

import static bg.Main.getTextArea;
import static bg.Main.sepuku;
import static bg.Main.win;

public class BonusPainter extends TextBatch implements Paintable {

  public BonusPainter() {

    setHeaderColor(Color.yellow);
    setTextColor(new Color(27,124,34));
    setBackgroundColor(new Color(30,30,30));
    setMarkerBackgroundColor(Color.red);
    setMarkerTextColor(Color.white);
    setActAsMenu(true);
    setActAsMarkup(true);
    setMenuFromLine(2);
  }

  @Override
  public void paint(Graphics g) {

    if (getTextArea() != null) {

      BonusPainter text = getTextArea();
      BoardDim d = win.canvas.getDimensions();
      List<String> bonuses = sepuku.getMatchPlay().getMoveBonuses();
      Font font = new Font("Ariel", Font.BOLD, (int) (10 * (d.factor * 1.0)));
      int offsetX = (int) ((d.boardOffsetX + d.boardWidth) * 1.03);
      int width = win.canvas.getWidth() - offsetX;
      int height = win.canvas.getHeight();

      if (bonuses != null) {
        text.clear();
        bonuses.forEach(this::writeLine);
      }
      text.setX(offsetX);
      text.setWidth(width);
      text.setY(1);
      text.setHeight(height);
      text.setFont(font);
      text.setLineHeight(g);
      text.drawBatch(g);
    }
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
