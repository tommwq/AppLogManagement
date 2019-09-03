package com.github.tommwq.applogmanagement;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface Logger {
  void open(StorageConfig aConfig, DeviceAndAppConfig aInfo);
  void close();

  void log(String message, Object... parameters);
  void print(Object... parameters);
  void trace();  
  void error(Throwable error);
  
  List<LogRecord> queryLogRecord(long sequence, int count);
  LogRecord deviceAndAppInfoLog();
  long maxSequence();
}
