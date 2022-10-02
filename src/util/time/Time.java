package util.time;

public class Time {

  public static String getConvertedTime(long timeInMillis) {

    if (timeInMillis >= 0) {

      Integer sec = (int)(timeInMillis/1000);
      Integer hour = sec/3600;
      Integer min = (sec - (hour*3600))/60;
      String m = min.toString();

      m = m.length() < 2 ? "0"+m : m;
      if (hour == 0) {
        sec = sec - ((min*60)+(hour*3600));
        String s = sec.toString();
        s = s.length() < 2 ? "0"+s : s;
        return "[mm:ss]: "+m+":"+s;
      } else {
        String h = hour.toString();
        h = h.length() < 2 ? "0"+h : h;
        return "[hh:mm]: "+h+":"+m;
      }
    } else
      return "";
  }

}
