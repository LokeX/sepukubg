package bg.engine.coreLogic.moves;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveBonuses {

  private List<String> bothPlayersBonusesList;
  private List<String> onRollBonusesList;
  private List<String> foeBonusesList;
  private EvaluatedMove move;
  private int format;

  MoveBonuses (EvaluatedMove move) {

    this.move = move;
    move.initBonusValues();
  }

  private String bonus (int bonus) {

    return bonus > -1 ? Integer.toString(bonus) : "N/A";
  }

  private String bonusWithTitle (int index, int bonus) {

    return move.getBonusTexts().get(index)
      + ": " + bonus(bonus);
  }

  private String onRollBonus (int index) {

    return bonusWithTitle(index, move.getBonusValues().get(index));
  }

  private Stream<String> onRollBonuses  () {

    return range(0, move.getBonusTexts().size())
      .mapToObj(this::onRollBonus);
  }

  private String foeBonus (int index) {

    return bonusWithTitle(index, move.getFoeValues().get(index));
  }

  private Stream<String> foeBonuses () {

    return range(0, move.getBonusTexts().size())
      .mapToObj(this::foeBonus);
  }

  private Stream<String> bothBonuses () {

    return range(0, move.getBonusTexts().size())
      .mapToObj(index ->
        onRollBonus(index)
          + " / "
          + bonus(move.getFoeValues().get(index))
      );
  }

  private String bonusHeader () {

    final String[] bonusHeaders = new String[] {
      "Listing both players bonuses:",
      "Listing moving players bonuses:",
      "Listing opponents bonuses:",
      "Listing no bonuses",
    };

    return bonusHeaders[format];
  }

  private List<String> bonusesAsList (Stream<String> bonuses) {

    List<String> bonusList = new ArrayList<>();

    bonusList.add(bonusHeader());
    bonusList.add("");
    bonusList.addAll(streamAsList(bonuses));
    return bonusList;
  }

  private List<String> bothPlayersBonusesList () {

    if (bothPlayersBonusesList == null) {
      bothPlayersBonusesList =
        new ArrayList<>(
          bonusesAsList(bothBonuses())
        );
    }
    return bothPlayersBonusesList;
  }

  private List<String> onRollBonusesList () {

    if (onRollBonusesList == null) {
      onRollBonusesList =
        new ArrayList<>(
          bonusesAsList(onRollBonuses())
        );
    }
    return onRollBonusesList;
  }

  private List<String> foeBonusesList () {

    if (foeBonusesList == null) {
      foeBonusesList =
        new ArrayList<>(
          bonusesAsList(foeBonuses())
        );
    }
    return foeBonusesList;
  }

  public List<String> getMoveBonusList (int format) {

    final int BOTH_BONUSES = 0;
    final int ON_ROLL_BONUSES = 1;
    final int FOE_BONUSES = 2;

    this.format = format;

    return
      switch (format) {
        case BOTH_BONUSES -> bothPlayersBonusesList();
        case ON_ROLL_BONUSES -> onRollBonusesList();
        case FOE_BONUSES -> foeBonusesList();
        default -> new ArrayList<>();
      };
    //Display no bonuses
  }

}
