package com.github.tommwq.applogmanagement.repository;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class MemoryLogRecordRepository implements LogRecordRepository {

        private static ConcurrentHashMap<String, CopyOnWriteArrayList<LogRecord>> table = new ConcurrentHashMap<>();

        private MemoryLogRecordRepository() {}
        public static MemoryLogRecordRepository instance = new MemoryLogRecordRepository();

        public static MemoryLogRecordRepository instance() {
                return instance;
        }
        
        @Override
        public void save(LogRecord log) {
                // String deviceId = log.getDeviceId();

                String deviceId = "";
                if (!table.containsKey(deviceId)) {
                        table.put(deviceId, new CopyOnWriteArrayList<>());
                }

                table.get(deviceId).add(log);
        }
  
        @Override
        public List<LogRecord> load(String deviceId, long sequence, int count) {
                if (!table.containsKey(deviceId)) {
                  return new ArrayList<>();
                }

                deviceId = "";
                return table.get(deviceId);
        }
}
