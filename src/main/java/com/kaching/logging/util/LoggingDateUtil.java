package com.kaching.logging.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingDateUtil {

  private static String samplePattern = "yyyy-MM-dd HH:mm:ss.SSS";

  public static String getTimeLogging() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(samplePattern));
  }
}
