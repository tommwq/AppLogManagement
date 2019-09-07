package com.github.tommwq.applogmanagement.repository;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import com.github.tommwq.utility.database.DBHelper;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Predicates;
import com.github.tommwq.utility.Util;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
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

public class SQLiteLogRecordRepository implements LogRecordRepository {

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
                
        public SQLiteLogRecordRepository(File databaseFile) throws SQLException {
                conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getName());
                helper = new SQLiteHelper(conn).createTableInNeed(Log.class);
        }
        
        @Override
        public void save(String deviceId, LogRecord logRecord) {
                try {
                        save(new Log(deviceId, logRecord.toString()));
                } catch (SQLException e) {
                        throw new RuntimeException("fail to save log record", e);
                }
        }

        private void save(Log log) throws SQLException {
                try (PreparedStatement pstmt = helper.insertStatement(log, conn)) {
                        pstmt.executeUpdate();
                }
        }

        @Override
        public List<LogRecord> load(String deviceId, long sequence, int count) {
                try {
                        return loadLog(deviceId, sequence, count)
                                .stream()
                                .map(log -> {
                                                try {
                                                        LogRecord.Builder builder = LogRecord.newBuilder();
                                                        com.google.protobuf.TextFormat.getParser().merge(log.logRecord, builder);
                                                        return builder.build();
                                                } catch (ParseException e) {
                                                        return null;
                                                }
                                        })
                                .filter(Predicates::notNull)
                                .collect(Collectors.toList());
                } catch (SQLException e) {
                        throw new RuntimeException("fail to load log record", e);
                }
        }
  
        public List<Log> loadLog(String deviceId, long sequence, int count) throws SQLException {
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
