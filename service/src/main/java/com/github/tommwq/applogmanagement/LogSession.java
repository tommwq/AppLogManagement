package com.github.tommwq.applogmanagement;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.storage.LogStorage;
import com.github.tommwq.applogmanagement.storage.Memory;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

public class LogSession implements StreamObserver<LogRecord> {

  private String deviceId = "";
  private ConcurrentHashMap<String, LogSession> deviceTable;
  private StreamObserver<Command> outputStream;
  private LogStorage storage = new Memory();
    
  public LogSession(StreamObserver<Command> aOutputStream, ConcurrentHashMap<String,LogSession> aDeviceTable) {
    outputStream = aOutputStream;
    deviceTable = aDeviceTable;
  }
      
  @Override
  public void onNext(LogRecord newLog) {
    Any body = newLog.getBody();
    if (body.is(DeviceAndAppInfo.class)) {
      try {
        DeviceAndAppInfo info = body.unpack(DeviceAndAppInfo.class);
        deviceId = info.getDeviceId();
        if (!deviceTable.containsKey(deviceId)) {
          deviceTable.put(deviceId, this);
        }
      } catch (InvalidProtocolBufferException e) {
        e.printStackTrace(System.err);
      }
    }

    storage.save(newLog);
  }
    
  @Override
  public void onError(Throwable throwable) {
    deviceTable.remove(deviceId);
  }
    
  @Override
  public void onCompleted() {
    deviceTable.remove(deviceId);
    outputStream.onCompleted();
  }

  public void command() {
    outputStream.onNext(Command.newBuilder()
                        .setSequence(0)
                        .setCount(0)
                        .build());
  }
}
  

