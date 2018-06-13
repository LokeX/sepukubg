package bg.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListUtil {

  static public <T> List<T> listInRange(

    List<T> list,
    int startPosition,
    int endPosition) {

    return
      IntStream.range(startPosition, endPosition)
        .mapToObj(list::get)
        .collect(Collectors.toList());
  }

}
