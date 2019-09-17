package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface LogRepositoryApi {

        public static class Log {
                public String deviceId;
                public String log;

                public Log() {
                }

                public Log(String aId, LogRecord record) {
                        deviceId = aId;
                        log = record.toString();
                }
        }
        
        void save(String deviceId, LogRecord log);
        List<LogRecord> load(String deviceId);
}
