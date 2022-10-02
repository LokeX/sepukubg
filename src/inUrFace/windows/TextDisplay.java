package inUrFace.windows;

import javax.swing.*;
import java.awt.*;

import static sepuku.App.win;

public class TextDisplay extends JFrame {

  JTextArea textArea;
  JScrollPane scrollPane;

  public TextDisplay() {

    this("", "");
  }

  public TextDisplay(String title) {

    this(title, "");
  }

  public TextDisplay(String title, String text) {

    setIconImage(new ImageIcon(getClass().getResource("Icon/AppIcon.gif")).getImage());
    setTitle(title);
    textArea = new JTextArea(20,40);
    textArea.setMargin(new Insets(20,20,20,20));
    textArea.setEditable(false);
    textArea.setFont(new Font("Ariel", Font.PLAIN,18));
    textArea.setText(text);
    scrollPane = new JScrollPane(textArea);
    scrollPane.setBorder(
      BorderFactory.
        createEmptyBorder(5,5,5,5)
    );
    add(scrollPane);
    pack();
    setLocationRelativeTo(win);
  }

  static public void displayReport (String report) {

    displayReport("Report", report);
  }

  static public void displayReport (String title, String report) {

    TextDisplay textDisplay = new TextDisplay(title, report);

    textDisplay.setVisible(true);
  }

}
