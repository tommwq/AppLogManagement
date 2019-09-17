package com.github.tommwq.applogmanagement.provider;

import com.github.tommwq.applogmanagement.api.LogRepositoryApi;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.utility.database.DBHelper;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Call;
import com.github.tommwq.utility.function.Predicates;
import com.github.tommwq.utility.Util;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogRepository implements LogRepositoryApi {

        private static class Log {
                String deviceId;
                String logRecord;

                public Log(String aDeviceId, String aLogRecord) {
                        deviceId = aDeviceId;
                        logRecord = aLogRecord;
                }
        }

        private Connection conn;
        private DBHelper helper;
                
        public LogRepository(Connection aConnection) throws SQLException {
                conn = aConnection;
                helper = new SQLiteHelper(conn).createTableInNeed(Log.class);
        }
        
        @Override
        public void save(String deviceId, LogRecord logRecord) {
                new Call(() -> save(new Log(deviceId, logRecord.toString()))).rethrow();
        }

        private void save(Log log) throws SQLException {
                helper.insert(log);
        }

        @Override
        public List<LogRecord> load(String deviceId) {
                List<Log> logList = (List<Log>) new Call((nil) -> loadLog(deviceId), null, null)
                        .rethrow()
                        .result();
                
                return logList.stream()
                        .map(log -> (LogRecord) new Call((nil) -> {
                                                LogRecord.Builder builder = LogRecord.newBuilder();
                                                com.google.protobuf.TextFormat.getParser().merge(log.logRecord, builder);
                                                return builder.build();
                        }, null, null).result())
                        .filter(Predicates::notNull)
                        .collect(Collectors.toList());
        }
  
        public List<Log> loadLog(String deviceId) throws SQLException {
                List<Log> logList = new ArrayList<>();
                String query = helper.selectSQL(Log.class) + " WHERE deviceId=?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, deviceId);
                        try (ResultSet resultSet = pstmt.executeQuery()) {
                                while (resultSet.next()) {
                                        String id = resultSet.getString(1);
                                        String log = resultSet.getString(2);
                                        logList.add(new Log(id, log));
                                }

                                return logList;
                        }
                }
        }
}
