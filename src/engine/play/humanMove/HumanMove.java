package engine.play.humanMove;

import engine.api.Settings;
import engine.play.gamePlay.GamePlay;
import engine.play.MatchPlay;

import java.util.stream.Stream;

import static util.ThreadUtil.runWhenNotified;

public class HumanMove {

  private MatchPlay matchPlay;
  private MoveSelection moveSelection;

  public HumanMove (MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
  }
  
  public int getPlayerID() {
    
    return
      inputActive()
        ? moveSelection.getPlayerID()
        : -1;
  }
  
  public boolean inputActive() {
    
    return
      matchPlay.gameIsPlaying()
      && inputReady();
  }
  
  public boolean endingPointIsNext() {
    
    return
      inputActive()
        && !moveSelection.endOfInput()
        && moveSelection.positionIsEndingPoint();
  }
  
  public Stream<Integer> getEndingPoints() {
    
    return
      inputActive()
        ? moveSelection.validEndingPoints()
        : null;
  }
  
  public void setPlayedMoveToSelectedMove() {

    if (getSelectedMoveNr() != -1) {
      gameState()
        .selectedTurn()
        .setPlayedMoveNr(getSelectedMoveNr());
    }
  }

  private void setMovePoints () {

    gameState()
      .selectedTurn()
      .getMoveByNr(getSelectedMoveNr())
      .setMovePoints(moveSelection.getMovePoints());
  }

  private void setSelectedMove () {

    gameState().setMoveNr(getSelectedMoveNr());
  }

  public void playMove () {

    if (hasSelectedMove() && getSelectedMoveNr() != -1) {
      setSelectedMove();
      setPlayedMoveToSelectedMove();
      setMovePoints();
      endMove();
    }
  }

  private boolean hasSelectedMove () {

    return
      moveSelection != null
      && moveSelection.isUniqueMove();
  }

  public int getSelectedMoveNr () {

    return
      hasSelectedMove()
      ? moveSelection
        .getUniqueEvaluatedMoveNr()
      : -1;
  }

  public MoveSelection getMoveSelection() {

    return moveSelection;
  }
  
  private Settings settings () {
    
    return
      matchPlay.settings();
  }

  private boolean autoSelectPartMoves() {

    return
      settings().isAutoCompletePartMoves();
  }

  private GamePlay gameState () {

    return
      matchPlay.gamePlay();
  }

  private void startMoveSelection () {

    moveSelection = new MoveSelection(
      gameState().selectedTurn()
    );
    matchPlay
    .getMoveOutput()
    .setOutputLayout(
      moveSelection
      .getParentMoveLayout()
    );
    matchPlay
    .navigation()
    .setIsNavigating(false);
  }

  public void startMove () {

    startMoveSelection();
    if (autoSelectPartMoves()) {
      autoSelectPoints();
      outputMoveLayouts();
    }
  }
  
  public boolean playerIsHuman () {
    
    return
      matchPlay.playerIsHuman();
  }

  public void endMove () {

    matchPlay
      .navigation()
      .setIsNavigating(true);
    moveSelection = null;
  }

  public boolean inputReady () {

    return
      moveSelection != null
      && !matchPlay
          .getMoveOutput()
          .hasOutput();
  }

  private void outputMoveLayouts () {

    if (moveSelection.endOfInput()) {
      matchPlay
        .getMoveOutput()
        .setEndOfOutputNotifier(
          runWhenNotified(matchPlay::endTurn)
        );
    }
    matchPlay
      .getMoveOutput()
      .setOutputLayouts(
        moveSelection.getMovePointLayouts()
      );
  }

  private void autoSelectPoints() {

    moveSelection.autoSelectPoints();
  }

  public void pointClicked (int clickedPoint) {

    if (inputReady()) {
      moveSelection.inputPoint(clickedPoint);
      if (clickedPoint != -1 && autoSelectPartMoves()) {
        autoSelectPoints();
      }
      outputMoveLayouts();
    }
  }

}
