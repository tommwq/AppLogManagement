package com.github.tommwq.applogmanagement;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.api.DeviceLogManagementServiceApi;
import com.github.tommwq.applogmanagement.provider.DeviceLogManagementService;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

//@Component
public class LogManagementService extends LogManagementServiceGrpc.LogManagementServiceImplBase {

        private DeviceLogManagementServiceApi service = new DeviceLogManagementService();

        public LogManagementService() {
                service.open();
        }

        public List<String> onlineDeviceIdList() {
                return service.onlineDeviceIdList();
        }

        public LogSession logSession(String deviceId) {
                return (LogSession) service.getContext(deviceId);
        }
          
        @Override
        public StreamObserver<LogRecord> reportLog(StreamObserver<Command> outputStream) {
                LogSession session = new LogSession(outputStream, service);
                return session;
        }

        @Override
        public void queryDeviceInfo(Command input, StreamObserver<DeviceAndAppInfo> outputStream) {
                System.out.println("queryDeviceInfo 1");
                String deviceId = input.getDeviceId();
                              
                List<DeviceAndAppInfo> infoList =  service.loadLog(deviceId)
                        .stream()
                        .filter(log -> log.getHeader().getLogType() == LogType.DEVICE_AND_APP_INFO)
                        .map(log -> {
                                        try {
                                                return log.getBody().unpack(DeviceAndAppInfo.class);
                                        } catch (InvalidProtocolBufferException e) {
                                                return null;
                                        }})
                        .filter(x -> x != null)
                        .collect(Collectors.toList());

                System.out.println("queryDeviceInfo 2");
                
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
