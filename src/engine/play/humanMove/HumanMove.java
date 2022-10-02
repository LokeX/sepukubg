package engine.play.humanMove;

import engine.api.Settings;
import engine.play.MoveOutput;
import engine.play.GameState;
import engine.play.PlayMatch;

import java.util.stream.Stream;

import static util.ThreadUtil.runWhenNotified;

public class HumanMove {

  private PlayMatch playMatch;
  private MoveSelection moveSelection;

  public HumanMove (PlayMatch playMatch) {

    this.playMatch = playMatch;
  }
  
  public int getPlayerID() {
    
    return
      humanInputActive()
        ? moveSelection.getPlayerID()
        : -1;
  }
  
  public boolean humanInputActive() {
    
    return
      playMatch.gameIsPlaying()
      && inputReady();
  }
  
  public boolean endingPointIsNext() {
    
    return
      humanInputActive()
        && !moveSelection.endOfInput()
        && moveSelection.positionIsEndingPoint();
  }
  
  public Stream<Integer> getEndingPoints() {
    
    return
      humanInputActive()
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
      playMatch.settings();
  }

  private boolean autoCompleteMove () {

    return
      moveSelection.getNrOfMoves() == 1
      && settings().isAutoCompleteMoves();
  }

  private boolean autoSelectPartMoves() {

    return
      settings().isAutoCompletePartMoves();
  }

  private GameState gameState () {

    return
      playMatch.getGameState();
  }

  private MoveOutput moveOutput () {

    return
      playMatch
        .getMoveOutput();
  }

  private void startMoveSelection () {

    moveSelection = new MoveSelection(
      gameState().selectedTurn()
    );
    playMatch
      .getMoveOutput()
      .setOutputLayout(
        moveSelection
          .getParentMoveLayout()
      );
  }

  public void startMove () {

    if (playMatch.getSelectedMove().isIllegal()) {
      endMove();
      playMatch.endTurn();
    } else {
      startMoveSelection();
      if (autoSelectPartMoves()) {
        autoSelectPoints();
        outputMoveLayouts();
      }
    }
  }

  public void endMove () {

    moveSelection = null;
  }

  public boolean inputReady () {

    return
      moveSelection != null
      && !playMatch
          .getMoveOutput()
          .hasOutput();
  }

  private void outputMoveLayouts () {

    if (moveSelection.endOfInput()) {
      playMatch
        .getMoveOutput()
        .setEndOfOutputNotifier(
          runWhenNotified(playMatch::endTurn)
        );
    }
    playMatch
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
