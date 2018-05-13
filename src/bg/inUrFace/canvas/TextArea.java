package bg.inUrFace.canvas;

import bg.util.TextBatch;

import java.awt.*;

import static bg.Main.getTextArea;
import static bg.Main.win;

public class TextArea extends TextBatch implements Paintable {

  public TextArea() {

    setHeaderColor(Color.yellow);
    setTextColor(new Color(27,124,34));
    setBackgroundColor(new Color(30,30,30));
    setMarkerBackgroundColor(Color.red);
    setMarkerTextColor(Color.white);
    setActAsMenu(true);
    setActAsMarkup(true);
    setMenuFromLine(7);
  }

  @Override
  public void paint(Graphics g) {

    if (getTextArea() != null) {

      TextArea text = getTextArea();
      BoardDim d = win.canvas.getDimensions();
      int offsetX = (int) ((d.boardOffsetX + d.boardWidth) * 1.03);
      int width = win.canvas.getWidth() - offsetX;
      int height = win.canvas.getHeight();

      text.setX(offsetX);
      text.setWidth(width);
      text.setY(1);
      text.setHeight(height);
      text.setFont(new Font("Ariel", Font.BOLD, (int) (10 * (d.factor * 0.9))));
      text.setLineHeight(g);
      text.drawBatch(g);
    }
  }

  public void write (String s) {

    appendText(s);
  }

  public void nlWrite (String s) {

    addText(s);
  }

  public void clear () {

    clearText();
  }

}
