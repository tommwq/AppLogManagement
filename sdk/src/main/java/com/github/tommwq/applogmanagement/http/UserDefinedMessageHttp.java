package com.github.tommwq.applogmanagement.http;

public class UserDefinedMessageHttp {

  private String SourceFile;
  private int LineNumber;
  private String PackageName;
  private String ClassName;
  private String MethodName;
  private String UserDefinedMessage;

  public String getSourceFile() {
    return SourceFile;
  }

  public void setSourceFile(String value) {
    SourceFile = value;
  }
  public int getLineNumber() {
    return LineNumber;
  }

  public void setLineNumber(int value) {
    LineNumber = value;
  }
  public String getPackageName() {
    return PackageName;
  }

  public void setPackageName(String value) {
    PackageName = value;
  }
  public String getClassName() {
    return ClassName;
  }

  public void setClassName(String value) {
    ClassName = value;
  }
  public String getMethodName() {
    return MethodName;
  }

  public void setMethodName(String value) {
    MethodName = value;
  }
  public String getUserDefinedMessage() {
    return UserDefinedMessage;
  }

  public void setUserDefinedMessage(String value) {
    UserDefinedMessage = value;
  }
}
