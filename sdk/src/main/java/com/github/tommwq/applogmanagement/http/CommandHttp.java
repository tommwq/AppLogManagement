package com.github.tommwq.applogmanagement.http;

public class CommandHttp {

  private String DeviceId;
  private long Sequence;
  private int Count;
  private String PackageName;
  private boolean IncludeSubPackage;

  public String getDeviceId() {
    return DeviceId;
  }

  public void setDeviceId(String value) {
    DeviceId = value;
  }
  public long getSequence() {
    return Sequence;
  }

  public void setSequence(long value) {
    Sequence = value;
  }
  public int getCount() {
    return Count;
  }

  public void setCount(int value) {
    Count = value;
  }
  public String getPackageName() {
    return PackageName;
  }

  public void setPackageName(String value) {
    PackageName = value;
  }
  public boolean getIncludeSubPackage() {
    return IncludeSubPackage;
  }

  public void setIncludeSubPackage(boolean value) {
    IncludeSubPackage = value;
  }
}
