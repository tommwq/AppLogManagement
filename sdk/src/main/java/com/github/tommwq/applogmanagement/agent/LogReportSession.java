package com.github.tommwq.applogmanagement.agent;

import static com.github.tommwq.applogmanagement.Constant.DEFAULT_LOG_COUNT;
import static com.github.tommwq.applogmanagement.Constant.INVALID_COUNT;
import static com.github.tommwq.applogmanagement.Constant.INVALID_SEQUENCE;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.github.tommwq.applogmanagement.LogManagementServiceGrpc;
import com.github.tommwq.applogmanagement.logging.Logger;
import com.github.tommwq.applogmanagement.logging.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * Receive Command, send requested logs.
 */
public class LogReportSession implements StreamObserver<Command> {

        private static final org.slf4j.Logger debugLogger = LoggerFactory.getLogger(LogReportSession.class);

        private LogReportAgent agent;
        private StreamObserver<LogRecord> outputStream = null;
        private Logger logger;
        private LogManagementServiceGrpc.LogManagementServiceStub stub;
        private boolean quit = false;

        private int[] backoff = new int[]{ 0, 4, 8, 16, 32, 64, 128 };
        private int backoffIndex = 0;
        
        public LogReportSession(LogReportAgent aAgent,
                                Logger aLogger,
                                LogManagementServiceGrpc.LogManagementServiceStub aStub) {
                agent = aAgent;
                logger = aLogger;
                stub = aStub;

                connect();
        }

        @Override
        public void onNext(Command command) {
                backoffIndex = 0;
                debugLogger.warn("onNext");
                
                long sequence = command.getSequence();
                int count = command.getCount();

                if (sequence == INVALID_SEQUENCE) {
                        sequence = logger.maxLsn();
                }
                if (count == INVALID_COUNT) {
                        count = sequence < DEFAULT_LOG_COUNT ? (int) sequence : DEFAULT_LOG_COUNT;
                }

                logger.read(sequence, count)
                        .stream()
                        .forEach(log -> outputStream.onNext(log));
        }
                
        @Override
        public void onError(Throwable error) {
                debugLogger.warn("onError");
                
                if (!quit) {
                        connect();
                }
        }
                
        @Override
        public void onCompleted() {
                debugLogger.warn("onCompleted");
                
                if (outputStream != null) {
                        outputStream.onCompleted();
                }

                if (!quit) {
                        connect();
                }
        }

        private void connect() {

                if (backoffIndex != 0) {
                        if (backoffIndex >= backoff.length) {
                                backoffIndex = backoff.length - 1;
                        }
                        
                        try {
                                long ms = backoff[backoffIndex] * 1000;
                                debugLogger.warn("sleep " + ms + " ms");
                                Thread.sleep(ms);
                        } catch (InterruptedException e) {
                                // ignore
                        }
                }
                backoffIndex++;
                
                outputStream = stub.reportLog(this);
                reportDeviceAndAppInfo();
        }

        public void reportDeviceAndAppInfo() {
                outputStream.onNext(logger.deviceAndAppInfoLog());
        }

        public void reportLog(LogRecord log) {
                outputStream.onNext(log);
        }

        public void shutdown() {
                quit = true;
                outputStream.onCompleted();
                outputStream = null;
        }
}

