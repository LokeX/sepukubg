package bg.engine.api.navigation;

import bg.engine.api.navigation.moveOutput.MoveOutput;
import bg.engine.api.navigation.moveOutput.MoveOutputLayouts;
import bg.engine.api.navigation.moveInput.HumanMove;
import bg.engine.coreLogic.Game;
import bg.engine.coreLogic.Turn;
import bg.engine.coreLogic.moves.EvaluatedMove;
import bg.engine.coreLogic.moves.Layout;

import static bg.Main.getActionButton;
import static bg.Main.settings;

public class Navigation extends Game {

  private HumanMove humanMove;
  private MoveOutput moveOutput;
  protected int selectedTurnNr = 0;
  protected int selectedMoveNr = 0;
  protected int selectedPartMoveNr = -1;

  public Navigation (Layout matchLayout) {

    super(matchLayout);
    humanMove = new HumanMove(this);
    moveOutput = new MoveOutput(this);
  }

  public MoveOutputLayouts getMoveOutputLayouts() {

    return moveOutput.getMoveOutputLayouts();
  }

  public void startComputerMove () {

    endHumanMove();
    moveOutput.newMove();
    System.out.println("Computer move started");
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

    System.out.println("Human move started");
    humanMove.startMoveSelection();
  }

  public void endHumanMove () {

    System.out.println("Human move ended");
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
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
    if (++selectedTurnNr == nrOfTurns()) {
      return getTurnByNr(--selectedTurnNr);
    } else {
      selectedMoveNr = playedMoveNr();
      return getTurnByNr((selectedTurnNr));
    }
  }

  public Turn selectPreviousTurn () {

    getActionButton().setHideActionButton(false);
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
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
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
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
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
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
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
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
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
    if (++selectedMoveNr >= selectedTurn().getNrOfMoves()) {
      selectedMoveNr = 0;
    }
    return selectedMove();
  }

  public EvaluatedMove selectPreviousMove () {

    getActionButton().setHideActionButton(false);
    selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
    if (--selectedMoveNr < 0) {
      selectedMoveNr = selectedTurn().getNrOfMoves() - 1;
    }
    return selectedMove();
  }

  public Layout selectNextPartMove () {

    if (selectedPartMoveNr < 0) {
      selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
    }
    System.out.println("selectNextPartMove");
    System.out.println("selectedPartMoveNr = "+selectedPartMoveNr);
    getActionButton().setHideActionButton(false);
    if (++selectedPartMoveNr >= selectedMove().getNrOfPartMoves()*2) {
      selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
    }
    System.out.println("selectedPartMoveNr = "+selectedPartMoveNr);
    return selectedMove().getMovePointLayouts().get(selectedPartMoveNr);
  }

  public Layout selectPreviousPartMove () {

    if (selectedPartMoveNr < 0) {
      selectedPartMoveNr = (selectedMove().getNrOfPartMoves()*2)-1;
    }
    System.out.println("selectPreviousPartMove");
    System.out.println("selectedPartMoveNr = "+selectedPartMoveNr);
    getActionButton().setHideActionButton(false);
    if (--selectedPartMoveNr < 0) {
      selectedPartMoveNr = 0;
    }
    System.out.println("selectedPartMoveNr = "+selectedPartMoveNr);
    return selectedMove().getMovePointLayouts().get(selectedPartMoveNr);
  }

}
