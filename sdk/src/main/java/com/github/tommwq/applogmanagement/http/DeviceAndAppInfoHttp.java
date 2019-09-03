package com.github.tommwq.applogmanagement.http;

public class DeviceAndAppInfoHttp {

  private String DeviceId;
  private String DeviceVersion;
  private String BaseOsName;
  private String BaseOsVersion;
  private String OsName;
  private String OsVersion;
  private String AppVersion;
  private java.util.List<com.github.tommwq.applogmanagement.http.ModuleInfoHttp> ModuleInfo;

  public String getDeviceId() {
    return DeviceId;
  }

  public void setDeviceId(String value) {
    DeviceId = value;
  }
  public String getDeviceVersion() {
    return DeviceVersion;
  }

  public void setDeviceVersion(String value) {
    DeviceVersion = value;
  }
  public String getBaseOsName() {
    return BaseOsName;
  }

  public void setBaseOsName(String value) {
    BaseOsName = value;
  }
  public String getBaseOsVersion() {
    return BaseOsVersion;
  }

  public void setBaseOsVersion(String value) {
    BaseOsVersion = value;
  }
  public String getOsName() {
    return OsName;
  }

  public void setOsName(String value) {
    OsName = value;
  }
  public String getOsVersion() {
    return OsVersion;
  }

  public void setOsVersion(String value) {
    OsVersion = value;
  }
  public String getAppVersion() {
    return AppVersion;
  }

  public void setAppVersion(String value) {
    AppVersion = value;
  }
  public java.util.List<com.github.tommwq.applogmanagement.http.ModuleInfoHttp> getModuleInfo() {
    return ModuleInfo;
  }

  public void setModuleInfo(java.util.List<com.github.tommwq.applogmanagement.http.ModuleInfoHttp> value) {
    ModuleInfo = value;
  }
}
