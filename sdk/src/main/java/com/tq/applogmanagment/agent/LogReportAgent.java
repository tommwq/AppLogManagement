package com.tq.applogmanagement.agent;

import com.tq.applogmanagement.AppLogManagementProto.Command;
import com.tq.applogmanagement.AppLogManagementProto.LogRecord;
import com.tq.applogmanagement.AppLogManagementProto.LogType;
import com.tq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.tq.applogmanagement.Logger;
import com.tq.applogmanagement.LogManagementServiceGrpc;
import com.tq.applogmanagement.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import java.util.List;

/**
 * connect to log management node, report logs when requested.
 */
public class LogReportAgent {

  private final ManagedChannel channel;
  private final LogManagementServiceGrpc.LogManagementServiceStub stub;
  private Logger logger;
  private LogReportSession session;
  
  public LogReportAgent(String host, int port, Logger aLogger) {
    logger = aLogger;
    channel = ManagedChannelBuilder.forAddress(host, port)
      .usePlaintext()
      .build();

    stub = LogManagementServiceGrpc.newStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown()
      .awaitTermination(30, TimeUnit.SECONDS);
  }

  public void start() throws InterruptedException {
    session = new LogReportSession(this, logger);
    session.setLogOutputStream(stub.reportLog(session));
    session.reportDeviceAndAppInfo();
  }
}
