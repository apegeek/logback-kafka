package com.github.ptgoetz.logback.kafka.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;

public class JsonFormatter implements Formatter {
  private static final char QUOTE = '"';
  private static final char COLON = ':';
  private static final char COMMA = ',';

  private boolean expectJson = false;
  private boolean includeMethodAndLineNumber = false;
  private String extraProperties = null;

  public String format(ILoggingEvent event) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    fieldName("level", sb);
    quote(event.getLevel().levelStr, sb);
    sb.append(COMMA);
    fieldName("logger", sb);
    quote(event.getLoggerName(), sb);
    sb.append(COMMA);
    fieldName("timestamp", sb);
    sb.append(event.getTimeStamp());
    sb.append(COMMA);
    fieldName("message", sb);
    if (this.expectJson) {
      sb.append(event.getFormattedMessage());
    } else {
      quote(event.getFormattedMessage(), sb);
    }
    if(includeMethodAndLineNumber) {
      sb.append(COMMA);
      // Caller Data
      StackTraceElement[] callerDataArray = event.getCallerData();
      if (callerDataArray != null && callerDataArray.length > 0) {
        StackTraceElement stackTraceElement = callerDataArray[0];
        fieldName("method", sb);
        quote(stackTraceElement.getMethodName(), sb);
        sb.append(COMMA);
        fieldName("lineNumber", sb);
        quote(stackTraceElement.getLineNumber() + "", sb);
      }
    }
    if(this.extraProperties!=null){
      sb.append(this.extraProperties);
    }
    sb.append("}");
    return sb.toString();
  }

  private static void fieldName(String name, StringBuilder sb) {
    quote(name, sb);
    sb.append(COLON);
  }

  private static void quote(String value, StringBuilder sb) {
    sb.append(QUOTE);
    sb.append(value);
    sb.append(QUOTE);
  }

  public boolean getExpectJson() {
    return expectJson;
  }

  public void setExpectJson(boolean expectJson) {
    this.expectJson = expectJson;
  }

  public boolean getIncludeMethodAndLineNumber() {
    return includeMethodAndLineNumber;
  }

  public void setIncludeMethodAndLineNumber(boolean includeMethodAndLineNumber) {
    this.includeMethodAndLineNumber = includeMethodAndLineNumber;
  }

  public String getExtraProperties() {
    return extraProperties;
  }

  public void setExtraProperties(String thatExtraProperties) {
    final Properties properties = new Properties();
    try {
      properties.load(new StringReader(thatExtraProperties));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Enumeration<?> enumeration = properties.propertyNames();
    StringBuilder sb = new StringBuilder();
    while(enumeration.hasMoreElements()){
      String name = (String)enumeration.nextElement();
      String value = properties.getProperty(name);
      sb.append(COMMA);
      fieldName(name, sb);
      quote(value,sb);
    }
    this.extraProperties = sb.toString();
  }
}