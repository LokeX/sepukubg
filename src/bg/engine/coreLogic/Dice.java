package bg.engine.coreLogic;

import bg.util.NumberUtil;

import java.util.Arrays;
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

    int[] diceTemp = new int[4];

    Arrays.fill(diceTemp, dice[0]);

    return diceTemp;
  }

  private int[] makePair (int pseudoPair) {

    String strPair = Integer.toString(pseudoPair);
    int[] intPair = new int[2];

    intPair[0] = Integer.parseInt(strPair.substring(0,1));
    intPair[1] = Integer.parseInt(strPair.substring(1));

    return intPair;
  }

  public boolean isExpandablePair (int[] pseudoPair) {

    return
      pseudoPair.length == 1
      && pseudoPair[0] > 9
      && pseudoPair[0] < 67;
  }

  public Dice expandDice() {

    if (isExpandablePair(dice)) {
      dice = makePair(dice[0]);
    }
    if (isExpandableDouble(dice)) {
      dice = expandDouble(dice);
    }
    return this;
  }

  public boolean areDouble () {

    return
      dice.length == 4;
  }

  public int distinctDieCount (int[] dice) {

    return (int)
      Arrays
        .stream(dice)
        .distinct()
        .count();
  }

  public boolean areDoubleValues (int[] dice) {

    return
      distinctDieCount(dice) == 1;
  }

  public boolean arePairValues (int[] dice) {

    return
      distinctDieCount(dice) == 2;
  }

  public Dice rollDice() {

    Random random = new Random();

    dice = new int[2];
    for (int a = 0; a < dice.length; a++) {
      dice[a] = random.nextInt(6)+1;
    }
    if (isExpandableDouble(dice)) {
      dice = expandDouble(dice);
    }
    return this;
  }

  public Dice initFullSpread () {

    dice = new int[4];

    Arrays.fill(dice,1);

    return this;
  }

  public boolean isExpandableDouble (int[] dice) {

    return
      dice.length == 1 && dice[0] > 0 && dice[0] < 7
      || dice.length == 2 && dice[0] == dice[1];
  }

  public int[] shrinkNoneDouble (int[] dice) {

    int[] tempDice = new int[2];

    tempDice[0] = dice[0];
    tempDice[1] = dice[1];

    return
      tempDice;
  }

  public Dice setNextSpread () {

    if (dice[0] < 6) {
      dice[0]++;
    } else if (dice[1] < 6) {
      dice[1]++;
      dice[0] = dice[1];
    }
    if (dice.length == 2 && dice[0] == dice[1]) {
      dice = expandDouble(dice);
    } else if (dice.length == 4 && dice[0] != dice[1]) {
      dice = shrinkNoneDouble(dice);
    }
    return this;
  }

  public boolean endOfSpread () {

    return dice[0] == 6 && dice[1] == 6;
  }

  static public void testRandomDice (int nrOfThrows) {

    int[] occurrences = new int[7];
    int[] testDice;

    for (int a = 0; a < nrOfThrows; a++) {
      testDice = new Dice().rollDice().getDice();
      for (int b = 0; b < 2; b++) {
        occurrences[testDice[b]]++;
      }
    }
    for (int a = 1; a < occurrences.length; a++) {
      System.out.println(a+"'s: "+occurrences[a]);
    }
  }

  public void printSpread () {

    initFullSpread().printDice();
    while (!endOfSpread()) {
      setNextSpread().printDice();
    }
  }

  public boolean dieInScope (int die) {

    return
      die > 0 && die < 7;
  }

  public boolean diceValuesOK (int[] dice) {

    return
      Arrays.stream(dice).allMatch(die -> die > 0 && die < 7);
  }

  public boolean diceCountOK (int[] dice) {

    return
      dice.length == 2 || dice.length == 4;
  }

  public boolean diceAreValid () {

    expandDice();
    return
      diceCountOK(dice)
      && diceValuesOK(dice)
      && areDoubleValues(dice)
      || arePairValues(dice);
  }

  public int[] getDice () {

    return dice;
  }

}
