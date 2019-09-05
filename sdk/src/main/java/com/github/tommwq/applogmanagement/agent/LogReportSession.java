package com.github.tommwq.applogmanagement.agent;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.github.tommwq.applogmanagement.LogManagementServiceGrpc;
import com.github.tommwq.applogmanagement.Logger;
import com.github.tommwq.applogmanagement.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import java.util.List;
import static com.github.tommwq.applogmanagement.Constant.DEFAULT_LOG_COUNT;
import static com.github.tommwq.applogmanagement.Constant.INVALID_COUNT;
import static com.github.tommwq.applogmanagement.Constant.INVALID_SEQUENCE;

/**
 * Receive Command, send requested logs.
 */
public class LogReportSession implements StreamObserver<Command> {

        private LogReportAgent agent;
        private StreamObserver<LogRecord> logOutputStream;
        private Logger logger;
  
        public LogReportSession(LogReportAgent aAgent, Logger aLogger) {
                agent = aAgent;
                logger = aLogger;
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

                logger.queryLogRecord(sequence, count)
                        .stream()
                        .forEach(log -> logOutputStream.onNext(log));
        }
                
        @Override
        public void onError(Throwable error) {
                System.out.println(error.toString());          
                // TODO 重写重连机制。
        }
                
        @Override
        public void onCompleted() {          
                // TODO 通知agent。
        }

        public void reportDeviceAndAppInfo() {
                System.out.println("report device and app info.");
                logOutputStream.onNext(logger.deviceAndAppInfoLog());
        }

        public void setLogOutputStream(StreamObserver<LogRecord> aStream) {
                logOutputStream = aStream;
        }

        public void reportLog(LogRecord log) {
                logOutputStream.onNext(log);
        }
}

