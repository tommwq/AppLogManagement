package com.github.tommwq.applogmanagement.provider;

import com.github.tommwq.applogmanagement.api.DeviceLogManagementService;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDeviceLogManagementService implements DeviceLogManagementService {

        private Connection sqliteConnection;
        private String sqliteDbFileName;

        public SQLiteDeviceLogManagementService(String aFileName) {
                sqliteDbFileName = aFileName;
        }

        @Override
        public void open() {
                String dsn = String.format("jdbc:sqlite:%s", sqliteDbFileName);
                try {
                        sqliteConnection = DriverManager.getConnection(dsn);
                        createTableInNeed(sqliteConnection, "onlineDevice", "create table onlineDevice (deviceId text);");
                } catch (SQLException e) {
                        throw new RuntimeException("fail to open SQLite database", e);
                }
        }

        @Override
        public void close() {
                if (sqliteConnection == null) {
                        return;
                }

                try {
                        sqliteConnection.close();
                } catch (SQLException e) {
                        throw new RuntimeException("fail to close SQLite database", e);
                }
        }

        private boolean isTableExist(Connection connection, String tableName) {
                String sqlFormat = "select count(*) as exist from sqlite_master where type='table' and name='%s'";
                String sql = sqlFormat.format(tableName);
                try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sql);
                        if (!resultSet.next()) {
                                throw new RuntimeException("unknown sqlite error");
                        }

                        boolean exist = (resultSet.getInt("exist") == 1);
                        return exist;
                } catch (SQLException e) {
                        throw new RuntimeException("fail to close SQLite database", e);
                }
        }

        private void createTable(Connection connection, String createTableSQL) {
                try {
                        Statement statement = connection.createStatement();
                        statement.executeQuery(createTableSQL);
                } catch (SQLException e) {
                        throw new RuntimeException("fail to create table", e);
                }
        }

        private void createTableInNeed(Connection connection, String tableName, String createTableSQL) {
                if (!isTableExist(connection, tableName)) {
                        createTable(connection, createTableSQL);
                }
        }
  
        public void onDeviceConnected(DeviceAndAppInfo info) {
                try {
                        Statement statement = sqliteConnection.createStatement();
                        String sql = String.format("insert into onlineDevice values ('%s');", info.getDeviceId());
                        statement.executeQuery(sql);
                } catch (SQLException e) {
                        throw new RuntimeException("fail to insert device record", e);
                }
        }

        public void onDeviceDisconnected(String deviceId) {
                try {
                        Statement statement = sqliteConnection.createStatement();
                        String sql = String.format("delete from onlineDevice where deviceId='%s';", deviceId);
                        statement.executeQuery(sql);
                } catch (SQLException e) {
                        throw new RuntimeException("fail to insert device record", e);
                }
        }

        public List<String> onlineDeviceIdList() {
                try {
                        Statement statement = sqliteConnection.createStatement();
                        String sql = "select deviceId from onlineDevice;";
                        ResultSet resultSet = statement.executeQuery(sql);
                        List<String> idList = new ArrayList<>();
                        while (resultSet.next()) {
                                String deviceId = resultSet.getString("deviceId");
                                idList.add(deviceId);
                        }
                        return idList;
                } catch (SQLException e) {
                        throw new RuntimeException("fail to query device record", e);
                }
        }

        public List<LogRecord> readLogCache(String deviceId) {
                return null;
        }

        public void appendLogCache(String deviceId, List<LogRecord> logList) {
        }

        public void appendLogCache(String deviceId, LogRecord log) {
        }
}
