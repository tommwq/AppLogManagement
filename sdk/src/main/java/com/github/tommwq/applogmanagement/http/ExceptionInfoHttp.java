package com.github.tommwq.applogmanagement.http;

public class ExceptionInfoHttp {

  private com.github.tommwq.applogmanagement.http.ObjectInfoHttp Exception;
  private java.util.List<com.github.tommwq.applogmanagement.http.MethodInfoHttp> Stack;

  public com.github.tommwq.applogmanagement.http.ObjectInfoHttp getException() {
    return Exception;
  }

  public void setException(com.github.tommwq.applogmanagement.http.ObjectInfoHttp value) {
    Exception = value;
  }
  public java.util.List<com.github.tommwq.applogmanagement.http.MethodInfoHttp> getStack() {
    return Stack;
  }

  public void setStack(java.util.List<com.github.tommwq.applogmanagement.http.MethodInfoHttp> value) {
    Stack = value;
  }
}
