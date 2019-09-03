package com.github.tommwq.applogmanagement.http;

public class CommandHttp {

  private String deviceId;
  private long sequence;
  private int count;
  private String packageName;
  private boolean includeSubPackage;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String value) {
    deviceId = value;
  }
  public long getSequence() {
    return sequence;
  }

  public void setSequence(long value) {
    sequence = value;
  }
  public int getCount() {
    return count;
  }

  public void setCount(int value) {
    count = value;
  }
  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String value) {
    packageName = value;
  }
  public boolean getIncludeSubPackage() {
    return includeSubPackage;
  }

  public void setIncludeSubPackage(boolean value) {
    includeSubPackage = value;
  }
}
