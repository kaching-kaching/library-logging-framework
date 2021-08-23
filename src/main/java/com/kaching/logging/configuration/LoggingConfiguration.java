package com.kaching.logging.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.kaching.logging")
public class LoggingConfiguration {

  @Value("${logging.type}")
  private String type;

  @Value("${logging.app-name}")
  private String appName;

  @Value("${logging.app-code}")
  private String appCode;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppCode() {
    return appCode;
  }

  public void setAppCode(String appCode) {
    this.appCode = appCode;
  }
}
