package com.github.tommwq.applogmanagement.http;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class LogHeaderHttp {

  private long sequence;
  private long time;
  private int logType;

  public long getSequence() {
    return sequence;
  }

  public void setSequence(long value) {
    sequence = value;
  }
  public long getTime() {
    return time;
  }

  public void setTime(long value) {
    time = value;
  }
  public int getLogType() {
    return logType;
  }

  public void setLogType(int value) {
    logType = value;
  }
}
