package com.github.tommwq.applogmanagement.storage;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface LogStorage {
  void save(LogRecord log);
  List<LogRecord> load(String deviceId, long sequence, int count);
}
