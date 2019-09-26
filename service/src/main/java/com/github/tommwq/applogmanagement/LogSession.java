package com.github.tommwq.applogmanagement;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.api.DeviceLogManagementServiceApi;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

public class LogSession implements StreamObserver<LogRecord> {

        private String deviceId = "";
        private StreamObserver<Command> outputStream;
        private DeviceLogManagementServiceApi service;
    
        public LogSession(StreamObserver<Command> aOutputStream, DeviceLogManagementServiceApi aService) {
                outputStream = aOutputStream;
                service = aService;
        }

        private void onDeviceConnect(String deviceId) {
                List<Command> cached = service.queryCachedCommand()
                        .stream()
                        .filter(command -> command.getDeviceId().equals(deviceId))
                        .collect(Collectors.toList());

                if (cached.isEmpty()) {
                        return;
                }

                outputStream.onNext(cached.get(0));
        }
      
        @Override
        public void onNext(LogRecord newLog) {
                Any body = newLog.getBody();
                if (body.is(DeviceAndAppInfo.class)) {
                        try {
                                DeviceAndAppInfo info = body.unpack(DeviceAndAppInfo.class);
                                deviceId = info.getDeviceId();
                                service.deviceConnect(info, this);
                                onDeviceConnect(deviceId);
                        } catch (InvalidProtocolBufferException e) {
                                e.printStackTrace(System.err);
                        }
                }

                if (!deviceId.isEmpty()) {
                        service.saveLog(deviceId, newLog);
                }
        }
    
        @Override
        public void onError(Throwable throwable) {
                service.deviceDisconnect(deviceId, throwable);
        }
    
        @Override
        public void onCompleted() {
                outputStream.onCompleted();
                service.deviceDisconnect(deviceId);
        }

        public void command() {
                outputStream.onNext(Command.newBuilder()
                                    .setSequence(0)
                                    .setCount(0)
                                    .build());
        }
}
  

