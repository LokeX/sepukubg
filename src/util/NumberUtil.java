package util;

import java.text.DecimalFormat;

public class NumberUtil {

  public static int parseInt (String intStr) {

    String tempStr = intStr.replaceAll("[\\D]", "");

    if (tempStr.length() == 0) {
      return -1;
    } else {
      return Integer.parseInt(tempStr);
    }
  }

  public static DecimalFormat getDottedDecimalFormat () {

    return new DecimalFormat("#,#00");
  }

}
