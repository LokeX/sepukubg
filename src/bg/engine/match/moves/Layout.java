package bg.engine.match.moves;

import bg.engine.match.Dice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Layout {

  public int[] point;
  protected int playerID;
  protected int hash = 0;
  protected int searchEvaluation;
  private int pip = -1;
  protected int rearPos = -1;
  protected int useBlackBot = 0;
  protected int useWhiteBot = 0;

  public Layout () {

  }

  public Layout(Layout layout) {

    this(layout, layout.playerID);
  }

  public Layout(Layout layout, int pID) {

    point = layout.getPoint();
    playerID = pID;
    useBlackBot = layout.useBlackBot;
    useWhiteBot = layout.useWhiteBot;
    hash = layout.hash;
  }

  public Layout (int[] point) {

    this.point = point.clone();
    playerID = 2;
  }

  public int getHash () {

    return hash;
  }

  public void setUseWhiteBot (int botNr) {

    useWhiteBot = botNr;
  }

  public void setUseBlackBot (int botNr) {

    useBlackBot = botNr;
  }

  public int getPip () {

    if (pip < 0) {
      calcPip();
    }
    return pip;
  }

  public Layout calcPip (){

    pip = 0;
    for (int a = 0; a < 26; a++ ){
      pip += point[a]*a;
    }
    return this;
  }

  public int getWhitePip () {

    return getPlayerPip(0);
  }

  public int getBlackPip () {

    return getPlayerPip(1);
  }

  public int getPlayerPip (int playerID) {

    return
      playerID == this.playerID ?
        calcPip().getPip() :
        getFlippedLayout().calcPip().getPip();
  }

  public int getOpponentPip () {

    return this.getFlippedLayout().calcPip().getPip();
  }

  public int getSearchEvaluation() {

    return searchEvaluation;
  }

  public void setSearchEvaluation(int value) {

    searchEvaluation = value;
  }

  public int[] getPoint () {

    int[] temp = new int[point.length];

    System.arraycopy(point, 0, temp, 0, point.length);
    return temp;
  }

  public void setPoint (int[] newPoint) {

    System.arraycopy(newPoint, 0, point, 0, newPoint.length);
  }

  public Layout getClone () {

    return new Layout(this);
  }

  public void generateHashCode () {

    hash = Arrays.hashCode(point);
  }

  public boolean isIdenticalTo (Layout layout) {

    return layout.hash == hash;
  }

  public int getPlayerID () {

    return playerID;
  }

  public void setPlayerID (int pID) {

    playerID = pID;
  }

  public String getPlayerTitle () {

    return playerID == 0 ? "White" : "Black";
  }

  public void printPlayerTitle () {

    System.out.print(getPlayerTitle());
  }

  public Layout flipLayout () {

    int temp;

    for (int a = 0; a < point.length / 2; a++) {
      temp = point[a];
      point[a] = point[point.length - 1 - a];
      point[point.length - 1 - a] = temp;
    }
    return this;
  }

  public void setLayout (Layout layout) {

    point = layout.point.clone();
  }

  public Layout getFlippedLayout () {

    return new Layout(this).flipLayout();
  }

  public int rearPos () {

    calcRearPos();
    return rearPos;
  }

  public void calcRearPos(int startPoint) {

    rearPos = startPoint;
    while (point[rearPos] == 0) {
      if (--rearPos < 0) {
        break;
      }
    }
  }

  public void printLayout () {

    for (int a = 0; a < point.length; a++) {
      System.out.print(point[a]);
    }
    System.out.println();
  }

  public void calcRearPos() {

    calcRearPos(25);
  }

  public int llep(int rearPos) {

    return rearPos < 7 ? 0 : 1;
  }

  public int llep() {

    return llep(rearPos);
  }

  public Moves getStartMoves() {

    Dice diceT = new Dice();
    Layout layout = new Layout(this);
    int[] dice;

    if (layout.playerID == 2) {
      dice = diceT.startRoll().getDice();
      layout.playerID = dice[0] > dice[1] ? 0 : 1;
    } else {
      dice = diceT.rollDice().getDice();
    }
    if (layout.playerID == 1) {
      layout.flipLayout();
    }
    return new Moves().generateMoves(layout, dice);
  }

  public Moves getNextMoves(int[] dice) {

    return new Moves().generateMoves(
      new Layout(this, playerID == 0 ? 1 : 0).flipLayout(), dice
    );
  }

  public List<Moves> getSearchMoves () {

    List<Moves> movesList = new ArrayList<>(21);
    Dice dice = new Dice();

    movesList.add(
      getNextMoves(dice.initFullSpread().getDice())
    );
    while (!dice.endOfSpread()) {
      movesList.add(getNextMoves(dice.setNextSpread().getDice()));
    }
    return  movesList;
  }

}
