package com.github.tommwq.applogmanagement.repository;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface LogRecordRepository {
        void save(String deviceId, LogRecord log);
        List<LogRecord> load(String deviceId, long sequence, int count);
}
