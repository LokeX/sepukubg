package bg.engine.api.gameState.navigation;

import bg.engine.api.gameState.navigation.moveOutput.MoveOutput;
import bg.engine.api.gameState.navigation.moveOutput.OutputLayouts;
import bg.engine.api.gameState.navigation.humanMove.HumanMove;
import bg.engine.match.Game;
import bg.engine.match.Turn;
import bg.engine.match.moves.EvaluatedMove;
import bg.engine.match.moves.Layout;

import static bg.Main.getActionButton;
import static bg.Main.settings;

public class Navigation extends Game {

  private HumanMove humanMove;
  private MoveOutput moveOutput;
  protected int selectedTurnNr = 0;
  protected int selectedMoveNr = 0;

  public Navigation (Layout matchLayout) {

    super(matchLayout);
    humanMove = new HumanMove(this);
    moveOutput = new MoveOutput(this);
  }

  public OutputLayouts getOutputLayouts () {

    return moveOutput.getOutputLayouts();
  }

  public void startComputerMove () {

    moveOutput.newMove();
  }

  public int getTurnNr () {

    return selectedTurnNr;
  }

  public int getMoveNr () {

    return selectedMoveNr;
  }

  public void setMoveNr (int moveNr) {

    selectedMoveNr = moveNr;
  }

  public HumanMove getHumanMove () {

    return humanMove;
  }

  public void startHumanMove () {

    humanMove.startMoveSelection();
  }

  public void endHumanMove () {

    humanMove.endMoveSelection();
  }

  public Turn selectedTurn () {

    return getTurnByNr(selectedTurnNr);
  }

  protected int playedMoveNr () {

    return selectedTurn().getPlayedMoveNr();
  }

  protected boolean isHumanTurn (Turn turn) {

    return
      settings
        .playerIsHuman(
          turn.getPlayerOnRollsID()
        );
  }

  public EvaluatedMove selectedMove () {

    return selectedTurn().getMoveByNr(selectedMoveNr);
  }

  public Turn selectNextTurn () {

    getActionButton().setHideActionButton(false);
    if (++selectedTurnNr == nrOfTurns()) {
      return getTurnByNr(--selectedTurnNr);
    } else {
      selectedMoveNr = playedMoveNr();
      return getTurnByNr((selectedTurnNr));
    }
  }

  public Turn selectPreviousTurn () {

    getActionButton().setHideActionButton(false);
    if (--selectedTurnNr == -1) {
      return getTurnByNr(++selectedTurnNr);
    } else {
      selectedMoveNr = playedMoveNr();
      return getTurnByNr((selectedTurnNr));
    }
  }

  public Turn selectNextHumanTurn() {

    int turnCount = selectedTurnNr;

    getActionButton().setHideActionButton(false);
    while (++turnCount < nrOfTurns()) {
      if (isHumanTurn(getTurnByNr(turnCount))) {
        selectedTurnNr = turnCount;
        return getTurnByNr(selectedTurnNr);
      }
    }
    return getTurnByNr(selectedTurnNr);
  }

  public Turn selectPreviousHumanTurn() {

    int turnCount = selectedTurnNr;

    getActionButton().setHideActionButton(false);
    while (--turnCount >= 0) {
      if (isHumanTurn(getTurnByNr(turnCount))) {
        selectedTurnNr = turnCount;
        return getTurnByNr(selectedTurnNr);
      }
    }
    return getTurnByNr(selectedTurnNr);
  }

  public Turn selectLatestHumanTurn() {

    getActionButton().setHideActionButton(false);
    if (nrOfTurns() > 1) {
      if (isHumanTurn(lastTurn())) {
        selectedTurnNr = lastTurnNr();
        return lastTurn();
      } else if (isHumanTurn(getTurnByNr(nrOfTurns()-2))) {
        selectedTurnNr = nrOfTurns()-2;
        return selectedTurn();
      }
    }
    return selectedTurn();
  }

  public EvaluatedMove selectNextMove () {

    getActionButton().setHideActionButton(false);
    if (++selectedMoveNr < selectedTurn().getNrOfMoves()) {
      return selectedMove();
    } else {
      selectedMoveNr = 0;
      return selectedMove();
    }
  }

  public EvaluatedMove selectPreviousMove () {

    getActionButton().setHideActionButton(false);
    if (--selectedMoveNr >= 0) {
      return selectedMove();
    } else {
      selectedMoveNr = selectedTurn().getNrOfMoves()-1;
      return selectedMove();
    }
  }

}
