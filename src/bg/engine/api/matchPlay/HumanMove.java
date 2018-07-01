package bg.engine.api.matchPlay;

import bg.engine.api.HumanMoveApi;
import bg.engine.api.gamePlay.GameState;
import bg.engine.match.moves.MoveInput;
import bg.engine.match.moves.Layout;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Stream;

import static bg.Main.engineApi;
import static bg.Main.getSettings;
import static bg.util.ThreadUtil.runWhenNotified;

public class HumanMove implements HumanMoveApi {

  private MoveInput moveInput;
  private GameState gameState;
  private int storedMoveNr;
  private int storedTurnNr;

  public int getPlayerID () {

    return moveInput.getPlayerID();
  }

  public void pointClicked (MouseEvent mouseEvent, int clickedPoint) {

    if (inputReady()) {
      if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
        moveInput.deleteLatestInput();
      } else {
        moveInput.input(clickedPoint);
      }
      if (moveInput.inputIsLegal()) {
        displayLayouts(
          moveInput.getOutputLayouts()
        );
      }
    }
  }

  public boolean isEndingPoint () {

    return
      moveInput
        .positionIsEndingPoint();
  }

  public Stream<Integer> getEndingPoints () {

    return
      moveInput
        .validEndingPoints();
  }

  int getStoredMoveNr() {

    return moveInput.getUniqueEvaluatedMoveNr();
  }

  private boolean isAutoCompletable (MoveInput moveInput) {

    return
      moveInput.getNrOfMoves() == 1
      && (getSettings().isAutoCompleteMoves()
      || getSettings().isAutoCompletePartMoves());
  }

  private MoveInput moveInput () {

    return
      gameState != null
        ? gameState.selectedTurn().getMoveInput()
        : null;
  }

  void setMoveInput (GameState gameState) {

    moveInput = moveInput();
    if (moveInput != null) {
      storedMoveNr = gameState.getMoveNr();
      storedTurnNr = gameState.getTurnNr();
      this.gameState = gameState;
      if (moveInput.isIllegal() || isAutoCompletable(moveInput)) {
        displayLayouts(
          moveInput.isIllegal()
            ? List.of(moveInput.getParentMoveLayout())
            : List.of(moveInput.getBestMove())
        );
        moveInput = null;
      } else {
        if (getSettings().isAutoCompletePartMoves()) {
          initialSelection();
        }
      }
    }
  }

  private void displayLayouts (List<Layout> layouts) {

    engineApi.displayLayouts(layouts, notifier());
  }

  private void endDisplayLayouts () {

    engineApi.matchState.endTurn();
  }

  private Object notifier () {

    return
      runWhenNotified(
        this::endDisplayLayouts
      );
  }

  private void initialSelection () {

    displayLayouts(
      getAutoSelectedLayouts()
    );
  }

  private List<Layout> getAutoSelectedLayouts() {

    moveInput.initialSelection();
    return
      moveInput.getOutputLayouts();
  }

  boolean endOfInput () {

    return
      moveInput == null
      || moveInput.endOfInput();
  }

  private boolean inputIsVoid () {

    return
      gameState == null
      || gameState.getTurnNr() != storedTurnNr
      || gameState.getMoveNr() != storedMoveNr;
  }

  public boolean inputReady() {

    if (inputIsVoid()) {
      moveInput = null;
    }
    return
      moveInput != null;
  }

  public String getMovePointsString () {

    return
      moveInput
        .getMatchingMoveLayout()
        .getMovePointsString();
  }

}