package com.github.tommwq.applogmanagement.http;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class DeviceAndAppInfoHttp {

  private String deviceId;
  private String deviceVersion;
  private String baseOsName;
  private String baseOsVersion;
  private String osName;
  private String osVersion;
  private String appVersion;
  private java.util.List<com.github.tommwq.applogmanagement.http.ModuleInfoHttp> moduleInfo;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String value) {
    deviceId = value;
  }
  public String getDeviceVersion() {
    return deviceVersion;
  }

  public void setDeviceVersion(String value) {
    deviceVersion = value;
  }
  public String getBaseOsName() {
    return baseOsName;
  }

  public void setBaseOsName(String value) {
    baseOsName = value;
  }
  public String getBaseOsVersion() {
    return baseOsVersion;
  }

  public void setBaseOsVersion(String value) {
    baseOsVersion = value;
  }
  public String getOsName() {
    return osName;
  }

  public void setOsName(String value) {
    osName = value;
  }
  public String getOsVersion() {
    return osVersion;
  }

  public void setOsVersion(String value) {
    osVersion = value;
  }
  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String value) {
    appVersion = value;
  }
  public java.util.List<com.github.tommwq.applogmanagement.http.ModuleInfoHttp> getModuleInfo() {
    return moduleInfo;
  }

  public void setModuleInfo(java.util.List<com.github.tommwq.applogmanagement.http.ModuleInfoHttp> value) {
    moduleInfo = value;
  }
}
