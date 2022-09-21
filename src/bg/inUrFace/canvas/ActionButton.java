package bg.inUrFace.canvas;

import bg.Main;
import bg.util.TextBatch;

import java.awt.*;
import java.awt.event.MouseEvent;

import static bg.Main.*;

public class ActionButton extends TextBatch implements Paintable {

  private Color readyBackgroundColor = new Color(255, 0, 255, 75);
  private Color notReadyBackgroundColor = new Color(108,13,13);

  public ActionButton() {

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

  public boolean showWaitBackground () {

    return
      engineApi.getActionState().isSearching();
  }

  private boolean buttonClicked (MouseEvent e) {

    return
      e.getButton() == MouseEvent.BUTTON1
        && !showWaitBackground()
        && mouseOnBatch()
        && showButton();
  }

  public void paint (Graphics g) {

    if (showButton()) {

      BoardDim d = win.canvas.getDimensions();

      if (showWaitBackground()) {
        setButtonText("Please wait");
        setBackgroundColor(notReadyBackgroundColor);
      } else {
        setBackgroundColor(readyBackgroundColor);
        setButtonText(engineApi.getActionState().nextPlayTitle());
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

  private void execButtonClick () {

    Main.sound.playSoundEffect("Blop-Mark_DiAngelo");
    bg.util.ThreadUtil.threadSleep(100);
    engineApi.getMatchPlay().actionButtonClicked();
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (buttonClicked(e)) {
      new Thread(this::execButtonClick).start();
    }
  }

  public String getButtonText () {

    return getFirstLine();
  }

  public void setButtonText(String s) {

    setText(s);
  }

  public boolean showButton() {

    return
      engineApi.getActionState().nextPlayReady()
      || showWaitBackground();
  }

}
