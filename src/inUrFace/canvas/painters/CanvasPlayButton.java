package inUrFace.canvas.painters;

import sepuku.WinApp;
import inUrFace.canvas.BoardDim;
import util.TextBatch;
import java.awt.*;
import java.awt.event.MouseEvent;

import static sepuku.WinApp.*;

public class CanvasPlayButton extends TextBatch implements Paintable {

  private Color readyBackgroundColor = new Color(255, 0, 255, 75);
  private Color notReadyBackgroundColor = new Color(108,13,13);

  public CanvasPlayButton() {

    super(470, 250, 125, 65);
    setMargins(25, 25, 7, 5);
    setBackgroundColor(readyBackgroundColor);
    setFrameColor(new Color(255, 255, 255, 15));
    setDrawFrame(true);
    setShadow(true);
    setRaised(true);
    setTextColor(new Color(200, 200, 200, 200));
    setFont(new Font("Ariel", Font.BOLD, 18));
  }

  public boolean isSearching () {

    return
      sepukuPlay.stateOfPlay().isSearching();
  }

  private boolean buttonClicked (MouseEvent e) {

    return
      e.getButton() == MouseEvent.BUTTON1
        && !isSearching()
        && mouseOnBatch()
        && showButton();
  }

  public void paint (Graphics g) {

    if (showButton()) {

      BoardDim d = win.canvas.getDimensions();

      if (isSearching()) {
        setText("Please wait");
        setBackgroundColor(notReadyBackgroundColor);
      } else {
        setBackgroundColor(readyBackgroundColor);
        setText(nextPlayTitle());
      }
      setComponent(win.canvas);
      setMargins(
        (int)(25*d.factor),
        (int)(25*d.factor),
        (int)(7*(d.factor*(d.factor != 1 ? 2 : 1))),
        (int)(5*(d.factor*(d.factor != 1 ? 3 : 1)))
      );
      setFont(new Font("Ariel", Font.BOLD, (int)(18.0*d.factor)));
      setX(d.rightPlayAreaOffsetX+(d.boardInnerWidth/10));
      setY(d.rightPlayAreaOffsetY+(int)(d.boardInnerHeight/2.28));
      setAutoHeight(g);
      setAutoWidth(g);
      drawBatch(g);
    }
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (buttonClicked(e)) {
      execNextPlay();
    }
  }
  
  public void execNextPlay () {
    
    if (!nextPlayTitle().equals("New match")) {
      WinApp.sound.playSoundEffect("wuerfelbecher");
    }
    sepukuPlay.execNextPlay();
  }
  
  private String nextPlayTitle () {
    
    return
      sepukuPlay
        .stateOfPlay()
        .nextPlayTitle();
  }
  
  public boolean showButton() {

    return
      sepukuPlay.stateOfPlay().nextPlayReady()
      || isSearching();
  }

}