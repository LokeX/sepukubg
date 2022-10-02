package util;

public class Layers {
  
  protected static int activeLayer = 0;
  
  public static int getActiveLayer () {
    
    return activeLayer;
  }
  
  public static int getNewActiveLayer (){
    
    return ++activeLayer;
  }
  
  public static void removeActiveLayer () {
    
    if (--activeLayer < 0) {
      activeLayer++;
    }
  }
}
