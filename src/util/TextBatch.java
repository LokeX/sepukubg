package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TextBatch extends Batch {

  class Markup {

    private int lineNr;

    public Markup (int lineNr) {

      this.lineNr = lineNr;
    }

    public int getLineNr () {

      return lineNr;
    }

  }

  class Line {

    public Line (String s) {

      this.s = s;
    }

    String s = "";


    public String getLine () {

      return s;
    }

    public void setLine (String s) {

      this.s = s;
    }

    public void append (String s) {

      this.s += s;
    }
  }

  public final static int LEFT = 0;
  public final static int RIGHT = 1;
  public final static int CENTER = 2;

  private List<Line> lines = new ArrayList<>();
  private List<Markup> markups = new ArrayList<>();

  private int leftMargin = 10;
  private int rightMargin = 10;
  private int topMargin = 5;
  private int bottomMargin = 5;

  private int lineHeight = 0;
  private int lineWidth = 0;
  private int textPosition = LEFT;
  private int markerPosition = -1;
  private int menuFromLine = 0;

  private Color markerBackground = Color.WHITE;
  private Color markerTextColor = Color.BLACK;

  private boolean actAsMenu = false;
  private boolean actAsMarkup = false;
  private boolean drawingBatch = false;

  private Font font = new Font("Ariel", Font.PLAIN, 12);

  private Color textColor = Color.WHITE;
  private Color headerColor;

  private Font headerFont;

  public TextBatch () {

  }

  public void setMenuFromLine (int line) {

    menuFromLine = line;
  }

  public void setMenuFromLine (String s) {

    for (int a = 0; a < lines.size(); a++) {
      if (lines.get(a).getLine().length() >= s.length() && lines.get(a).getLine().substring(0, s.length()).equals(s)) {
        menuFromLine = a;
        return;
      }
    }
  }

  public String getFirstLine () {

    return lines.size() > 0 ? lines.get(0).getLine() : null;
  }

  public boolean isMarked (int lineNr) {

    for (int a = 0; a < markups.size(); a++) {
      if (markups.get(a).getLineNr() == lineNr) {
        return true;
      }
    }
    return false;
  }

  public void removeMarkup (int lineNr) {

    for (int a = 0; a < markups.size(); a++) {
      if (markups.get(a).getLineNr() == lineNr) {
        markups.remove(a);
      }
    }
  }

  public int getMenuFromLine () {

    return menuFromLine;
  }

  public void setActAsMarkup (boolean markup) {

    actAsMarkup = markup;
  }

  public boolean isActingAsMarkup () {

    return actAsMarkup;
  }

  public void setMarkerTextColor (Color color) {

    markerTextColor = color;
  }

  public void setMarkerBackgroundColor (Color color) {

    markerBackground = color;
  }

  public TextBatch (int x, int y) {

    super(x, y);
  }

  public TextBatch (int x, int y, int w, int h) {

    super(x, y, w, h);
  }

  public TextBatch (int x, int y, int w) {

    super(x, y, w);
  }

  public void resetMarkerPosition () {

    markerPosition = -1;
  }

  public int getNrOfLines () {

    return lines.size();
  }

  public void setHeaderColor (Color c) {

    headerColor = c;
  }

  public void setActAsMenu (boolean set) {

    actAsMenu = set;
  }

  public void setHeaderFont (Font f) {

    headerFont = f;
  }

  public void setAutoWidth (Graphics g) {

    if (lines.size() > 0) {

      int longestLine = 0;

      g.setFont(font);
      for (int a = 0; a < lines.size(); a++) {
        lineWidth = (int) g.getFontMetrics().getStringBounds(lines.get(a).getLine(),g).getWidth();
        longestLine = lineWidth > longestLine ? lineWidth : longestLine;
      }
      w = longestLine + leftMargin + rightMargin;
    }
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    super.mousePressed(e);
    setMarkerPosition(component);
    if (mouseOnBatch(e) && lines.size() > 0 && getMarkerPosition() < lines.size()) {
      if (isMarked(getMarkerPosition())) {
        removeMarkup(getMarkerPosition());
      } else {
        markups.add(new Markup(getMarkerPosition()));
      }
    }
  }

  public void setLineHeight (Graphics g) {

    if (lines.size() > 0) {
      g.setFont(font);
      lineHeight = (int) g.getFontMetrics().getStringBounds(lines.get(0).getLine(), g).getHeight();
    }
  }

  public void setLineHeight (int lh) {

    lineHeight = lh;
  }

  public void setAutoHeight (Graphics g) {

    if (lines.size() > 0) {
      if (lineHeight == 0) {
      	setLineHeight(g);
      }
      h = lineHeight * (lines.size() + 1) + bottomMargin;
    }
  }

  public void setMarkerPosition (Component c) {

    if (c != null) {

      Point p = c.getMousePosition();

      markerPosition = -1;
      if (p != null && lineHeight > 0 && mouseOnBatch(c)) {

        int my = (int) p.getY();

        markerPosition = (my - y - topMargin) / lineHeight;
      }
    }
  }

  public int getMarkerPosition () {

    return markerPosition;
  }

  public void setTextPosition (int al) {

    textPosition = al;
  }

  public void setMargins (int lm, int rm, int tm, int bm) {

    leftMargin = lm;
    rightMargin = rm;
    topMargin = tm;
    bottomMargin = bm;
  }

  public void setFont (Font font) {

    this.font = font;
  }

  public void setTextColor (Color color) {

    textColor = color;
  }

  public void setText (String[] t) {

    lines.clear();
    appendText(t);
  }

  public void appendText (String[] t) {

    for (int a = 0; a < t.length; a++) {
      lines.add(new Line(t[a]));
    }
  }


  @Override
  public void drawBatch (Graphics g) {

    drawingBatch = true;
    g.setFont(font);
    if (w == 0) {
      setAutoWidth(g);
    }
    if (h == 0) {
      setAutoHeight(g);
    }
    if (lineHeight == 0) {
      setLineHeight(g);
    }

    int xb = x;
    int yb = y;

    if (centerOnComponent) {
      x = (component.getWidth() - w) / 2;
      y = (component.getHeight() - h) / 2;
    }
    super.drawBatch(g);

    if (actAsMenu) {
      setMarkerPosition(component);
    }

    int xa = 0;

    if (textPosition == LEFT) {
      xa = x + leftMargin;
    }
    for (int a = 0; a < lines.size(); a++) {
      if (textPosition != LEFT) {
        if (textPosition == CENTER) {
          xa = x + (w - (int) g.getFontMetrics().getStringBounds(lines.get(a).getLine(), g).getWidth()) / 2;
        } else if (textPosition == RIGHT) {
          xa = x + (w - ((int) g.getFontMetrics().getStringBounds(lines.get(a).getLine(), g).getWidth() + rightMargin));
        }
      }
      if (headerColor != null && a == 0) {
	      g.setColor(headerColor);
      } else {
        g.setColor(textColor);
      }
      if (headerFont != null && a == 0) {
      	g.setFont(headerFont);
      }
      if ((actAsMenu && a >= menuFromLine && markerPosition == a) || (actAsMarkup && lines != null && lines.size() > 0 && isMarked(a) && a >= menuFromLine)) {
        g.setColor(markerBackground);
        g.fill3DRect(xa - 2, y + topMargin + 2 + (lineHeight * a), w - rightMargin - 2, lineHeight + 4, raised);
        g.setColor(markerTextColor);
      }
      if (lines != null && lines.size() > 0) {
        g.drawString(lines.get(a).getLine(), xa, y + topMargin + (lineHeight * (a + 1)));
      }
      if ((actAsMenu && markerPosition == a) || (headerColor != null && a == 0 || actAsMarkup && lines != null && lines.size() > 0 && isMarked(a))) {
        g.setColor(textColor);
      }
      if (headerFont != null && a == 0) {
        g.setFont(font);
      }
    }
    if (centerOnComponent) {
      x = xb;
      y = yb;
    }
    drawingBatch = false;
  }

  public void setText (String s) {
/*
    while (drawingBatch) {
      ThreadUtil.threadSleep(5);
    }
*/
    lines.clear();
    lines.add(new Line(s));
  }

  public void clearText () {
/*
    while (drawingBatch) {
      ThreadUtil.threadSleep(5);
    }
*/
    lines.clear();
  }

  public void addText (String s) {

    lines.add(new Line(s));
  }

  public void appendText (String s) {

    lines.get(lines.size()-1).append(s);
  }


}
