package bg.engine.moves;

import java.lang.reflect.Field;
import java.util.ArrayList;
import static java.util.Arrays.sort;
import java.util.List;

public class EvaluatedMove extends BonusElements {

  public EvaluatedMove(MoveLayout moveLayout) {

    super(moveLayout);
  }

  private int start = 0;
  private int effectivePipPercentage = 0;
  private int gammonWinPercentage = 0;
  private int gammonLoosePercentage = 0;
  private int stackBonus = 0;
  private int gammonBonus = 0;
  private int directShot = 0;
  private int indirectShot = 0;
  private int doubleShot = 0;
  private int luckShot = 0;
  private int potentialHitBonus = 0;
  private int bestDiceBonus;
  private int midpointBonus = 0;
  private int squeezeBonus = 0;
  private int primeBonus = 0;
  private int halfPrimeBonus = 0;
  private int buildBonus = 0;
  private int flexBonus = 0;
  private int barBonus = 0;
  private int reverseBarBonus = 0;
  private int singleBonus = 0;
  private int bGBonus = 0;
  private int noContactBonus = 0;
  private int domesticViolence = 0;
  private int bullsEyeBonus = 0;
  private int bullsNosePenalty = 0;
  private int homeCommingBonus = 0;
  private int checkingOutBonus = 0;
  private int deadFoesBonus = 0;
  private int deadFoesBonusPoint123 = 0;
  private int runningBonus = 0;
  private int runningWildBonus = 0;
  private int trapBonus = 0;
  private int kamikazeBonus = 0;
  private int backBonus = 0;
  private int anchorBonus = 0;
  private int reserveBonus = 0;
  private int holdBonus = 0;
  private int luckyHoldBonus = 0;

  private int totalDoubleBonus = 0;
  private int totalDoubleBonusPercentage = -1;
  private int layoutDoubleStrength = -1;

  private int totalBonus = 0;
  protected int totalBonusPercentage = -1;
  protected int layoutStrength = -1;
  private int end = 0;


  boolean doubble;
  boolean doubleTake;

  private int[] escapeDice = {0,0,0,0,0,0};
  private int[] potentialHitDice = {0,0,0,0,0,0};
  private int[] toppingValue = {0,0,0,0,0,0};

  private int pip = 0;
  private int ePip = 0;
  private int bGPip = 0;
  private int barPip = 0;
  private int foePip = 0;
  private int foeEPip = 0;
  private int average = 0;
  private int foeBGPip = 0;
  private int gammonPip = 0;
  private int foeAverage = 0;
  private int foesAtHome = 0;
  private int foeGammonPip = 0;
  private int foesInvading = 0;
  private int pseudoAverage = 0;
  private int blockadeCount = 0;
  private int reverseBarPip = 0;
  private int chekkersAtHome = 0;
  private int chekkersInvading = 0;
  private int foeBlockadeCount = 0;
  private int foesAwayFromHome = 0;
  private int foePseudoAverage = 0;
  private int chekkersAwayFromHome = 0;
  private int pipModifier = 0;
  private int contact = 0;
  private int reverseContact = 0;
  private int pointContact = 0;
  private int reversePointContact = 0;
  private int pipCount = 0;
  private int pipPercentage = 0;
  private int bGWinPercentage = 0;
  private int bGLoosePercentage = 0;
  private int effectivePipCount = 0;

  private int onRoll = 0;
  private int bonusNullifier = 1;

  private boolean displayingMove = false;
  private boolean bonusCalculated = false;

  private List<String> bonusTexts = new ArrayList();
  private List<Integer> bonusValues = new ArrayList();
  private List<Integer> foeValues = new ArrayList();

  public List<String> getBonusTexts () {

    return bonusTexts;
  }

  public List<Integer> getBonusValues () {

    return bonusValues;
  }

  public List<Integer> getFoeValues () {

    return foeValues;
  }
//  MatchApi matchApi;

//    int layoutStrength = 10000-Main.round.getTurnByNr(Main.round.getLatestTurn().getTurnNr()-1).getSelectedMove().getLayoutStrength();



  protected List<String> getFieldNamesAndSetPositions () {

    List<String> names = new ArrayList();
    Field[] fields = this.getClass().getDeclaredFields();
    String name;

    boolean startReached = false;
    int fieldNr = 0;

    do {
      name = fields[fieldNr++].getName();
      if (startReached || name.equals("start")) {
        if (!startReached) {
          start = fieldNr;
          startReached = true;
        } else {
          names.add(name);
        }
      }
    } while(!name.equals("end"));
    end = fieldNr;
    return names;
  }

  private List<Integer> getFieldValues () {

    Field[] fields = this.getClass().getDeclaredFields();
    List<Integer> values = new ArrayList();

    try {
      for (int a = start; a < end; a++) {
        values.add((Integer)fields[a].get(this));
      }
    } catch (IllegalAccessException iae) {
      System.out.println(iae.getMessage());
    }
    return values;
  }

  public void checkPoints() {

    pip = 0;
    ePip = 0;
    bGPip = 0;
    barPip = 0;
    foePip = 0;
    foeEPip = 0;
    average = 0;
    foeBGPip = 0;
    gammonPip = 0;
    foeAverage = 0;
    foesAtHome = 0;
    foeGammonPip = 0;
    foesInvading = 0;
    pseudoAverage = 0;
    blockadeCount = 0;
    reverseBarPip = 0;
    chekkersAtHome = 0;
    chekkersInvading = 0;
    foeBlockadeCount = 0;
    foesAwayFromHome = 0;
    foePseudoAverage = 0;
    chekkersAwayFromHome = 0;

    int checkPointsReverseBarPipPow = checkPointsBarPipPow;

    for (int a = 0; a < 26; a++ ){
      pip += point[a]*a;
      foePip += point[51-a]*a;
      if (a < 7){
        if (point[a] > 1 && a != 0) {
          blockadeCount++;
        }
        if (point[51-a] > 1 && a != 0) {
          foeBlockadeCount++;
        }
        bGPip += point[18+a]*a;
        foeBGPip += point[33-a]*a;
        chekkersInvading += point[25-a];
        foesInvading += point[26+a];
        chekkersAtHome += point[a];
        foesAtHome += point[51-a];
      } else {
        gammonPip += point[a]*(a-6);
        foeGammonPip += point[51-a]*(a-6);
        chekkersAwayFromHome += point[a];
        foesAwayFromHome += point[51-a];
      }
      pipCount = pip;
      pipPercentage = (int)(100*((float)pip/(float)(pip + foePip)));

      average = chekkersAtHome/6;
      foeAverage = foesAtHome/6;
      pseudoAverage = (chekkersAtHome+checkPointsPseudoAdder)/6;
      foePseudoAverage = (foesAtHome+checkPointsPseudoAdder)/6;

      ePip = pip;
      ePip += 2+(pip/6);

      foeEPip = foePip;

      for (int c = 1; c < 4; c++){
        if (point[c] > average){
          ePip += (point[c]-average)*(4-c);
        }
        if (point[51-c] > foeAverage){
          foeEPip += (point[51-c]-foeAverage)*(4-c);
        }
      }
      barPip = (int)Math.pow(blockadeCount, checkPointsBarPipPow);
      reverseBarPip = (int)Math.pow(foeBlockadeCount, checkPointsReverseBarPipPow);
      ePip += point[25]*(reverseBarPip);
      foeEPip += point[26]*(barPip);
      effectivePipCount = ePip;
      effectivePipPercentage = (int)(100*((float)ePip/(float)(ePip + foeEPip)));
    }
  }

  private void contact() {
// counting the number of your chekkers still facing opponent chekkers
// counting the number of points, from where your chekkers are still facing opponent chekkers
    contact = 0;
    pointContact = 0;

    for (int a = 1; a < 26; a++){
      if (point[a] > 0){
        for (int b = 0; a-b > 0; b++){
          if (point[26] > 0 || point[a-b+26] > 0){
            pointContact++;
            contact += point[a];
            break;
          }
        }
      }
    }
  }

  private void reverseContact() {
// counting the number of foes still facing your chekkers
    reverseContact = 0;

    flipLayout();
    contact();
    reverseContact = contact;
    reversePointContact = pointContact;
    flipLayout();
    contact();
  }

  private void pipModifier(){

    pipModifier = 5;
//  +/- 5
    int pipModifierCount = 0;

    if (effectivePipPercentage <= pipModifierTippingPoint){
//  hvis effectivePipPercentage <= 50
      for (int a = pipModifierLowSquare; a < pipModifierHighSquare; a++){
//  a = 2-6
        if (effectivePipPercentage <= pipModifierTippingPoint-(a*a)){
//  hvis effectivePipPercentage <= 50-4 = 46        50-36 = 14
          pipModifierCount = a-2;
//  a = 0-4         5-9
        }
      }
    } else {
//  hvis effectivePipPercentage > 50
      for (int a = pipModifierLowSquare; a < pipModifierHighSquare; a++){
        if (effectivePipPercentage < pipModifierTippingPoint+(a*a)){
          pipModifierCount = a+2;
//  a = 0-4         1-5
        }
      }
    }
    pipModifier += pipModifierCount;
  }

  private void calcBonusNullifier () {

    int trapCount = 0;
    bonusNullifier = 1;

    for (int a = 0; contact > 0 && trapCount < 6 && a < 7; a++) {
      if (point[25-a] > 0){
        for (int b = 1; b < 12; b++){
          if (point[51-a-b] > 1){
            trapCount++;
            if (trapCount > 5){
              bonusNullifier = 0;
            }
          } else {
            trapCount = 0;
          }
        }
      }
    }
  }

  private void gammonPercentage () {
//  hvad er sandsynligheden for at tabe/vinde en backgammon?

    gammonWinPercentage = 0;
    gammonLoosePercentage = 0;

    for (int d = 1; d < 4; d++){
      if (point[45-d] > foePseudoAverage){
        foeGammonPip += (point[45-d]-foePseudoAverage)*(4-d);
      }
      if (point[d] > pseudoAverage){
        ePip += (point[d]-pseudoAverage)*(4-d);
      }
      if (point[6+d] > pseudoAverage){
        gammonPip += (point[6+d]-pseudoAverage)*(4-d);
      }
      if (point[51-d] > foeAverage){
        foeEPip += (point[51-d]-foeAverage)*(4-d);
      }
    }
    foeGammonPip += point[26]*(barPip);
    gammonPip += point[25]*(reverseBarPip);
    if (point[51] > 0){
      gammonWinPercentage = 0;
    } else {
      gammonWinPercentage = (int)(100*((float)foeGammonPip/(float)(foeGammonPip + ePip)));
    }
    if (point[0] > 0){
      gammonLoosePercentage = 0;
    } else {
      gammonLoosePercentage = (int)(100*((float)gammonPip/(float)(gammonPip + foeEPip)));
    }
  }

  private void bGLoosePercentage () {
//  hvad er sandsynligheden for at tabe en backgammon?

    bGLoosePercentage = 0;

    for (int c = 1; c < 4; c++){
      if (point[18+c] > pseudoAverage){
        bGPip += (point[18+c]-pseudoAverage)*(4-c);
      }
      if (point[51-c] > foeAverage){
        foeEPip += (point[51-c]-foeAverage)*(4-c);
      }
    }
    bGPip += point[25]*(reverseBarPip);

    if (point[0] > 0){
      bGLoosePercentage = 0;
    } else {
      bGLoosePercentage = (int)(100*((float)bGPip/(float)(bGPip + foeEPip)));
    }
  }

  private void bGWinPercentage () {

    bGWinPercentage = 0;

    for (int c = 1; c < 4; c++){
      if (point[33-c] > foePseudoAverage){
        foeBGPip += (point[33-c]-foePseudoAverage)*(4-c);
      }
      if (point[c] > average){
        ePip += (point[c]-average)*(4-c);
      }
    }
    foeBGPip += point[26]*(barPip);
    if (point[51] > 0){
      bGWinPercentage = 0;
    } else {
      bGWinPercentage = (int)(100*((float)foeBGPip/(float)(foeBGPip + ePip)));
    }
  }
  /*
    private void calcGammonWinPercentage () {
  //  hvad er sandsynligheden for at vinde en backgammon?

      for (int d = 1; d < 4; d++){
        if (point[45-d] > pseudoAverage){
          foeGammonPip += (point[45-d]-pseudoAverage)*(4-d);
        }
        if (point[d] > average){
          ePip += (point[d]-average)*(4-d);
        }
      }
      foeGammonPip += point[26]*(barPip);
      if (point[51] > 0){
        gammonWinPercentage = 0;
      } else {
        gammonWinPercentage = (int)(100*((float)foeGammonPip/(float)(foeGammonPip + ePip)));
      }
    }
  */
  private void calcGammonBonus () {

    int bonus = 1;

    gammonBonus = 0;

    for (int a = 18; bonus == 1 && a < 26; a++){
      if (point[a] > 0){
        bonus = 0;
//    hvis man har mindst 1 brik i position 18-25, så er der ingen bonus
      }
    }
    if (chekkersAtHome < 11){
      bonus = 0;
    }
//    hvis man har mindre end 11 brikker i position 0-6, så er der ingen bonus
    if (bonus == 1){
//      calcGammonWinPercentage();
      gammonBonus += point[0] > 10 && point[51] == 0 ? 1000 : 0;
      gammonPercentage();
      for (int a = 1; a < 4; a++){
        for (int b = 1; b < 7; b++){
          int d = (6*a)+b;
          gammonBonus += point[51-d]*a*bonus*gammonWinPercentage;
        }
      }
    }
    gammonBonus += point[26]*4*bonus*gammonWinPercentage;
//    if (chekkersInvading ){

  }

  private void calcBGBonus () {

    bGBonus = 0;

    int bGFoes = 0;
    int chekkersCheckingOut = chekkersAtHome- point[0];

    if (point[51] == 0 && foesInvading > 0 && chekkersAtHome == 15){
      for (int a = 1; a < 7; a++){
        if (a < 4){
          bGFoes += (point[26+a] < 2) ? point[26+a]*3 : point[26+a]*4;
        } else {
          bGFoes += (point[26+a] < 2) ? point[26+a]*2 : point[26+a]*3;
        }
//        bGFoes += (a < 4) ? point[26+a]*3 : point[26+a]*2;
      }
      bGFoes += point[26]*5;
      if (point[0] > 7){
        if (bGFoes >= (chekkersCheckingOut/4)){
          bGBonus += (int)Math.pow(1+(bGFoes-(chekkersCheckingOut/4)), bGBonusPow)*bGBonusMultiplier;
        }
      } else {
        if (bGFoes >= ((chekkersAtHome- point[0])/2)){
          bGBonus += (int)Math.pow(1+(bGFoes-(chekkersCheckingOut/2)), bGBonusPow)*bGBonusMultiplier;
        }
      }
    }
  }

  private void calcSingleBonus () {

    singleBonus = 0;

    reverseContact();

    int count = 0;

    for (int a = 1; a < 25; a++){
      if (point[a+26] == 1){
        count++;
      }
    }
    if (count > point[25]){
      count -= point[25];
    }

    singleBonus = (int) Math.pow(count,singleBonusPow);
    for (int a = 0; a < 4; a++){
      if (reverseContact == a){
        singleBonus = (int) Math.pow(count,a);
      }
    }
    if (point[26] > 0){
      singleBonus *= (1+ point[26]);
    }
    if (point[25] > 0){
      singleBonus /= (1+ point[25]);
    }
    singleBonus *= singleBonusMultiplier;
  }

  private void calcBarBonus () {

    checkPoints();

    int lowVacantPoints = 0;
    int highVacantPoints = 0;
    int rearPos = rearPos();
    barBonus = 0;

    if (point[26] > 0){
      barBonus = (point[26]+barBonusBarPointModifier)*barPip*pipModifier*barBonusMultiplier*bonusNullifier;
    }
/*
System.out.println("point[26] = "+point[26]);
System.out.println("barPip = "+barPip);
System.out.println("pipModifier = "+pipModifier);
System.out.println("barBonusMultiplier = "+barBonusMultiplier);
System.out.println("bonusNullifier = "+bonusNullifier);
*/

    if (chekkersAtHome == 15 && barBonus > 0 && blockadeCount < 6){
//  denne rutine skal dels sikre at man ikke for enhver pris smider modpartens brikker på barren
//  desuden skal den belønne at man trækker sine brikker sikkert ud, i stedet for at åbne en ladeport for modpartens kamikaze- eller anker-brikker
      for (int a = 1; a < 7; a++){
        if (point[a] == 0 && point[26+a] == 0){
          if (a > rearPos){
//  hvis hullet i blokaden er bag rearPos
            highVacantPoints++;
          } else {
//  hvis hullet i blokaden er foran rearPos
            lowVacantPoints++;
          }
        }
      }
      if (lowVacantPoints >= highVacantPoints){
//  hvis der flere huller i blokaden foran rearPos end bag rearPos
        barBonus *= 0;
      } else {
//  hvis der flere huller i blokaden bag rearPos end foran rearPos
        barBonus *= highVacantPoints;
        if (lowVacantPoints > 0){
          barBonus /= lowVacantPoints;
        }
      }
    }

  }

  private void calcBuildAndFlexBonus () {

    boolean[] flex = new boolean[] {false,false,false,false,false};

    buildBonus = 0;
    flexBonus = 0;

    int BuildAndFlexBonusContactModifier;

    if (pointContact >= BuildAndFlexBonusContactModifierLimit){
      BuildAndFlexBonusContactModifier = (int) Math.pow(BuildAndFlexBonusContactModifierLimit, 2);
    } else {
      BuildAndFlexBonusContactModifier = pointContact*pointContact;
    }
    for (int a = 1; a < 25; a++) {
      if (point[a] > 0 && point[a] != 2){
        for (int b = 1; b < 6; b++){
          if (a+b < 25 && (point[a+b] > 0 && point[a+b] != 2)){
            for (int c = 6-b; c > 0; c--){
              if (a-c > 0 && point[26+a-c] < 2){
                flex[b-1] = true;
                buildBonus += (a-c < 7) ? (6-b)*3 : (6-b)*2;
              }
            }
          }
        }
      }
    }
    for (int d = 0; d < 5; d++){
      if (flex[d]) {
        flexBonus += flexBonusMultiplier*BuildAndFlexBonusContactModifier;
        if (point[25] > 0){
          flexBonus /= (1+ point[25]);
        }
      }
    }
    buildBonus *= BuildAndFlexBonusContactModifier*buildBonusMultiplier;
    buildBonus /= (buildBonusDivisor+(point[25]*BuildAndFlexBonusBarDivisorMulitplier));
    if (blockadeCount == 6 && reverseContact == point[26]){
      buildBonus *= 0;
      flexBonus *= 0;
    }
  }

  private void calcStackBonus () {

    stackBonus = 0;

    int[] idealBonuses;

    int temp;
    int stacBonusContactMultiplier;

    if (contact >= stacBonusMaxContact){
      stacBonusContactMultiplier = stacBonusMaxContact*stacBonusMaxContact;
    } else {
      stacBonusContactMultiplier = contact*contact;
    }
    if (stacBonusContactMultiplier == 0){
      stacBonusContactMultiplier = 1;
    }
    if (contact > 0){
      idealBonuses = new int[] {7,-5,-2,0,2,3,4,2,1,1,1,1,1,2,1,1,1,1,2,1,2,1,1,1,1};
    } else {
      idealBonuses = new int[] {23,0,0,0,3,5,7,-2,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//      reference                {00,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6};
    }
    for (int a = 0; a < 25; a++){
      if (point[51-a] > 0){
        temp = 0;
        int c = 0;
        for (int b = point[51-a]; b > 0; b--){
          temp = (idealBonuses[a]-c);
          c++;
        }
        if (temp < 0){
          stackBonus += temp;
        }
      }
    }
    for (int a = 0; a < 25; a++){
      if (point[a] > 0){
        temp = 0;
        int c = 0;
        for (int b = point[a]; b > 0; b--){
          temp = (idealBonuses[a]-c);
          c++;
        }
        if (temp > 0){
          stackBonus += temp;
        }
      }
    }

    if (contact == 0){
      stackBonus /= stacBonusNoContactDivisor;
    }
    stackBonus *= stacBonusContactMultiplier;
    if (stackBonus < 0){
      stackBonus = 0;
    }
  }

  private void calcRunningWildBonus () {

    runningWildBonus = 0;

    int dice;
    int primaryGammonStackBonus = 0;
    int secondaryGammonStackBonus = 0;
    int tertiaryGammonStackBonus = 0;
    int count;

    if (gammonLoosePercentage < 80 && gammonLoosePercentage > runningWildBonusGammonLoosePercentageFactor1){
      count = 0;
      for (int a = 0; a < 4; a++){
        dice = 4;
//      44444444444444444444444444444444444444444444444444444444
        if (a != 0 || point[10+(a*dice)] < 2){
          count += point[10+(a*dice)];
        }
      }
      primaryGammonStackBonus += count*count*runningWildBonusPanicFactor;
      count = 0;
      for (int a = 0; a < 3; a++){
        dice = 5;
//      55555555555555555555555555555555555555555555555555555555
        if (a != 0 || point[11+(a*dice)] < 2){
          count += point[11+(a*dice)];
        }
      }
      primaryGammonStackBonus += count*count*runningWildBonusPanicFactor;
      count = 0;
      for (int a = 0; a < 3; a++){
        dice = 6;
//      66666666666666666666666666666666666666666666666666666666
        if (a != 0 || point[12+(a*dice)] < 2){
          count += point[12+(a*dice)];
        }
      }
      primaryGammonStackBonus += count*count*runningWildBonusPanicFactor;
    }
    if (gammonLoosePercentage < 80 && gammonLoosePercentage > runningWildBonusGammonLoosePercentageFactor2){
      count = 0;
      for (int a = 0; a < 3; a++){
        dice = 5;
//      55555555555555555555555555555555555555555555555555555555
        if (a != 0 || point[10+(a*dice)] < 2){
          count += point[10+(a*dice)];
        }
      }
      if (point[5] == 0 && count > 0){
        count--;
      }
      secondaryGammonStackBonus += count*count*runningWildBonusPanicFactor;
      count = 0;
      for (int a = 0; a < 3; a++){
        dice = 6;
//      66666666666666666666666666666666666666666666666666666666
        if (a != 0 || point[11+(a*dice)] < 2){
          count += point[11+(a*dice)];
        }
      }
      if (point[5] == 0 && count > 0){
        count--;
      }
      secondaryGammonStackBonus += count*count*runningWildBonusPanicFactor;
    }
    if (gammonLoosePercentage < 80 && gammonLoosePercentage > runningWildBonusGammonLoosePercentageFactor3){
      count = 0;
      for (int a = 0; a < 3; a++){
        dice = 6;
//      66666666666666666666666666666666666666666666666666666666
        if (a != 0 || point[10+(a*dice)] < 2){
          count += point[10+(a*dice)];
        }
      }
      tertiaryGammonStackBonus += count*count;
    }
    primaryGammonStackBonus /= runningWildBonusPrimaryDivisor;
    secondaryGammonStackBonus /= runningWildBonusSecondaryDivisor;
    tertiaryGammonStackBonus /= runningWildBonusTertiaryDivisor;
    runningWildBonus += primaryGammonStackBonus+secondaryGammonStackBonus+tertiaryGammonStackBonus;
  }

  private void calcRunningBonus () {

//  no real concerns:
    boolean endPoint = true;
//  essentials:
    int[] runningPoint;

    runningBonus = 0;
    domesticViolence = 0;
    bullsEyeBonus = 0;
    bullsNosePenalty = 0;
    noContactBonus = 0;

//  multiple concerns:
    int count = 0;
    int chekkersOnTheRun;
    int anchorChekkers = 0;
//  concerning noContactBonus;
    int foeRearPos = 1;

//  concerning noContactBonus:
    int argument = (int) ((noContactBonusTippingPointMultiplier*noContactBonusTippingPoint)+
      ((int)Math.pow(blockadeCount, noContactBonusBlockadePow)*noContactBonusBlockadeMultiplier)-
      (noContactBonusTippingPointMultiplier*effectivePipPercentage)+
      ((int)Math.pow(foeBlockadeCount, noContactBonusBlockadePow)*noContactBonusBlockadeMultiplier));

    for (int a = 27; contact == 0 && a < 51; a++){
      if (point[a] > 0){
        foeRearPos = a-26;
        break;
      }
    }

    for (int a = 19; foesAtHome == 15 && a < 26; a++){
      if (point[a] > 0){
        for (int b = 18; b < a; b++){
          if (point[b+26] > 0){
            anchorChekkers += point[a];
            break;
          }
        }
      }
    }
    chekkersOnTheRun = 15-(chekkersAtHome+anchorChekkers);

    if (onRoll == 1){
      runningBonus = 0;
    } else {
      if (playerID == 1){
        runningPoint = movePoints2;
      } else {
        runningPoint = movePoints;
      }
      for (int a = 0; runningPoint[0] != -1 && foesAtHome > 10 && onRoll == 0 && a < runningPoint.length; a++){
        if (count == 1){
//        runningPoints[a] is an ending point
          if (runningPoint[a] == 6){
            bullsEyeBonus += bullsEyeBaseBonus+(int)Math.pow(chekkersOnTheRun, bullsEyePow)*bullsEyeBonusFactor;
          }
          for (int b = 1; chekkersOnTheRun > 0 && b < 6; b++){
            if (runningPoint[a] == b){
              bullsNosePenalty -= (int)Math.pow((6-b), bullsNosePenaltyPow)*(int)Math.pow(chekkersOnTheRun, bullsNosePenaltyPow2)*bullsNosePenaltyMultiplier;
            }
          }
          count = 0;
        } else {
//      runningPoints[a] is a starting point
          if (runningPoint[a] > foeRearPos && contact == 0) {
            noContactBonus = noContactBonusMultiplier*argument;
          }
          if (chekkersOnTheRun > 0 && (contact == 0 || (foesAtHome == 15 && gammonLoosePercentage > domesticViolenceGammonLooseTippingPoint))){
            if (runningPoint[a] < 7){
//          if starting point < 7
//            System.out.println("starting point["+runningPoints[a]+"] lower than 7");
              domesticViolence -= (int)Math.pow((6-runningPoint[a]), domesticViolencePow)*(int)Math.pow(chekkersOnTheRun, domesticViolenceOnTheRunPow)*domesticViolenceBonusMultiplier;
//            the lower the starting point, the higher the penalty
            }
          }
          count++;
        }
      }
    }
    runningBonus += runningBonusFrame+bullsNosePenalty+bullsEyeBonus+domesticViolence+noContactBonus;
    if (runningBonus < 0){
      runningBonus = 0;
    }
  }

  private void calcHomeCommingBonus () {

    homeCommingBonus = 0;

    int[] rearHomePoint;
    int[] rearHomePointChekkers;

    int rearPos = rearPos();

//    int anchorFoes = 0;
    int anchorChekkers = 0;
    int chekkersQuadrantsAway = 0;
    int combinedCount;
    int count = 0;
    int vacantPoint = 0;

    int rearHomePoint1Mulitplier;
    int rearHomePoint2Mulitplier;
    int rearHomePoint3Mulitplier;

    for (int a = rearPos; rearPos < 8 && chekkersAtHome > 11 && a > 0; a--){
      if (point[a] == 0 && point[26+a] == 0){
        vacantPoint ++;
      }
      if (point[26+a] > 0){
        break;
      }
    }

    for (int a = 0; a < 6; a++){
/*
      if (point[a+26] > 0){
        for (int b = a+1; b < 7; b++){
          if (point[b] > 0){
            anchorFoes += point[a+26];
            break;
          }
        }
      }
*/
      if (point[25-a] > 0){
        for (int b = a+1; b < 7; b++){
          if (point[51-b] > 0){
            anchorChekkers += point[25-a];
            break;
          }
        }
      }
    }

    for (int a = 1; a < 4; a++){
      for (int b = 1; b < 7; b++){
        if (point[(a*6)+b] > 0){
          chekkersQuadrantsAway += point[(a*6)+b]*a;
        }
      }
    }

    rearHomePoint = new int[] {7,0,0,0};
    rearHomePointChekkers = new int[] {0,0,0,0};

    for (int a = 0; rearHomePointChekkers[3] == 0 && a < 3; a++){
      for (int b = rearHomePoint[a]-1; b > 0; b--){
        if (point[b] > 0){
          rearHomePoint[a+1] = b;
          rearHomePointChekkers[a+1] = point[b];
          break;
        }
      }
    }

    rearHomePoint1Mulitplier = homeCommingBonusRearHomePointMulitplierReducer+rearHomePoint[1];
    rearHomePoint2Mulitplier = homeCommingBonusRearHomePointMulitplierReducer+rearHomePoint[2];
    rearHomePoint3Mulitplier = homeCommingBonusRearHomePointMulitplierReducer+rearHomePoint[3];

    if (anchorChekkers == 0 && chekkersAtHome > 11 && rearHomePointChekkers[1] > 0){
      combinedCount = chekkersQuadrantsAway+rearHomePointChekkers[1];
      for (int a = 2; combinedCount > 1 && a < 7; a++){
        if (combinedCount >= a){
          homeCommingBonus += (homeCommingBonusBonusCounter-count)*rearHomePoint1Mulitplier;
          combinedCount -= 2;
          count += 2;
        }
        a++;
      }
      if (count < 6 && rearHomePointChekkers[2] > 0){
        for (int a = 2; count < 6 && a < 6; a++){
          if (rearHomePointChekkers[2]+combinedCount == a){
            homeCommingBonus += (homeCommingBonusBonusCounter-count)*rearHomePoint2Mulitplier;
            combinedCount = 0;
            count += a;
            break;
          }
          a++;
        }
        if (count < 6 && rearHomePointChekkers[3] > 0){
          for (int a = 2; count < 6 && a < 6; a++){
            if (rearHomePointChekkers[3]+combinedCount == a){
              homeCommingBonus += (homeCommingBonusBonusCounter-count)*rearHomePoint3Mulitplier;
              count += a;
              break;
            }
          }
        }
      }
    }
    if (vacantPoint > 0){
      homeCommingBonus *= homeCommingBonusVacantPointEqualizer;
      homeCommingBonus /= (homeCommingBonusVacantPointEqualizer+vacantPoint);
    }
  }

  private void calcCheckkingOutBonus(){

    checkingOutBonus = 0;

//    int count = 0;
    int rearPos = rearPos();
    int secondRearPos = 0;
    int threeChekkersLeftBonus = 0;
    int fourChekkersLeftBonus = 0;
    int singleChekkers = 0;

    if (chekkersAtHome == 15){
      for (int a = 1; a < 7; a++){
        singleChekkers += point[a] == 1 ? 1 : 0;
      }
      if (contact > 0 && singleChekkers < 2 && point[0] == 11){
        fourChekkersLeftBonus = (6-rearPos)*checkingOutBonusFourChekkersLeftBaseBonus;
      }
      for (int a = rearPos; a > 0; a--){
        if (point[a] > 0){
          secondRearPos = a;
        }
      }
      if (rearPos <= (secondRearPos*2) && point[0] == 12){
        threeChekkersLeftBonus = checkingOutBonusThreeChekkersLeftBaseBonus;
      }
    }
    if (contact > 0 && (point[rearPos] == 2 || point[rearPos] == 4 || point[rearPos] == 6)){
      checkingOutBonus += (7-rearPos)*foeBlockadeCount*checkingOutBonusPoint123Multiplier;
    }

    checkingOutBonus += threeChekkersLeftBonus+fourChekkersLeftBonus;
  }

  private void calcDeadFoesBonus () {

    deadFoesBonus = 0;
    deadFoesBonusPoint123 = 0;

    int dyingFoes = 0;
    int deadFoes = 0;
    int killerChekkers = 0;

    for (int a = 1; a < 6; a++){
      dyingFoes += point[51-a];
      killerChekkers += point[a] > 1 ? 2 : 0;
      if (dyingFoes+killerChekkers > (a*2)){
        deadFoes += dyingFoes+killerChekkers-(a*2);
        deadFoesBonus += deadFoes*(int)Math.pow((6-a),deadFoesBonusPow)*(foePip/deadFoesBonusPipDivisor)*deadFoesBonusMultiplier;
      }
    }

    for (int a = 1; a < 4; a++){
      if (point[51-a] < 2){
        break;
      }
      if (point[51-a] > 2){
        deadFoesBonusPoint123 += ((int)Math.pow((point[51-a]-1), (deadFoesBonusPoint123Pow-a)))*deadFoesBonusPoint123Multiplier;
      }
    }
  }

  private void calcPrimeBonus () {

    primeBonus = 0;

    int count;

    for (int a = 2; a < 8; a++){
      if (point[a] > 1){
        count = 0;
        for (int b = 1; b < 6; b++){
          if (point[a+b] > 1){
            primeBonus += (7-b)*((a+b) < 8 ? 2 : 1);
            count++;
          } else {
            count = 0;
          }
          if (count == 5){
            primeBonus += 50;
          }
        }
      }
    }
    primeBonus *= ((foePip*6)/4)+pipPercentage+(pointContact*primeBonusPointContactMultiplier);
    primeBonus *= primBonusMultiplier;
    primeBonus /= primBonusDivisor;
//  75 % foePipCount + 20 % pipPercentage + 5 % pointContact - multiplying by 300
//  foePipCount probpably diminishing steadily, pointContact probpably diminishing slightly while pipPercentage can slowly go either way for say;
//  25 % foePipCount + 65 % pipPercentage + 10 % pointContact - multiplying by 150 to 250
  }

  private void calcHalfPrimeBonus () {

    halfPrimeBonus = 0;

    int contactModifier;
    int rearValue;
    int frontValue;
    int temp;
    int value = 0;

    if (contact > halfPrimeBonusContactHigh){
      contactModifier = halfPrimeBonusContactHigh;
    } else {
      contactModifier = contact-halfPrimeBonusContactModifierModifier;
    }

    toppingValue = new int[]{0,0,0,0,0,0};

    for (int a = 1; point[25] < 2 && a < 25; a++){
      if (point[a] == 1){
//  hvis man har en enlig brik, der gerne vil toppes (og det vil den særligt hvis a < 7)
        for (int b = 1; b < 7; b++){
          if (point[a+b] > 0 && a+b < 26){
//  og hvis op til seks positioner længere tilbage har en position med 1 eller flere brikker
            rearValue = 0;
            for (int c = 0; a+c < 26 && c < 6; c++){
              if (point[a+c] > 1 && c != b){
//  så kigger vi 0-5 positioner (fraregnet position a+b) tilbage fra positionen med den enlige brik (point[a])
                temp = (6-c)*pipModifier*contactModifier;
                temp *= a+c < 8 ? halfPrimeBonusHomeValue : halfPrimeBonusAwayValue;
                temp /= point[a+b] != 2 ? 1 : halfPrimeBonusDualTopperDivisor;
                rearValue += temp;
//  værdien øges for her gang man har mindst 2 (og helst flere) brikker i en position bag (helt tæt bag) den enlige brik
              }
            }
            value += rearValue;
            frontValue = 0;
            for (int d = 1; a-d > 0 && d < 6; d++){
              if (point[a-d] > 1){
//  så kigger vi 1-5 positioner frem fra positionen med den enlige brik (point[a])
                temp = (6-d)*pipModifier*contactModifier;
                temp *= a-d < 8 && a-d > 1 ? halfPrimeBonusHomeValue : halfPrimeBonusAwayValue;
                temp /= point[a+b] != 2 ? 1 : halfPrimeBonusDualTopperDivisor;
                frontValue += temp;
//  værdien øges for her gang man har mindst 2 (og helst flere) brikker i en position foran (og helst tæt foran) den enlige brik
              }
            }
            value += frontValue;
            if (point[25] == 0 && value > toppingValue[b-1]){
              toppingValue[b-1] = value;
            }
          }
        }
        halfPrimeBonus = value;
        if (point[25] > 0){
          halfPrimeBonus *= (halfPrimeBonusBarDivisorEqualizer+ point[26]);
          halfPrimeBonus /= (halfPrimeBonusBarDivisorEqualizer+(point[25]*halfPrimeBonusBarDivisor));
        }
        halfPrimeBonus /= halfPrimeBonusDivisor;
        sort(toppingValue);
      }
    }
  }

  private void calcPotentialHitBonus () {

    directShot = 0;
    indirectShot = 0;
    doubleShot = 0;
    luckShot = 0;

    int riskyHitDivisor;
    int quadrantDivisor;
    int temp;
    int temp2;
    int count;

    potentialHitDice = new int[]{0,0,0,0,0,0};
    potentialHitBonus = 0;

    for (int b = 1; b < 7; b++){
//  terning b
      temp2 = 0;
      for (int a = 25; a-b > 0 && a > 1; a--){
        riskyHitDivisor = 1;
        for (int d = 1; a != 25 && d < 5; d++){
          if (a <= 6*d){
            riskyHitDivisor = 5-d;
            break;
          }
        }
        if (point[a] > 0){
//  point[a] er position, der hittes fra
          if (point[26+a-b] == 1){
/*
            quadrantDivisor = 4;
            for (int e = 1; e < 5; e++){
              if (a-b >= (e*6)){
                quadrantDivisor--;
                break;
              }
            }
*/
//  point[26+a-b] er position, der hittes fra med terning b

//  System.out.println("riskyHitDivisor = "+riskyHitDivisor);
//  System.out.println("directHit from point["+a+"] to point["+(a-b)+"]");
            if (point[25] == 0 || a == 25){
              temp = (a-b)+blockadeCount;
//              temp /= quadrantDivisor;
              temp2 += (point[a] != 2 || a == 25) ? temp : temp/riskyHitDivisor;
            }
            if (point[25] == 1 && a != 25){
              count = 0;
              for (int c = 1; c < 7; c++){
                if (point[51-c] < 2 && c != b){
                  count++;
                }
                temp = (a-b)+blockadeCount;
                temp *= count;
                temp /= 5;
                temp2 += (point[a] != 2) ? temp : temp/riskyHitDivisor;
              }
            }
          }
          directShot += temp2;
          if (potentialHitDice[b-1] < temp2){
            potentialHitDice[b-1] = temp2;
          }
        }
      }
    }

    for (int b = 1; b < 7; b++){
      for (int a = 25; a-b > 0 && a > 1; a--){
        if (point[a] > 0 && (point[25] == 0 || a == 25)){
          for (int c = 1; a-b-c > 0 && c < 7; c++){
            if (c != b && (point[26+a-b] == 0) && point[26+a-b-c] == 1){
              indirectShot += (a-b-c)+blockadeCount;
            }
            if (c != b && (point[26+a-b] == 1 || point[26+a-c] == 1 )&& point[26+a-b-c] == 1){
              doubleShot += (a-b)+(a-b-c)+(blockadeCount*2);
            }
          }
        }
      }
    }

    for (int b = 1; b < 7; b++){
      for (int a = 2; (point[25] == 0 || point[51-b] < 2) && a < 26; a++){
        for (int c = 1; a-(b*c) > 0 && point[26+a-(b*c)] < 2 && c < 5; c++){
          if (c > 1 && point[a] > 0 && a-(b*c) > 0 && (point[25] <= (4-c) || (a == 25 && point[25] <= (5-c))) && point[26+a-(b*c)] < 2){
            if (point[26+a-(b*c)] == 1){
              luckShot += (a-(b*c)+blockadeCount)/c;
            }
          }
        }
      }
    }
    directShot *= 12;
    indirectShot *= 1;
    doubleShot *= 5;

    potentialHitBonus += directShot+indirectShot+doubleShot+luckShot;
    potentialHitBonus *= bonusNullifier*pipModifier;
    potentialHitBonus *= potentialHitBonusMultiplier;
    potentialHitBonus *= 1+(point[0]/3);
    potentialHitBonus /= potentialHitBonusDivisor;
  }
  //  */
  private void calcBestDiceBonuses () {

    bestDiceBonus = 0;

    int blockade;
    int temp;

    escapeDice = new int[]{0,0,0,0,0,0};

    for (int a = 24; point[25] < 2 && contact > 0 && point[25] == 0 && a > 18; a--){
      blockade = 0;
      if (point[a] > 0){
//  hvis man har en brik i position 19-24
        for (int b = 1; b < 17; b++){
          if (point[26+a-b] > 1){
//  hvis modparten har spærret en position 1-6 skridt frem
            blockade++;
          }
          if (point[a-b] > 1 && b < 7){
//  hvis man selv har en sikret position 1-6 skridt frem
            blockade = 0;
          }
        }
        for (int c = 1; bestDiceBonusMinNrOfBlockades < blockade && c < 7; c++){
          if (point[26+a-c] < 2){
            temp = blockade;
            if (temp > escapeDice[c-1]){
              escapeDice[c-1] = temp;
            }
          }
        }
      }
    }
    sort(escapeDice);

    for (int a = 0; contact > 0 && point[25] == 0 && a < 6; a++){
      for (int b = 3; b < 6; b++){
        if (escapeDice[a] != potentialHitDice[b] && escapeDice[a] > 0 && potentialHitDice[b] > 0){
          bestDiceBonus += a*b;
        }
        if (escapeDice[a] != toppingValue[b] && escapeDice[a] > 0 && toppingValue[b] > 0){
          bestDiceBonus += a*b;
        }
        if (potentialHitDice[a] != toppingValue[b] && toppingValue[b] > 0 && potentialHitDice[a] > 0){
          bestDiceBonus += a*b;
        }
      }
    }
    bestDiceBonus *= bestDiceBonusMulitplier;
    bestDiceBonus /= bestDiceBonusDivisor;
    bestDiceBonus /= (1+ point[25]);
  }

  private void calcBackAndKamikazeBonus () {

    kamikazeBonus = 0;
    backBonus = 0;
    pipModifier += 2;

    int backUpMultiplier = 1;

    int rearPos = rearPos();

    int foePointsFaced = 0;
    int foeFacedFromOnePoint = 0;


    for (int a = 25; foesAtHome > backAndKamikazeBonusEnoughFoesAtHome && a > 19; a--) {
      if (point[a] > 0){
// hvis man har 1 eller flere brikker i en position (24-19)
        for (int b = 1; b < 13; b++){
          if (point[26+a-b] > 0){
// og hvis opponenten har mindst 1 brik 1-10 positioner foran
            foePointsFaced++;
            if (b < 10){
              if (point[a] == 1 || a == 25){
                kamikazeBonus += (kamikazeBonusShortAdder+b)*kamikazeBonusShortMultiplier;
              } else {
                if (foePointsFaced == 1 && point[26+a-b] != 2){
                  foeFacedFromOnePoint++;
                }
                if (point[26+a-b] < backAndKamikazeBonusStackBackBonusLimit){
                  backBonus += (b*backBonusShortMultiplier)*(backBonusShortFewFoesMultiplier/ point[26+a-b]);
                } else {
                  backBonus += (b*backBonusShortMultiplier*backBonusShortManyFoesMultiplier)/(backBonusShortManyFoesDivisor* point[26+a-b]);
                }
              }
            } else {
              if (point[a] == 1 || a == 25){
                kamikazeBonus += (13-b)*kamikazeBonusLongMultiplier*(backBonusShortFewFoesMultiplier/ point[26+a-b]);
              } else {
                if (point[26+a-b] < backAndKamikazeBonusStackBackBonusLimit){
                  backBonus += (13-b)*kamikazeBonusLongMultiplier*(backBonusLongFewFoesMultiplier/ point[26+a-b]);
                } else {
                  backBonus += (13-b)*backBonusShortMultiplier*backBonusLongManyFoesMultiplier/(backBonusLongManyFoesDivisor* point[26+a-b]);
                }
              }
            }
          }
        }
      }
    }
    if (backBonus > 0 && kamikazeBonus > 0){
      for (int a = rearPos; a > 18; a++){
        if (point[a] > 1){
          backUpMultiplier++;
        }
        if (point[a] < 2){
          break;
        }
      }
    }
    kamikazeBonus *= pipModifier*backUpMultiplier;
    backBonus *= pipModifier;
    if (foePointsFaced == 1 && foeFacedFromOnePoint == 1){
      backBonus *= 0;
    }
    pipModifier = 5;
  }

  private void calcLuckyHoldBonus () {

    luckyHoldBonus = 0;

    if (point[26] > 0 && (blockadeCount == 4 || blockadeCount == 5) && chekkersAwayFromHome > 1 && point[26] < 4){
      for (int a = 1; a < 7; a++){
        if (point[a] == 0){
          for (int b = 2; b <= 5- point[26]; b++){
            if (point[a*b] > 1){
              luckyHoldBonus += (luckyHoldBaseBonus-b)*luckyHoldBonusMultiplier*pipModifier;
            }
          }
        }
      }
    }
  }

  private void calcHoldBonus () {

    holdBonus = 0;

    int pipModifierReducer = 0;

    pipModifier -= pipModifierReducer;

    if (pipModifier < 0){
      pipModifier = 0;
    }


    int holdBonusMultiplier = 20;

    for (int a = 10; foesAtHome > 8 && a < 19; a++) {
      if (point[a] > 1){
// hvis man har mindst 2 brikker i en position (10-18)
        for (int b = 1; b < 7; b++){
          if (point[26+a-b] > 0){
// og hvis opponenten har mindst 1 brik 1-6 positioner foran
            holdBonus += b;
//            holdBonus += (b > 7) ? b : 12 - b;
          }
        }
      }
    }
    holdBonus *= holdBonusMultiplier*pipModifier;
    pipModifier = 5;
  }

  private void calcAnchorBonus () {

    anchorBonus = 0;

    int bonus = 0;
    int pip2ModifierCount = 0;

    if (effectivePipPercentage <= anchorBonusPipTippingPoint){
      for (int a = anchorBonusLowSquare; a < anchorBonusHighSquare; a++){
        if (effectivePipPercentage <= anchorBonusPipTippingPoint-(int)Math.pow(a, anchorBonusPipPow)){
          pip2ModifierCount--;
        }
      }
    }

    for (int a = 24; a > 19; a--) {
      if (point[a] > 1){
// hvis man har mindst 2 brikker i en position (24-19)
        for (int b = 1; a-b > 18; b++){
          if (point[a-b] > 1){
// og hvis man har mindst 2 brikker i en position tæt foran (23-18)
            for (int c = 1; c < 7; c++){
              if (point[26+a-b-c] > 0){
// og hvis alle disse brikker har kontakt med opponenten
                bonus += 6-b;
                bonus += c;
                break;
              }
            }
          }
        }
      }
    }
    anchorBonus = bonus*(anchorBonusPip2Modifier-pip2ModifierCount)*anchorBonusMultiplier;
  }

  private void calcReserveBonus () {

    reserveBonus = 0;

    for (int a = 7; a < 12; a++){
      if (point[a] > 0){
        reserveBonus = (kamikazeBonus+backBonus+anchorBonus)/reserveBonusDivisor;
        break;
      }
    }
  }

  private void calcMidpointBonus () {

    midpointBonus = 0;

    int obstacles;

    for (int a = 1; contact > 0 && a < 13; a++){
      if (point[26+a] > 0){
        obstacles = 0;
        for (int b = 1; a+b < 26; b++){
          obstacles += point[a+b];
          if (point[26+a+b] > 1 || a+b == 25){
            midpointBonus += b > 12 ? point[26+a]*obstacles : 0;
            break;
          }
        }
      }
    }
    midpointBonus *= midpointBonusMultiplier;
  }

  private void calcTrapBonus () {

    trapBonus = 0;

    boolean cantEscapeByOnePartMove;
    boolean cantEscapeVia6;
    boolean cantEscapeVia5;
    boolean cantEscapeVia4;

    int trapCount;
    int trappedFoes = 0;
    int pip2Modifier = pipModifier;
    pip2Modifier -= trapBonusPipModifierReducer;

    if (pip2Modifier < 0){
      pip2Modifier = 0;
    }
    if (pip2Modifier > trapBonusMaxPipModifier){
      pip2Modifier = trapBonusMaxPipModifier;
    }

    for (int a = 1; foesInvading > 0 && a < 7; a++){
      if (point[26+a] > 0){
        cantEscapeByOnePartMove = true;
        cantEscapeVia6 = true;
        cantEscapeVia5 = true;
        cantEscapeVia4 = true;
        for (int b = 1; b < 7; b++){
          if (a+b > 6 && point[a+b] < 2){
            cantEscapeByOnePartMove = false;
            if (b == 6){
              cantEscapeVia6 = false;
            }
            if (b == 5){
              cantEscapeVia5 = false;
            }
            if (b == 4){
              cantEscapeVia4 = false;
            }
          }
        }
        trapCount = 0;
        for (int b = 1; a+b < 12; b++){
          if (point[a+b] > 1){
            trapCount += (b < 7) ? b : 12-b;
          }
        }
        if (cantEscapeByOnePartMove){
          trapBonus += point[26+a]*trapCount*66;
        }
        if (cantEscapeVia6){
          trapBonus += point[26+a]*trapCount*16;
        }
        if (cantEscapeVia5){
          trapBonus += point[26+a]*trapCount*15;
        }
        if (cantEscapeVia4){
          trapBonus += point[26+a]*trapCount*14;
        }
      }
    }
    trapBonus *= trapBonusMultiplier;
    trapBonus *= pip2Modifier;
    trapBonus /= trapBonusDivisor;
    trapBonus /= (chekkersAtHome/2);

    if (trappedFoes > 1){
//      trapBonus /= trappedFoes-1;
    }
  }

  private void calcSqueezeBonus () {

    squeezeBonus = 0;

    int foeRearPoint = 0;
    int foeLegalPartMoves;
    int squeezeFactor = 0;
    int contactModifier;

    if (contact >= squeezeBonusContactLimit){
      contactModifier = squeezeBonusContactLimit;
    } else {
      contactModifier = contact;
    }

    for (int a = 25; a > 0; a--){
      if (point[51-a] > 0){
        foeRearPoint = a;
        break;
      }
    }

    for (int b = 1; b < 7; b++){
      foeLegalPartMoves = 0;
      for (int a = foeRearPoint; a > 1; a--){
        if ((point[26] == 0 || a == 25) && point[51-a] > 0 && ((a-b > 0 && point[a-b] < 2) || ((a-b <= 0 && a == foeRearPoint) || (foeRearPoint < 7 && a-b == 0)))){
//          if (point[51-a] > 0 && (a-b > 0 || a == foeRearPoint || (foeRearPoint < 7 && a-b == 0) && point[a-b] < 2)){
          foeLegalPartMoves += point[51-a];
        }
      }
      if (foeLegalPartMoves < squeezeLimit){
        squeezeFactor = squeezeLimit-foeLegalPartMoves;
        if (squeezeFactor == 4){
//          squeezeFactor = 1;
        }
      }
      squeezeBonus += (int)Math.pow(squeezeFactor, squeezeBonusPow)*contactModifier*squeezeBonusMultiplier;
    }
  }

  private void calcReverseBarBonus () {

    reverseBarBonus = 0;

    flipLayout();
    calcBonusNullifier();
    reverseBarBonus = point[26]*reverseBarPip*(1-bonusNullifier)*reverseBarBonusMultiplier;
    flipLayout();
    calcBonusNullifier();
//  System.out.println("reverseBarBonus = "+reverseBarBonus);
  }

  private void calcTotalBonus () {

    totalBonus = 0;
    totalDoubleBonus = 0;

    checkPoints();
    contact();
    reverseContact();
    pipModifier();
    gammonPercentage();
    bGLoosePercentage();
    bGWinPercentage();
    if (contact > 0){
      calcBestDiceBonuses();
      calcBonusNullifier();
      calcSqueezeBonus();
      calcBuildAndFlexBonus();
      calcHalfPrimeBonus();
      calcPrimeBonus();
      calcBarBonus();
      calcReverseBarBonus ();
      calcPotentialHitBonus();
      calcDeadFoesBonus();
      calcBackAndKamikazeBonus();
      calcAnchorBonus();
      calcHoldBonus();
      calcLuckyHoldBonus();
      calcSingleBonus();
      calcTrapBonus();
      calcReserveBonus();
      calcMidpointBonus();
      calcHomeCommingBonus();
    }
    calcRunningBonus();
    calcRunningWildBonus();
    calcCheckkingOutBonus();
    calcGammonBonus();
    calcBGBonus();
    calcStackBonus();

    if (contact > 0){
      totalBonus =          primeBonus+halfPrimeBonus+squeezeBonus+holdBonus+luckyHoldBonus+trapBonus+buildBonus+flexBonus+midpointBonus+singleBonus+
        homeCommingBonus;
      totalDoubleBonus =    primeBonus+halfPrimeBonus+squeezeBonus+holdBonus+luckyHoldBonus+trapBonus+buildBonus+flexBonus+midpointBonus+singleBonus;
/*
      totalBonus *= (15-point[51]);
      totalDoubleBonus *= (15-point[51]);
      totalBonus /= 15;
      totalDoubleBonus /= 15;
*/
      totalBonus +=         kamikazeBonus+backBonus+anchorBonus+reserveBonus+barBonus+reverseBarBonus;
      totalDoubleBonus +=   kamikazeBonus+backBonus+anchorBonus+reserveBonus+barBonus+reverseBarBonus;
    }
    totalBonus +=           stackBonus+gammonBonus+bGBonus+checkingOutBonus+runningBonus+runningWildBonus+deadFoesBonus+deadFoesBonusPoint123;
    totalDoubleBonus +=     stackBonus+gammonBonus+bGBonus+checkingOutBonus+runningBonus+runningWildBonus+deadFoesBonus+deadFoesBonusPoint123;
//    totalBonus +=           stackBonus+gammonBonus+bGBonus+runningWildBonus;
//    totalDoubleBonus +=     stackBonus+gammonBonus+bGBonus+runningWildBonus;
    totalBonus +=           point[0]*100;
    totalDoubleBonus +=     point[0]*100;
/*
    if (contact == 0){
      totalBonus /= totalBonusDivisor;
      totalDoubleBonus /= totalBonusDivisor;
    }
*/
  }

  private void calcTotalBonusPercentage () {

    totalBonusPercentage = 0;
    totalDoubleBonusPercentage = 0;

    flipLayout ();
    onRoll = 1;
    calcTotalBonus ();
//    System.out.println("foe totalBonus = "+totalBonus);

    if (displayingMove) {
      foeValues = getFieldValues();
    }

    int foeTotalBonus =         totalBonus+potentialHitBonus+bestDiceBonus;
    int foeDoubleTotalBonus =   totalDoubleBonus+potentialHitBonus+bestDiceBonus;

    flipLayout ();
    onRoll = 0;
    calcTotalBonus ();
//    System.out.println("player totalBonus = "+totalBonus);

    potentialHitBonus /= potentialHitBonusTurnDivisor;

    totalBonus +=       potentialHitBonus+bestDiceBonus;
    totalDoubleBonus += potentialHitBonus+bestDiceBonus;

    if (totalBonus < 1){
      totalBonus = 1;
    }
    if (totalDoubleBonus < 1){
      totalDoubleBonus = 1;
    }
    totalBonusPercentage = (int)(((float)(totalBonus)/(float)(totalBonus+foeTotalBonus))*10000);
    totalDoubleBonusPercentage = (int)(((float)(totalDoubleBonus)/(float)(totalDoubleBonus+foeDoubleTotalBonus))*10000);
  }

  public void calcLayoutStrength () {

    calcTotalBonusPercentage();
    layoutStrength = 0;
    layoutDoubleStrength = 0;
    doubble = false;
    doubleTake = false;

    int pipStrengthMultiplier = 3;
    int BonusPercentageMultiplier = 3;
    int pipMultiplierPow = 2;
    int exitDivisor = 6;

    if (contact < 2){
      pipStrengthMultiplier += (int)Math.pow((3-contact), pipMultiplierPow);
//      pipMultiplier += (3-contact)*(3-contact);
    }
    pipStrengthMultiplier += (point[0]+ point[51])/exitDivisor;
    pipStrengthMultiplier += 2;

    int pipStrength = 100-effectivePipPercentage;

    if (pipStrength > 50 && point[0] > 0){
      pipStrength += (((100-pipStrength)/15)* point[0])/2;
    }
    if (pipStrength < 50 && point[51] > 0){
      pipStrength -= ((pipStrength/15)* point[51])/2;
    }
    pipStrength *= 100;

    int singleChekkers = 0;

    if (chekkersAtHome == 15 && contact == 0) {
      for (int a = 1;  a < 7; a++){
        singleChekkers += point[a] == 1 ? 1 : 0;
      }
    }

    layoutStrength =
      ((pipStrength*pipStrengthMultiplier)+
      ((singleChekkers+1)*
      (totalBonusPercentage*BonusPercentageMultiplier)))/
      (BonusPercentageMultiplier+pipStrengthMultiplier+singleChekkers);

    layoutDoubleStrength =
      ((pipStrength*pipStrengthMultiplier)+
      ((singleChekkers+1)*
      (totalDoubleBonusPercentage*BonusPercentageMultiplier)))/
      (BonusPercentageMultiplier+pipStrengthMultiplier+singleChekkers);

/*
    if (bonusNullifier == 0){
      layoutStrength = ((totalBonusPercentage*(trapStrengthMultiplier-1))+(effectivePipPercentage*100))/trapStrengthMultiplier;
      layoutDoubleStrength = ((totalBonusPercentage*(trapStrengthMultiplier-1))+(effectivePipPercentage*100))/trapStrengthMultiplier;
    } else {
      if (contact > 0){
        layoutStrength = (pipStrength*100)+(totalBonusPercentage)/2;
        layoutDoubleStrength = (pipStrength*100)+(totalBonusPercentage)/2;
      } else {
        layoutStrength = pipStrength*100;
        layoutDoubleStrength = pipStrength*100;

        layoutStrength = ((totalBonusPercentage)+((10000-(pipStrength*100)))*9)/10;
        layoutDoubleStrength = ((totalDoubleBonusPercentage)+((10000-(pipStrength*100)))*9)/10;
      }
    }
*/

    if (foePip < 4 && bGPip > 0){
      layoutStrength = 0;
      layoutDoubleStrength = 0;
    }

    int gammonLooseModifier = gammonLoosePercentage*gammonLoosePercentageModifierMultiplier;
    int gammonWinModifier = gammonWinPercentage*gammonWinPercentageModifierMultiplier;

    if (layoutDoubleStrength > 2800+gammonLooseModifier-gammonWinModifier && layoutDoubleStrength < 3800){
      doubble = true;
    }
    if (layoutDoubleStrength > 3500+gammonLooseModifier-gammonWinModifier){
      doubleTake = true;
    }
    if (displayingMove) {
      bonusValues = getFieldValues();
    }
  }

  /**
   * Author: Sebastian Tue Øltieng 12.06.2017:
   *
   * Returns an integer denoting player strength as a percent
   * expression:
   *
   * The value: 5223
   *
   * denotes an estimated strength of 52.23% vs the opponent, which
   * in turn is estimated to have a strength of:
   *
   * (10000-5223)/100 = 47,77% against the evaluated player.
   *
   * @return int
   */

  public int getLayoutStrength () {

    if (!bonusCalculated) {
      bonusCalculated = true;
      calcLayoutStrength();
//      System.out.println("getLayoutStrength returning = "+layoutStrength);
    }
    return layoutStrength;
  }

  public int getProbabilityAdjustedLayoutStrength () {

    return (dice.areDouble() ? 1 : 2)*getLayoutStrength();
  }

  public boolean shouldTake () {

    return doubleTake;
  }

  public boolean shouldDouble () {

    return doubble;
  }

  public void initBonusValues () {

    if (!displayingMove) {
      displayingMove = true;
      totalBonusPercentage = -1;
      layoutStrength = -1;
      bonusTexts = getFieldNamesAndSetPositions();
      calcLayoutStrength();
    }
  }

}
