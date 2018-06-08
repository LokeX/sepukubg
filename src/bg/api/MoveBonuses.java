package bg.api;

import bg.Settings;
import bg.engine.moves.EvaluatedMove;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static bg.util.StreamsUtil.streamAsList;
import static java.util.stream.IntStream.range;

public class MoveBonuses {

  private final static int NO_HEADER = 0;
  private final static int WITH_HEADER = 0;

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

    return bonusHeaders[DISPLAY_MODE];
  }

  private List<String> bonusesAsList (Stream<String> bonuses, int headerUse) {

    List<String> bonusList = new ArrayList<>();

    if (headerUse != NO_HEADER) {
      bonusList.add(bonusHeader());
      bonusList.add("");
    }
    bonusList.addAll(streamAsList(bonuses));
    return bonusList;
  }

  public List<String> bothPlayersBonusesList (int headerUse) {

    return bonusesAsList(bothBonuses(), headerUse);
  }

  public List<String> onRollBonusesList (int headerUse) {

    return bonusesAsList(onRollBonuses(), headerUse);
  }

  public List<String> foeBonusesList (int headerUse) {

    return bonusesAsList(foeBonuses(), headerUse);
  }

  public List<String> getMoveBonusList (int headerUse) {

    final int BOTH_BONUSES = 0;
    final int ON_ROLL_BONUSES = 1;
    final int FOE_BONUSES = 2;

    switch (DISPLAY_MODE) {
      case BOTH_BONUSES    : return bothPlayersBonusesList(headerUse);
      case ON_ROLL_BONUSES : return onRollBonusesList(headerUse);
      case FOE_BONUSES     : return foeBonusesList(headerUse);
    }
    return new ArrayList<>(); //Display no bonuses
  }

  public List<String> getMoveBonusList () {

    return getMoveBonusList(WITH_HEADER);
  }

}
