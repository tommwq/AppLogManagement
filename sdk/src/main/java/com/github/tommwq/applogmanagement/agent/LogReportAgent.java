package com.github.tommwq.applogmanagement.agent;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.github.tommwq.applogmanagement.logging.Logger;
import com.github.tommwq.applogmanagement.LogManagementServiceGrpc;
import com.github.tommwq.applogmanagement.logging.SimpleLogger;
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
        private LogReportSession session = null;
  
        public LogReportAgent(String host, int port, Logger aLogger) {
                logger = aLogger;
                channel = ManagedChannelBuilder.forAddress(host, port)
                        .usePlaintext()
                        .build();

                stub = LogManagementServiceGrpc.newStub(channel);
        }

        public void shutdown() throws InterruptedException {
                if (session != null) {
                        session.shutdown();
                }
                
                channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }

        public void start() throws InterruptedException {
                session = new LogReportSession(this, logger, stub);
        }
}
