package bg.engine.play.humanMove;

import bg.engine.api.Settings;
import bg.engine.play.MoveOutput;
import bg.engine.play.GameState;
import bg.engine.play.MatchPlay;

import java.util.stream.Stream;

import static bg.util.ThreadUtil.runWhenNotified;

public class HumanMove {

  private MatchPlay matchPlay;
  private MoveSelection moveSelection;

  public HumanMove (MatchPlay matchPlay) {

    this.matchPlay = matchPlay;
  }
  
  public int getPlayerID() {
    
    return
      humanInputActive()
        ? moveSelection.getPlayerID()
        : -1;
  }
  
  public boolean humanInputActive() {
    
    return
      matchPlay.gameIsPlaying()
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

    gameState()
      .selectedTurn()
      .setPlayedMoveNr(getSelectedMoveNr());
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

    if (hasSelectedMove()) {
      setSelectedMove();
      setPlayedMoveToSelectedMove();
      setMovePoints();
      endMove();
      System.out.println("Played selected move: "+ matchPlay.getGameState().getMoveNr());
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
      matchPlay.getGameState();
  }

  private MoveOutput moveOutput () {

    return
      matchPlay
        .getMoveOutput();
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
  }

  public void startMove () {

    startMoveSelection();
    moveSelection.printMovePoints();
    if (moveSelection.noLegalMove()) {
      moveSelection = null;
    } else if (autoSelectPartMoves()){
      autoSelectPoints();
      outputMoveLayouts();
    }
  }

  public void endMove () {

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
