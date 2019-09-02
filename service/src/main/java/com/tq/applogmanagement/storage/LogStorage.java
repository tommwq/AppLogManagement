package com.tq.applogmanagement.storage;

import com.tq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface LogStorage {
  void save(LogRecord log);
  List<LogRecord> load(String deviceId, long sequence, int count);
}
