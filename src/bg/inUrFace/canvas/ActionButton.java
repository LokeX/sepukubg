package bg.inUrFace.canvas;

import bg.Main;
import bg.util.TextBatch;

import java.awt.*;
import java.awt.event.MouseEvent;

import static bg.Main.*;

public class ActionButton extends TextBatch implements Paintable {

  private boolean init = true;
  private boolean hideActionButton = false;
  private boolean showPleaseWaitButton = false;
  private Color backgroundColor = new Color(255, 0, 255, 75);

  public ActionButton() {

    super(470, 250, 125, 65);
  }

  private void setupButton () {

    setComponent(win.canvas);
    setMargins(25, 25, 7, 5);
    setBackgroundColor(backgroundColor);
    setFrameColor(new Color(255, 255, 255, 15));
    setDrawFrame(true);
    setShadow(true);
    setRaised(true);
    setTextColor(new Color(200, 200, 200, 200));
    setFont(new Font("Ariel", Font.BOLD, 18));
  }

  private boolean okToSetText() {

    return
      engineApi != null
        && engineApi.gameIsPlaying()
        && engineApi.getLatestTurn() != null;
  }

  private boolean selectedTurnIsLatestTurn () {

    return
      engineApi.getSelectedTurnNr() == engineApi.getLatestTurnNr();
  }

  private boolean selectedTurnsPlayerIsHuman () {

    return
      engineApi.humanTurnSelected();
  }

  private boolean playedMoveIsSelected () {

    return
      engineApi
        .getSelectedTurn()
        .getPlayedMoveNr() == engineApi.getSelectedMoveNr();
  }

  private boolean buttonClicked (MouseEvent e) {

    return
      e.getButton() == MouseEvent.BUTTON1
        && !showPleaseWaitButton
        && mouseOnBatch()
        && showButton();
  }

  public void paint(Graphics g) {

    if (showButton()) {

      if (init) {
        setupButton();
        init = false;
      }

      BoardDim d = win.canvas.getDimensions();

      if (showPleaseWaitButton) {
        setButtonText("Please wait");
        setBackgroundColor(new Color(108,13,13));
      } else if (okToSetText()) {
        setBackgroundColor(backgroundColor);
        setButtonText(
          engineApi.matchOver() && selectedTurnIsLatestTurn() ? "New Match" :
            engineApi.gameOver() && selectedTurnIsLatestTurn() ? "New game" :
              selectedTurnsPlayerIsHuman() ? "Play move" : "Roll dice"
        );
      }
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

    showPleaseWaitButton = true;
    mouse.getMoveInputListener().setAcceptMoveInput(false);
    Main.sound.playSoundEffect("Blop-Mark_DiAngelo");
    bg.util.ThreadUtil.threadSleep(100);
    engineApi.getMatchState().actionButtonClicked();
  }

  @Override
  public void mouseClicked (MouseEvent e) {

    if (buttonClicked(e)) {
      new Thread(this::execButtonClick).start();
    }
  }

  public boolean showPleaseWaitButton () {

    return showPleaseWaitButton;
  }

  public String getButtonText () {

    return getFirstLine();
  }

  public void setButtonText(String s) {

    setText(s);
  }

  public void setShowPleaseWaitButton(boolean show) {

    showPleaseWaitButton = show;
  }

  public void setHideActionButton(boolean hide) {

    hideActionButton = hide;
  }

  public boolean buttonIsHidden () {

    return hideActionButton;
  }

  public boolean showButton() {

    boolean showButton = false;

    if (okToSetText()) {
      if (selectedTurnIsLatestTurn()) {
        showButton = !hideActionButton;
      } else if (!playedMoveIsSelected()) {
        showButton = true;
      } else {
        showButton = false;
      }
    }
    return showButton || !engineApi.gameIsPlaying() ||
      engineApi.gameOver() && selectedTurnIsLatestTurn();
  }

}
