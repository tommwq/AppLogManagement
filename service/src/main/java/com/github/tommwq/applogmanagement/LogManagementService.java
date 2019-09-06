package com.github.tommwq.applogmanagement;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.repository.LogRecordRepository;
import com.github.tommwq.applogmanagement.repository.SQLiteLogRecordRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

//@Component
public class LogManagementService extends LogManagementServiceGrpc.LogManagementServiceImplBase {

        private ConcurrentHashMap<String, LogSession> deviceTable = new ConcurrentHashMap<>();
        private LogRecordRepository repository;

        public LogManagementService() {
                try {
                        repository = new SQLiteLogRecordRepository(new File("./log.db"));
                } catch (SQLException e) {
                        throw new RuntimeException("fail to open log record repository", e);
                }
        }
        
        public LogSession getLogSession(String deviceId) {
                return deviceTable.get(deviceId);
        }

        public Set<String> getOnlineDeviceIdSet() {
                return deviceTable.keySet();
        }
  
        @Override
        public StreamObserver<LogRecord> reportLog(StreamObserver<Command> outputStream) {
                LogSession session = new LogSession(outputStream, deviceTable, repository);
                return session;
        }

        @Override
        public void queryDeviceInfo(Command input, StreamObserver<DeviceAndAppInfo> outputStream) {
                System.out.println("queryDeviceInfo 1");
                String deviceId = input.getDeviceId();
                              
                List<DeviceAndAppInfo> infoList =  repository.load(deviceId, 0, 0)                
                        .stream()
                        .filter(log -> log.getHeader()
                                .getLogType() == LogType.DEVICE_AND_APP_INFO)
                        .map(log -> {
                                        try {
                                                return log.getBody().unpack(DeviceAndAppInfo.class);
                                        } catch (InvalidProtocolBufferException e) {
                                                return null;
                                        }})
                        .filter(x -> x != null)
                        .collect(Collectors.toList());

                System.out.println("queryDeviceInfo 2");

                System.out.println("info list size: " + infoList.size());
                repository.load(deviceId, 0, 0)
                        .stream()
                        .forEach(System.out::println);
                
                DeviceAndAppInfo info = null;
                if (infoList.size() > 0) {
                        info = infoList.get(infoList.size() - 1);
                } else {
                        info = DeviceAndAppInfo.newBuilder().build();
                }

                System.out.println("queryDeviceInfo 3");
                                
                outputStream.onNext(info);
                outputStream.onCompleted();

                System.out.println("queryDeviceInfo 4");
        }

        @Override
        public void queryLog(Command input, StreamObserver<LogRecord> outputStream) {
        }
}
