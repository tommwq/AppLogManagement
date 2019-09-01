package com.tq.applogmanagement;

import com.tq.applogmanagement.AppLogManagementProto.Log;
import java.util.List;

public interface Logger {

  public interface LogSubscriber {
    void onLog(Log log);
  }
  
  void open(StorageConfig aConfig, DeviceAndAppConfig aInfo);
  void close();

  void log(String message, Object... parameters);
  void print(Object... parameters);
  void trace();  
  void error(Throwable error);
  
  List<Log> queryLog(long sequence, int count);
  Log deviceAndAppInfoLog();
  long maxSequence();
  void setSubscriber(LogSubscriber aSubscriber);
}
