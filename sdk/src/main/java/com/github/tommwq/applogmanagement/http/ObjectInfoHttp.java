package com.github.tommwq.applogmanagement.http;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ObjectInfoHttp {

  private String objectType;
  private String objectValue;

  public String getObjectType() {
    return objectType;
  }

  public void setObjectType(String value) {
    objectType = value;
  }
  public String getObjectValue() {
    return objectValue;
  }

  public void setObjectValue(String value) {
    objectValue = value;
  }
}
