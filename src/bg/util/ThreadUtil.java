package bg.util;

public class ThreadUtil {
  
  static public void threadSleep (long m) {

    try {
      Thread.sleep(m);
    } catch (InterruptedException ie) {
      System.out.println(ie.getMessage());
    }
  }
  
  static public Object runWhenNotified (final Runnable runner) {
    
    final Object notifier = new Object();
    
    new Thread(() -> {

      try {
        synchronized (notifier) {
          notifier.wait();
        }
      } catch (InterruptedException ie) {
        System.out.println(ie.getMessage());
      }
      runner.run();
    }).start();
    return notifier;
  }
}
