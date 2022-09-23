package bg.inUrFace.menu.dropMenues.trainingMenu;

import bg.engine.core.trainer.Bot;
import bg.engine.core.trainer.Trainer;
import bg.inUrFace.windows.TextDisplay;
import bg.util.menus.Listable;

import java.util.List;
import java.util.stream.Collectors;

public class BotReportMenu implements Listable {

  private int selectedItemNr = Trainer.blackBot;

  @Override
  public void itemSelectedAction(int selectedItemNr) {

    Bot bot = Trainer.bots.get(selectedItemNr);

    TextDisplay.displayReport(
      "Statistical report: "+bot.name(),
      bot.getStats().getStatReport()
    );
//    new BotReport(Trainer.bots.get(selectedItemNr));
  }

  @Override
  public int getSelectedItemNr() {

    return selectedItemNr;
  }

  @Override
  public String getMenuTitle() {

    return "Bot report";
  }

  @Override
  public List<String> getElementTitles() {

    return Trainer.bots.stream().
      map(Bot::name).collect(Collectors.toList());
  }

}
