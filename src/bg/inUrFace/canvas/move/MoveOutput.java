package bg.inUrFace.canvas.move;

import bg.Main;
import bg.inUrFace.canvas.BonusPainter;
import bg.inUrFace.mouse.MoveInput;
import bg.engine.moves.Layout;
import bg.engine.moves.EvaluatedMove;
import bg.engine.moves.MoveLayout;

import java.util.List;

import static bg.Main.*;
import static bg.util.ThreadUtil.threadSleep;

public class MoveOutput extends MoveLayout {

  EvaluatedMove evaluatedMove;

  public MoveOutput () {

  }

  public MoveOutput (EvaluatedMove evaluatedMove) {

    super(evaluatedMove);
    this.evaluatedMove = evaluatedMove;
  }

//  public void showMovePoints (final int startPoint, final int endPoint, final Object notifier) {
//
//    new Thread(() -> {
//
//      int errorCorrectedEndPoint = endPoint < movePoints2.length ? endPoint : movePoints2.length-1;
//      int[] tempPoint = point.clone();
//
//      point = parentLayout.point.clone();
//      for (int a = 0; a <= errorCorrectedEndPoint; a++) {
//        if (movePoints2[a] >= 0) {
//          if (hitPoints[a] >= 0) {
//            if ((a+2)%2 == 0) {
//              point[hitPoints[a]]--;
//            } else {
//              point[hitPoints[a]]++;
//            }
//          }
//          if ((a+2)%2 == 0) {
////              point[movePoints2[a] == 0 && playerID == 1 ? 25 : movePoints2[a]]--;
//            point[movePoints2[a]]--;
//          } else {
////              point[movePoints2[a] == 25 && playerID == 1 ? 0 : movePoints2[a]]++;
//            point[movePoints2[a]]++;
//          }
//          if (a >= startPoint) {
//            displayLayout();
//            Main.sound.playSoundEffect("Blop-Mark_DiAngelo");
//            threadSleep(settings.getShowMoveDelay());
//          }
//        }
//      }
//      point = tempPoint.clone();
//      synchronized (notifier) {
//
//        notifier.notifyAll();
//      }
//    }).start();
//  }

  public void showMove (final int startPoint, final int endPoint, final Object notifier) {

    new Thread(() -> {

      List<Layout> movePointLayouts = getMovePointLayouts();
      int errorCorrectedEndPoint = endPoint < movePointLayouts.size() ? endPoint : movePointLayouts.size()-1;

      for (int a = startPoint; a <= errorCorrectedEndPoint; a++) {
//        for (int a = startPoint; a <= errorCorrectedEndPoint; a++) {
        displayLayout(movePointLayouts.get(a));
        Main.sound.playSoundEffect("Blop-Mark_DiAngelo");
        threadSleep(settings.getShowMoveDelay());
      }
      synchronized (notifier) {
        notifier.notifyAll();
      }
    }).start();
  }

  public void showMove () {

    showMove (0);
  }

  public void showMove (int startPoint) {

    showMove (startPoint,getMovePoints2().length);
  }

  public void showMove (int startPoint, int endPoint) {

    showMove (startPoint, endPoint, new Object());
  }

  public void displayLayout() {

    win.canvas.
      setDisplayedLayout(
        getPlayerID() == 0 ?
          new Layout(this) :
          new Layout(this).getFlippedLayout()
      );
  }

  public void displayLayout(Layout layout) {

    win.canvas.
      setDisplayedLayout(
        getPlayerID() == 0 ?
          new Layout(layout) :
          new Layout(layout).getFlippedLayout()
      );
  }

  public void writeMove () {

    BonusPainter text = getTextArea();

    text.clear();
    text.writeLine("Moves matchLayout information:");
    text.writeLine("Player: " + getPlayerTitle());
    text.writeLine("Turn: #" + (matchApi.getSelectedTurn().getTurnNr() + 1));
    text.writeLine("Dice cast: ");
    for (int a = 0; a < getDice().length; a++) {
      text.write(getDice()[a] + ",");
    }
    text.writeLine("");
    text.write("Legal move:  #" + (matchApi.getSelectedMoveNr()+1) + "/" + matchApi.getSelectedTurn().getNrOfMoves() + ":  ");
    for (int a = 0; a < getMovePoints().length; a++) {
//      win.bonusPainter.write(Integer.toString(playerID == 0 ? partMovePoints[a] : 25 - partMovePoints[a]) + ", ");
      text.write(Integer.toString(getMovePoints()[a]) + ", ");
    }
    text.writeLine("");

/*
    if (moveLayout.i.isWinningMove()) {
      win.text.writeLine(playerTitle[playerID] + " player is the winner");
    }
*/
  }

  public void outputCustomMove (MoveInput moveInput) {

    int turnNr = matchApi.getSelectedTurn().getTurnNr();
    String playerTitle = matchApi.getSelectedTurn().getPlayerTitle();
    int[] rolledDice = matchApi.getSelectedTurn().getDice();
    int[] movePoints = moveInput.getMovePoints();
    Layout customLayout = moveInput.getCustomMoveLayout();
    BonusPainter text = getTextArea();

    win.canvas.setDisplayedLayout(new Layout(
      moveInput.getPlayerID() == 0 ? customLayout : customLayout)
    );
    text.clear();
    text.writeLine("Custom move input: ");
    text.writeLine("Player: " + playerTitle);
    text.writeLine("Turn: #" + (turnNr + 1));
    text.writeLine("Dice rolled: ");
    for (int a = 0; a < rolledDice.length; a++) {
      text.write(rolledDice[a] + ",");
    }
    text.writeLine("");
    text.write("Moves: ");
    for (int a = 0; a < movePoints.length; a++) {
      text.write(movePoints[a] + ",");
    }
  }

  public void writeMoveBonuses() {

    final String[] bonusHeaders = new String[] {

      "Listing both players bonuses:",
      "Listing moving players bonuses:",
      "Listing opponents bonuses:",
      "Press T to toggle bonus views",
    };
    BonusPainter text = getTextArea();

    evaluatedMove.initBonusValues();

    List<String> bonusTexts = evaluatedMove.getBonusTexts();
    List<Integer> bonusValues = evaluatedMove.getBonusValues();
    List<Integer> foeValues = evaluatedMove.getFoeValues();

    text.writeLine("");
    text.writeLine(bonusHeaders[settings.getBonusDisplayMode()]);
    for (int a = 0; a < bonusTexts.size()-1; a++) {
      if (settings.getBonusDisplayMode() < 3) {
        text.writeLine(bonusTexts.get(a)+": ");
        if (settings.getBonusDisplayMode() == 2) {
          text.write(foeValues.get(a) >= 0 ? Integer.toString(foeValues.get(a)) : "N/A");
        } else if (settings.getBonusDisplayMode() == 1) {
          text.write(Integer.toString(bonusValues.get(a)));
        } else {
          text.write(bonusValues.get(a)+" / "+(foeValues.get(a) >= 0 ? foeValues.get(a) : "N/A"));
        }
      }
    }
//    text.setMenuFromLine(bonusTexts.get(0));
  }

  public void outputMove() {

    displayLayout();
    writeMove();
    writeMoveBonuses();
  }

}
