package com.github.tommwq.applogmanagement.http;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ExceptionInfoHttp {

  private com.github.tommwq.applogmanagement.http.ObjectInfoHttp exception;
  private java.util.List<com.github.tommwq.applogmanagement.http.MethodInfoHttp> stack;

  public com.github.tommwq.applogmanagement.http.ObjectInfoHttp getException() {
    return exception;
  }

  public void setException(com.github.tommwq.applogmanagement.http.ObjectInfoHttp value) {
    exception = value;
  }
  public java.util.List<com.github.tommwq.applogmanagement.http.MethodInfoHttp> getStack() {
    return stack;
  }

  public void setStack(java.util.List<com.github.tommwq.applogmanagement.http.MethodInfoHttp> value) {
    stack = value;
  }
}
