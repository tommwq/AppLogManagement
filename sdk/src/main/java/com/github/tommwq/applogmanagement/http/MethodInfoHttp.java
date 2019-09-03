package com.github.tommwq.applogmanagement.http;

public class MethodInfoHttp {

  private String SourceFile;
  private int LineNumber;
  private String ClassName;
  private String MethodName;

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
}
