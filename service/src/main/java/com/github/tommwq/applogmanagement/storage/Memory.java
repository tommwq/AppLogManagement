package com.github.tommwq.applogmanagement.storage;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Memory implements LogStorage {

        private static ConcurrentHashMap<String, CopyOnWriteArrayList<LogRecord>> table = new ConcurrentHashMap<>();
  
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
                // ignore sequence and count
                // if (!table.containsKey(deviceId)) {
                //   return new ArrayList<>();
                // }

                deviceId = "";
                return table.get(deviceId);
        }
}
