package com.github.tommwq.applogmanagement.http;

public class LogHeaderHttp {

  private long Sequence;
  private long Time;
  private int LogType;

  public long getSequence() {
    return Sequence;
  }

  public void setSequence(long value) {
    Sequence = value;
  }
  public long getTime() {
    return Time;
  }

  public void setTime(long value) {
    Time = value;
  }
  public int getLogType() {
    return LogType;
  }

  public void setLogType(int value) {
    LogType = value;
  }
}
