package engine.play;

import engine.core.Game;
import engine.core.Turn;
import engine.core.moves.EvaluatedMove;
import engine.core.moves.Layout;
import engine.core.moves.MoveLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GameState extends Game {

  private MatchPlay matchPlay;
  private int turnNr = 0;
  private int moveNr = 0;
  private int partMoveNr = -1;

  public GameState (MatchPlay matchPlay) {

    super(matchPlay.getScenario());
    this.matchPlay = matchPlay;
  }

  public boolean playedMoveSelected() {

    return
      moveNr == playedMoveNr();
  }

  public boolean humanTurnSelected () {

    return
      isHumanTurn(
        selectedTurn()
      );
  }

  public boolean lastTurnSelected() {

    return
      turnNr == lastTurnNr();
  }

  public void newTurn () {

    if (nrOfTurns() == 0) {
      nextTurn(0, 0);
    } else {
      selectedTurn()
        .setPlayedMoveNr(
          moveNr
        );
      nextTurn(
        turnNr,
        moveNr
      );
    }
    turnNr = lastTurnNr();
    moveNr = 0;
  }
  public int getTurnNr () {

    return turnNr;
  }

  public int getMoveNr () {

    return moveNr;
  }

  public void setMoveNr (int moveNr) {

    this.moveNr = moveNr;
  }

  public Turn selectedTurn () {

    return
      nrOfTurns() > 0
      ? getTurnByNr(turnNr)
      : null;
  }

  protected int playedMoveNr () {

    return selectedTurn().getPlayedMoveNr();
  }

  protected boolean isHumanTurn (Turn turn) {

    return
      matchPlay.settings()
        .playerIsHuman(
          turn.getPlayerOnRollsID()
        );
  }

  public EvaluatedMove selectedMove () {

    return selectedTurn().getMoveByNr(moveNr);
  }

  public Turn selectNextTurn () {

    partMoveNr = -1;
    if (++turnNr == nrOfTurns()) {
      return getTurnByNr(--turnNr);
    } else {
      moveNr = playedMoveNr();
      return getTurnByNr((turnNr));
    }
  }

  public Turn selectPreviousTurn () {

    partMoveNr = -1;
    if (--turnNr == -1) {
      return getTurnByNr(++turnNr);
    } else {
      moveNr = playedMoveNr();
      return getTurnByNr((turnNr));
    }
  }

  public Turn selectNextHumanTurn() {

    int turnCount = turnNr;

    partMoveNr = -1;
    while (++turnCount < nrOfTurns()) {
      if (isHumanTurn(getTurnByNr(turnCount))) {
        turnNr = turnCount;
        return getTurnByNr(turnNr);
      }
    }
    return getTurnByNr(turnNr);
  }

  public Turn selectPreviousHumanTurn() {

    int turnCount = turnNr;

    partMoveNr = -1;
    while (--turnCount >= 0) {
      if (isHumanTurn(getTurnByNr(turnCount))) {
        turnNr = turnCount;
        return getTurnByNr(turnNr);
      }
    }
    return getTurnByNr(turnNr);
  }

  public Turn selectLatestHumanTurn() {

    partMoveNr = -1;
    if (nrOfTurns() > 1) {
      if (isHumanTurn(lastTurn())) {
        turnNr = lastTurnNr();
        return lastTurn();
      } else if (isHumanTurn(getTurnByNr(nrOfTurns()-2))) {
        turnNr = nrOfTurns()-2;
        return selectedTurn();
      }
    }
    return selectedTurn();
  }

  public EvaluatedMove selectNextMove () {

    partMoveNr = -1;
    if (++moveNr >= selectedTurn().getNrOfMoves()) {
      moveNr = 0;
    }
    return selectedMove();
  }

  public EvaluatedMove selectPreviousMove () {

    partMoveNr = -1;
    if (--moveNr < 0) {
      moveNr = selectedTurn().getNrOfMoves() - 1;
    }
    return selectedMove();
  }

  private List<MoveLayout> prunedMovePointLayoutList() {

    return
      IntStream.range(0,selectedMove().getMovePointLayouts().size())
        .filter(moveLayoutNr -> moveLayoutNr % 2 == 1)
        .mapToObj(selectedMove().getMovePointLayouts()::get)
        .toList();
  }

  private List<MoveLayout> getPartMoveLayoutList() {

    List<MoveLayout> partMoveLayoutList =
      new ArrayList<>(prunedMovePointLayoutList());

    partMoveLayoutList.add(
      0,
      selectedMove().getParentMoveLayout()
    );
    return partMoveLayoutList;
  }

  public Layout selectNextPartMove () {

    List<MoveLayout> partMoveLayoutList = getPartMoveLayoutList();

    if (partMoveNr == -1 || ++partMoveNr > partMoveLayoutList.size()-1) {
      partMoveNr = partMoveLayoutList.size()-1;
    }
    return partMoveLayoutList.get(partMoveNr);
  }

  public Layout selectPreviousPartMove () {

    List<MoveLayout> partMoveLayoutList = getPartMoveLayoutList();

    if (partMoveNr == -1) {
      partMoveNr = partMoveLayoutList.size()-2;
    } else if (--partMoveNr < 0) {
      partMoveNr = 0;
    }
    return partMoveLayoutList.get(partMoveNr);
  }

}
