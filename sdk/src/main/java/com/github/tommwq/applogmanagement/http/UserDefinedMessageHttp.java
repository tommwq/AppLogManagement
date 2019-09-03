package com.github.tommwq.applogmanagement.http;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class UserDefinedMessageHttp {

  private String sourceFile;
  private int lineNumber;
  private String packageName;
  private String className;
  private String methodName;
  private String userDefinedMessage;

  public String getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(String value) {
    sourceFile = value;
  }
  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int value) {
    lineNumber = value;
  }
  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String value) {
    packageName = value;
  }
  public String getClassName() {
    return className;
  }

  public void setClassName(String value) {
    className = value;
  }
  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String value) {
    methodName = value;
  }
  public String getUserDefinedMessage() {
    return userDefinedMessage;
  }

  public void setUserDefinedMessage(String value) {
    userDefinedMessage = value;
  }
}
