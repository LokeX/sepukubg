package bg.inUrFace.windows;

import bg.engine.trainer.Bot;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static bg.util.NumberUtil.parseInt;

public class BotEditor extends TextDisplay {

  private Bot bot;
  private String originalText;

  public BotEditor (Bot bot) {

    super("Edit bot: ", bot.getFields());
    this.bot = bot;
    originalText = textArea.getText();
    textArea.setEditable(true);
    addWindowListener(onWindowEvent());
    setVisible(true);
  }

  private WindowAdapter onWindowEvent() {

    return new WindowAdapter () {
      @Override
      public void windowClosing (WindowEvent e) {
        if (!originalText.equals(textArea.getText())) {
          confirmApplication();
        }
      }
    };
  }

  private int parseValue(String str) {

    return parseInt(
      str.substring(
        str.lastIndexOf("=")+1, str.length()
      )
    );
  }

  private List<Integer> getParsedValues() {

    List<Integer> values = new ArrayList<>();
    String text = textArea.getText();
    int index;

    text = text.substring(0,text.length()-1);
    index = text.lastIndexOf("\n");
    while (index != -1) {
      values.add(0,
        parseValue(
          text.substring(
            index+1, text.length()
          )
        )
      );
      text = text.substring(0, index);
      index = text.lastIndexOf("\n");
    }
    values.add(0, parseInt(text));
    return values;
  }

  private void confirmApplication () {

    if (bg.util.Dialogs.confirmed(
      "Apply changes to bot:\n"+bot.name(),
      this)) {
      try {
        bot.applyValues(getParsedValues());
      } catch (IllegalAccessException iae) {
        System.out.println(iae.getMessage());
        bg.util.Dialogs.showMessage(
          "An error has occurred!",
          this
        );
      }
    }
  }

}
