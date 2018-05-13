package bg.util;

import java.awt.Container;
import javax.swing.JOptionPane;

public class Dialogs {

  static public void showMessage (String message, Container container) {

    JOptionPane.showMessageDialog(
    container,
    message,
    "Be advised",
    JOptionPane.INFORMATION_MESSAGE
    );
  }

  static public boolean confirmed (String questionText, Container container) {

  String[] options = new String[] {"Yes", "No"};
  String header = "Please advice";

 return
   JOptionPane.showOptionDialog(
   container,
   questionText,
   header,
   JOptionPane.OK_CANCEL_OPTION,
   JOptionPane.INFORMATION_MESSAGE,
   null,
   options,
   options[0]) == JOptionPane.OK_OPTION;
 }

  public static int getIntegerInput (String message, Container container) {

    int convertedInput = -1;
    String inputString = JOptionPane.showInputDialog(container,message);

    if (inputString != null) {
      try {
        convertedInput = NumberUtil.parseInt(inputString);
      } catch (NumberFormatException nfe) {
        System.out.println(nfe.getMessage());
      }
    }
    return convertedInput;
  }

}
