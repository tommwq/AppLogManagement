package com.github.tommwq.applogmanagement.provider;

import com.github.tommwq.applogmanagement.api.DeviceLogManagementServiceApi;
import com.github.tommwq.applogmanagement.api.DeviceInfoRepositoryApi;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.repository.CommandRepository;
import com.github.tommwq.applogmanagement.repository.DeviceInfoRepository;
import com.github.tommwq.applogmanagement.repository.LogRecordRepository;
import com.github.tommwq.applogmanagement.repository.SQLiteCommandRepository;
import com.github.tommwq.applogmanagement.repository.SQLiteLogRecordRepository;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Call;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceLogManagementService implements DeviceLogManagementServiceApi {

        private Connection connection;
        private String sqliteDbFileName;
        private DeviceInfoRepositoryApi deviceInfoRepository;

        public DeviceLogManagementService() {
                sqliteDbFileName = "applog.db";
        }

        @Override
        public void open() {
                String dsn = String.format("jdbc:sqlite:%s", sqliteDbFileName);
                new Call(() -> {
                                connection = DriverManager.getConnection(dsn);
                                deviceInfoRepository = new DeviceInfoRepository(connection);
                }).rethrow();
        }

        @Override
        public void close() {
                if (connection == null) {
                        return;
                }

                new Call(() -> connection.close()).rethrow();
        }
  
        public void onDeviceConnected(DeviceAndAppInfo info) {
                deviceInfoRepository.deviceConnect(info);
        }

        public void onDeviceDisconnected(String deviceId) {
                deviceInfoRepository.deviceDisconnect(deviceId);
        }

        public List<String> onlineDeviceIdList() {
                return deviceInfoRepository.loadAll()
                        .stream()
                        .filter(info -> info.isOnline)
                        .map(info -> info.deviceId)
                        .collect(Collectors.toList());
        }

        public List<LogRecord> readLogCache(String deviceId) {
                return null;
        }

        public void appendLogCache(String deviceId, List<LogRecord> logList) {
        }

        public void appendLogCache(String deviceId, LogRecord log) {
        }
}
