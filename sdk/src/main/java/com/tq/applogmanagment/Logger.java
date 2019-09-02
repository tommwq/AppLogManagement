package com.tq.applogmanagement;

import com.tq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface Logger {

  public interface LogRecordSubscriber {
    void onLogRecord(LogRecord log);
  }
  
  void open(StorageConfig aConfig, DeviceAndAppConfig aInfo);
  void close();

  void log(String message, Object... parameters);
  void print(Object... parameters);
  void trace();  
  void error(Throwable error);
  
  List<LogRecord> queryLogRecord(long sequence, int count);
  LogRecord deviceAndAppInfoLog();
  long maxSequence();
  void setSubscriber(LogRecordSubscriber aSubscriber);
}
