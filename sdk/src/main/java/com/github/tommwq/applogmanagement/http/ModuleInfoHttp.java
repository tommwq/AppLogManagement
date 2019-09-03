package com.github.tommwq.applogmanagement.http;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ModuleInfoHttp {

  private String moduleName;
  private String moduleVersion;

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String value) {
    moduleName = value;
  }
  public String getModuleVersion() {
    return moduleVersion;
  }

  public void setModuleVersion(String value) {
    moduleVersion = value;
  }
}
