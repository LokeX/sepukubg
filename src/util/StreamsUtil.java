package util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsUtil {

  static public <T> List<T> streamAsList(Stream<T> tStream) {

    return tStream.collect(Collectors.toList());
  }


}
