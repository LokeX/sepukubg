package util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class Reflection {
  
  static public <T> List<T> getFieldsList (Object obj, List<T> tList) {
    
    Field[] fields = obj.getClass().getDeclaredFields();
    
    for (Field field : fields) {
      try {
        if (!Modifier.isPrivate(field.getModifiers())) {
          tList.add((T)field.get(obj));
        }
      } catch (IllegalArgumentException | IllegalAccessException ex) {
        System.out.println(ex.getMessage());
      }       
    }
    return tList;
  }
  
}
