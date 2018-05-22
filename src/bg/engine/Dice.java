package bg.engine;

import bg.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dice {

  private int[] dice;


  public Dice () {

    rollDice();
  }

  public Dice (Dice dice) {

    this.dice = dice.getDice();
  }

  public Dice (int[] dice) {

    this.dice = dice.clone();
  }

  public Dice (String dice) {

    setDice(dice);
  }

  public int[] getSwappedDice () {

    return new int[] {dice[1],dice[0]};
  }

  public void setDice (String diceToSet) {

    if (diceToSet.length() < 3) {

      dice = new int[1];

      dice[0] = NumberUtil.parseInt(diceToSet);
    }
  }


  public void setDice (int[] diceToSet) {

    dice = diceToSet.clone();
  }

  public void printDice () {

    System.out.print("Dice:");
    for (int a : dice) {
      System.out.print(a+",");
    }
    System.out.println();
  }

  public Dice startRoll () {

    do {
      rollDice();
    } while (dice.length == 4);
    return this;
  }

  private int[] expandDouble (int[] dice) {

    int[] diceT;

    diceT = new int[4];
    for (int a = 0; a < diceT.length; a++) {
      diceT[a] = dice[0];
    }
    return diceT;
  }

  private int[] makePair (int pseudoPair) {

    String strPair = Integer.toString(pseudoPair);
    int[] intPair = new int[2];

    intPair[0] = Integer.parseInt(strPair.substring(0,1));
    intPair[1] = Integer.parseInt(strPair.substring(1));
    if (intPair[0] == intPair[1]) {
      intPair = expandDouble(intPair);
    }
    return intPair;
  }

  public Dice expandDice() {

    if (dice.length == 1) {
      if (dice[0] > 9) {
      	dice = makePair(dice[0]);
      } else {
      	dice = expandDouble(dice);
      }
    }
    return this;
  }

  public boolean areDouble() {

    return dice.length == 4 || dice[0] == dice[1] || (dice.length == 1 && dice[0] < 7);
  }

  public Dice rollDice() {

    Random random = new Random();

    dice = new int[2];
    for (int a = 0; a < dice.length; a++) {
      dice[a] = random.nextInt(6)+1;
    }
    if (areDouble()) {
      dice = expandDouble(dice);
    }
    return this;
  }

  public Dice initFullSpread () {

    dice = new int[2];
    dice[0] = 1;
    dice[1] = 1;
    return this;
  }

  public Dice setNextSpread () {

    if (dice[0] < 6) {
      dice[0]++;
    } else if (dice[1] < 6) {
      dice[1]++;
      dice[0] = dice[1];
    }
    return this;
  }

  public boolean endOfSpread () {

    return dice[0] == 6 && dice[1] == 6;
  }

  static public void testRandomDice (int nrOfThrows) {

    int[] occurences = new int[7];
    int[] testDice;

    for (int a = 0; a < nrOfThrows; a++) {
      testDice = new Dice().rollDice().getDice();
      for (int b = 0; b < 2; b++) {
        occurences[testDice[b]]++;
      }
    }
    for (int a = 1; a < occurences.length; a++) {
      System.out.println(a+"'s: "+occurences[a]);
    }
  }

  private boolean diceValuesOK () {

    for (int a = 0; a < dice.length; a++) {
      if (dice[a] < 1 || dice[a] > 6) {
        return false;
      }
    }
    return true;
  }

  public boolean diceAreValid () {

    expandDice();
    return (dice.length != 3 && dice.length < 5 &&
      dice.length != 0 && diceValuesOK());
  }

  public int[] getDice () {

    return dice;
  }

}
