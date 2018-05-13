package bg.engine.trainer;

import bg.engine.moves.BonusElements;
import java.lang.reflect.Field;
import java.util.List;

public class Bot extends BonusElements {

  private String name;
  private BotStats stats;

  public Bot (String name) {

    this.name = name;
    stats = new BotStats(name);
  }

  public Bot (Bot bot) {

    this(bot.getName());
//    stats = new BotStats(bot);
  }

  public String getName () {

    return name;
  }

  public void setName (String name) {

    this.name = name;
  }

  public BotStats getStats() {

    return stats;
  }

  public void setStats (BotStats botStats) {

    stats = botStats;
  }


  public String getFields () {

    Field[] fields = BonusElements.class.getDeclaredFields();
    String elements = "";

    for (Field field : fields) {
      try {
        elements += field.getName() + " = " + field.get(this) + "\n";
      } catch (IllegalAccessException iae) {
        iae.getMessage();
      }
    }
    return elements;
  }

  public void applyValues (List<Integer> values) throws IllegalAccessException {

    Field[] fields = BonusElements.class.getDeclaredFields();

    for (int a = 0; a < fields.length; a++) {
      try {
        fields[a].set(this, values.get(a));
      } catch (IllegalAccessException iae) {
        throw new IllegalAccessException(iae.getMessage());
      }
    }
  }

  public void printBonusFields () {

    System.out.println("Bonus elements: ");
    System.out.println(getFields());
  }


}
