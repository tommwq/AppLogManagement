package com.github.tommwq.applogmanagement.provider;

import com.github.tommwq.applogmanagement.api.CommandRepositoryApi;
import com.github.tommwq.applogmanagement.api.DeviceInfoRepositoryApi;
import com.github.tommwq.applogmanagement.api.DeviceLogManagementServiceApi;
import com.github.tommwq.applogmanagement.api.LogRepositoryApi;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.LogSession;
import com.github.tommwq.applogmanagement.provider.CommandRepository;
import com.github.tommwq.applogmanagement.provider.DeviceInfoRepository;
import com.github.tommwq.applogmanagement.provider.LogRepository;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Call;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeviceLogManagementService implements DeviceLogManagementServiceApi {

        private Connection connection;
        private String sqliteDbFileName;
        private DeviceInfoRepositoryApi deviceInfoRepository;
        private CommandRepositoryApi commandRepository;
        private LogRepositoryApi logRepository;
        private Map<String,LogSession> deviceTable = new ConcurrentHashMap<>();
        
        public DeviceLogManagementService() {
                sqliteDbFileName = "applog.db";
        }

        @Override
        public void open() {
                String dsn = String.format("jdbc:sqlite:%s", sqliteDbFileName);
                new Call(() -> {
                                connection = DriverManager.getConnection(dsn);
                                deviceInfoRepository = new DeviceInfoRepository(connection);
                                commandRepository = new CommandRepository(connection);
                                logRepository = new LogRepository(connection);
                }).rethrow();
        }

        @Override
        public void close() {
                if (connection == null) {
                        return;
                }

                new Call(() -> connection.close()).rethrow();
        }
  
        public void deviceConnect(DeviceAndAppInfo info, Object context) {                
                deviceInfoRepository.deviceConnect(info);
                LogSession session = (LogSession) context;
                deviceTable.put(info.getDeviceId(), session);
        }

        public void deviceDisconnect(String deviceId) {
                deviceTable.remove(deviceId);
        }

        public void deviceDisconnect(String deviceId, Throwable error) {
                deviceInfoRepository.deviceDisconnect(deviceId);
        }

        public List<String> onlineDeviceIdList() {
                deviceInfoRepository.loadAll().forEach(System.out::println);
                
                return deviceInfoRepository.loadAll()
                        .stream()
                        .filter(info -> info.isOnline == 1)
                        .map(info -> info.deviceId)
                        .collect(Collectors.toList());
        }

        public void saveLog(String deviceId, LogRecord log) {
                System.out.println(log.toString());
                logRepository.save(deviceId, log);
        }

        public Object getContext(String deviceId) {
                return deviceTable.get(deviceId);
        }

        public List<LogRecord> loadLog(String deviceId) {
                return logRepository.load(deviceId);
        }

        @Override
        public List<Command> queryCachedCommand() {
                return commandRepository.loadAll();
        }

        @Override
        public void cacheCommand(Command command) {
                commandRepository.save(command);
        }
}
