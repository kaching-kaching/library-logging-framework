package com.kaching.logging;

import com.kaching.logging.util.LoggingDateUtil;
import lombok.Data;

@Data
public class LogEntry {
  private String appCode;
  private String timeStamp;
  private String type;
  private String serviceId;
  private String requestType;
  private String msgUid;
  private String clientIp;
  private String statusCode;
  private String errorClassification;
  private String errorType;
  private Long executionTime;
  private String requestPath;
  private String traceId;
  private String status;
  private String userId;

  public static LogEntry initializedWithDefault() {
    LogEntry logEntry = new LogEntry();
    logEntry.updateTimeStamp();
    return logEntry;
  }

  public void updateTimeStamp() {
    this.setTimeStamp(LoggingDateUtil.getTimeLogging());
  }
}
