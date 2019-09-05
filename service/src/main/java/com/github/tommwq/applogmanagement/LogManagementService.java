package com.github.tommwq.applogmanagement;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.repository.LogRecordRepository;
import com.github.tommwq.applogmanagement.repository.MemoryLogRecordRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

//@Component
public class LogManagementService extends LogManagementServiceGrpc.LogManagementServiceImplBase {

        private ConcurrentHashMap<String, LogSession> deviceTable = new ConcurrentHashMap<>();

        public LogSession getLogSession(String deviceId) {
                return deviceTable.get(deviceId);
        }

        public Set<String> getOnlineDeviceIdSet() {
                return deviceTable.keySet();
        }
  
        @Override
        public StreamObserver<LogRecord> reportLog(StreamObserver<Command> outputStream) {
                LogSession session = new LogSession(outputStream, deviceTable);
                return session;
        }

        @Override
        public void queryDeviceInfo(Command input, StreamObserver<DeviceAndAppInfo> outputStream) {
                System.out.println("queryDeviceInfo 1");
                String deviceId = input.getDeviceId();
                
                LogRecordRepository repository = MemoryLogRecordRepository.instance();
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
                
                DeviceAndAppInfo info = null;
                if (infoList.size() > 0) {
                        info = infoList.get(infoList.size() - 1);
                } else {
                        info = DeviceAndAppInfo.newBuilder().build();
                }

                System.out.println("queryDeviceInfo 3");
                                
                outputStream.onNext(info);
        }

        @Override
        public void queryLog(Command input, StreamObserver<LogRecord> outputStream) {
        }
}
