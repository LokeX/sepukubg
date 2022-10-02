package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class Batch implements MouseListener {

  protected List<MouseListener> mouseListeners = new ArrayList();
  protected List<MouseMotionListener> mouseMotionListeners = new ArrayList();

  protected int x = 0;
  protected int y = 0;
  protected int w = 0;
  protected int h = 0;

  protected boolean shadow = false;
  protected boolean raised = true;
  protected boolean centerOnComponent = false;
  protected boolean drawFrame = false;

  protected Color backgroundColor = Color.BLACK;
  protected Color frameColor = Color.WHITE;

  protected Component component;
  protected int layer = Layers.getActiveLayer();

  public Batch () {

    super();
  }

  public Batch (Component c) {

    component = c;
    centerOnComponent = true;
  }

  public Batch (int x, int y) {

    this.x = x;
    this.y = y;
  }

  public Batch (int x, int y, int w, int h) {

    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public Batch (int x, int y, int w) {

    this.x = x;
    this.y = y;
    this.w = w;
  }

  public void setX (int x) {

    this.x = x;
  }

  public void setY (int y) {

    this.y = y;
  }

  public void setWidth (int w) {

    this.w = w;
  }

  public void setHeight (int h) {

    this.h = h;
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (layer == Layers.getActiveLayer()) {
      e.setSource(this);
      for (int a = 0; a < mouseListeners.size(); a++) {
        if (mouseOnBatch(e)) {
          mouseListeners.get(a).mouseClicked(e);
        }
      }
    }
  }

  @Override
  public void mousePressed (MouseEvent e) {

    if (layer == Layers.getActiveLayer()) {
      e.setSource(this);
      for (int a = 0; a < mouseListeners.size(); a++) {
        if (mouseOnBatch(e)) {
          mouseListeners.get(a).mousePressed(e);
        }
      }
    }
  }

  public void setDrawFrame (boolean df) {

    drawFrame = df;
  }

  public boolean mouseOnBatch (int mx, int my) {

    return layer == Layers.getActiveLayer() && (mx >= x && mx <= x + w && my >= y && my <= y + h);
  }

  public void addMouseMotionListener (MouseMotionListener mm) {

    mouseMotionListeners.add(mm);
  }

  public void mouseDragged (MouseEvent e) {

    if (layer == Layers.getActiveLayer()) {
      e.setSource(this);
      for (int a = 0; a < mouseMotionListeners.size(); a++) {
        if (mouseOnBatch(e)) {
          mouseMotionListeners.get(a).mouseDragged(e);
        }
      }
    }
  }

  public int getHeight () {

    return h;
  }

  public int getLayer () {

    return layer;
  }

  public void setShadow (boolean sh) {

    shadow = sh;
  }

  public void setLayer (int l) {

    layer = l;
  }

  public boolean mouseOnBatch (Component c) {

    Point p = c.getMousePosition();

    if (p != null) {

      int mx = (int) p.getX();
      int my = (int) p.getY();

      return mouseOnBatch(mx, my);
    } else {
      return false;
    }
  }

  public boolean mouseOnBatch () {

    if (component == null) {
      return false;
    } else {
      return mouseOnBatch(component);
    }
  }

  public boolean mouseOnBatch (MouseEvent e) {

    return layer == Layers.getActiveLayer() && (e.getX() >= x && e.getX() <= x + w && e.getY() >= y && e.getY() <= y + h);
  }

  public void setCenterOnComponent (boolean b) {

    if (component != null || !b) {
      centerOnComponent = b;
    }
  }

  public int getY () {

    return y;
  }

  public void setComponent (Component c) {

    component = c;
  }

  public void drawBatch (Graphics g) {

    int xb = x;
    int yb = y;

    if (centerOnComponent) {
      x = (component.getWidth() - w) / 2;
      y = (component.getHeight() - h) / 2;
    }

    if (shadow) {
      g.setColor(new Color(50, 50, 50, 150));
      g.fillRect(x + w, y + 5, 5, h - 5);
      g.fillRect(x + 5, y + h, w, 5);
    }
    g.setColor(backgroundColor);
    g.fill3DRect(x, y, w, h, raised);
    if (drawFrame) {
      g.setColor(frameColor);
      g.drawRect(x, y, w, h);
    }
    if (centerOnComponent) {
      x = xb;
      y = yb;
    }
  }

  @Override
  public void mouseEntered (MouseEvent e) {

    if (layer == Layers.getActiveLayer()) {
      e.setSource(this);
      for (int a = 0; a < mouseListeners.size(); a++) {
        if (mouseOnBatch(e)) {
          mouseListeners.get(a).mouseEntered(e);
        }
      }
    }
  }

  @Override
  public void mouseExited (MouseEvent e) {

    if (layer == Layers.getActiveLayer()) {
      e.setSource(this);
      for (int a = 0; a < mouseListeners.size(); a++) {
        if (mouseOnBatch(e)) {
          mouseListeners.get(a).mouseExited(e);
        }
      }
    }
  }

  public void addMouseListener (MouseListener m) {

    mouseListeners.add(m);
  }

  public void setBackgroundColor (Color color) {

    backgroundColor = color;
  }

  public void mouseMoved (MouseEvent e) {

    if (layer == Layers.getActiveLayer()) {
      e.setSource(this);
      for (int a = 0; a < mouseMotionListeners.size(); a++) {
	if (mouseOnBatch(e)) {
	  mouseMotionListeners.get(a).mouseMoved(e);
	}
      }
    }
  }

  public int getWidth () {

    return w;
  }

  public void removeMouseListeners () {

    mouseListeners.removeAll(mouseListeners);
  }

  public void removeMouseListener (MouseListener ml) {

    for (int a = 0; a < mouseListeners.size(); a++) {
      if (ml == mouseListeners.get(a)) {
        mouseListeners.remove(a);
      }
    }
  }

  public void setFrameColor (Color color) {

    frameColor = color;
  }

  public int getX () {

    return x;
  }

  public void removeMouseMotionListeners () {

    mouseMotionListeners.removeAll(mouseMotionListeners);
  }

  public void setRaised (boolean r) {

    raised = r;
  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }
}
