package com.tq.applogmanagement.agent;

import com.tq.applogmanagement.AppLogManagementProto.LogType;
import com.tq.applogmanagement.AppLogManagementProto.Log;
import com.tq.applogmanagement.AppLogManagementProto.Command;
import com.tq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.tq.applogmanagement.LogManagementServiceGrpc;
import com.tq.applogmanagement.Logger;
import com.tq.applogmanagement.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import java.util.List;
import static com.tq.applogmanagement.Constant.DEFAULT_LOG_COUNT;
import static com.tq.applogmanagement.Constant.INVALID_COUNT;
import static com.tq.applogmanagement.Constant.INVALID_SEQUENCE;

/**
 * Receive Command, send requested logs.
 */
public class LogReportSession implements StreamObserver<Command> {

  private LogAgent agent;
  private StreamObserver<Log> logOutputStream;
  private static final Logger logger = SimpleLogger.instance();
  
  public LogReportSession(LogAgent aAgent) {
    agent = aAgent;
  }

  @Override
  public void onNext(Command command) {
    long sequence = command.getSequence();
    int count = command.getCount();

    if (sequence == INVALID_SEQUENCE) {
      sequence = SimpleLogger.instance().maxSequence();
    }
    if (count == INVALID_COUNT) {
      count = sequence < DEFAULT_LOG_COUNT ? (int) sequence : DEFAULT_LOG_COUNT;
    }

    logger.queryLog(sequence, count)
      .stream()
      .forEach(log -> logOutputStream.onNext(log));
  }
                
  @Override
  public void onError(Throwable error) {
    // TODO 重写重连机制。
  }
                
  @Override
  public void onCompleted() {
    // TODO 通知agent。
  }

  public void reportDeviceAndAppInfo() {
    logOutputStream.onNext(logger.deviceAndAppInfoLog());
  }

  public void setLogOutputStream(StreamObserver<Log> aStream) {
    logOutputStream = aStream;
  }

  public void reportLog(Log log) {
    logOutputStream.onNext(log);
  }
}

