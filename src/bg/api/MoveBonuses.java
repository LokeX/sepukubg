package bg.api;

import bg.Settings;
import bg.engine.moves.EvaluatedMove;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveBonuses {

  private final int DISPLAY_MODE;

  private EvaluatedMove move;

  public MoveBonuses (Settings settings, EvaluatedMove move) {

    DISPLAY_MODE = settings.getBonusDisplayMode();
    this.move = move;
    move.initBonusValues();
  }

  private String bonus (int bonus) {

    return bonus > -1 ? Integer.toString(bonus) : "N/A";
  }

  private String bonusWithHeader (int index, int bonus) {

    return move.getBonusTexts().get(index)
      + ": " + bonus(bonus);
  }

  private String onRollBonus (int index) {

    return bonusWithHeader(index, move.getBonusValues().get(index));
  }

  private Stream<String> onRollBonuses  () {

    return range(0, move.getBonusTexts().size())
      .mapToObj(this::onRollBonus);
  }

  private String foeBonus (int index) {

    return bonusWithHeader(index, move.getFoeValues().get(index));
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

    return bonusHeaders[DISPLAY_MODE];
  }

  private List<String> bonusesAsList (Stream<String> bonuses) {

    List<String> bonusList = new ArrayList<>();

    bonusList.add(bonusHeader());
    bonusList.add("");
    bonusList.addAll(streamAsList(bonuses));

    return bonusList;
  }

  public List<String> bothPlayersBonusesList () {

    return bonusesAsList(bothBonuses());
  }

  public List<String> onRollBonusesList () {

    return bonusesAsList(onRollBonuses());
  }

  public List<String> foeBonusesList () {

    return bonusesAsList(foeBonuses());
  }

  public List<String> getMoveBonusList () {

    final int BOTH_BONUSES = 0;
    final int ON_ROLL_BONUSES = 1;
    final int FOE_BONUSES = 2;

    switch (DISPLAY_MODE) {
      case BOTH_BONUSES    : return bothPlayersBonusesList();
      case ON_ROLL_BONUSES : return onRollBonusesList();
      case FOE_BONUSES     : return foeBonusesList();
    }
    return new ArrayList<>(); //Display no bonuses
  }

}
