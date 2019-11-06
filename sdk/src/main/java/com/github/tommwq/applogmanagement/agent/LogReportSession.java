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

/**
 * Receive Command, send requested logs.
 */
public class LogReportSession implements StreamObserver<Command> {

        private LogReportAgent agent;
        private StreamObserver<LogRecord> outputStream = null;
        private Logger logger;
        private LogManagementServiceGrpc.LogManagementServiceStub stub;
        private boolean quit = false;
        private long lastConnectFailure = 0;
        
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
                
                lastConnectFailure = 0;

                long sequence = command.getSequence();
                int count = command.getCount();

                if (sequence == INVALID_SEQUENCE) {
                        sequence = logger.maxLsn();
                }
                if (count == INVALID_COUNT) {
                        count = sequence < DEFAULT_LOG_COUNT ? (int) sequence : DEFAULT_LOG_COUNT;
                }

                logger.readAll() // (sequence, count)
                        .stream()
                        .filter(log -> log.getHeader().getLogType() != LogType.DEVICE_AND_APP_INFO)
                        .forEach(log -> outputStream.onNext(log));
        }
                
        @Override
        public void onError(Throwable error) {
                if (quit) {
                        return;
                }

                long now = System.currentTimeMillis();
                if (lastConnectFailure != 0) {
                        long gap = (now - lastConnectFailure) / 1000;
                        long sleepTime;
                        if (gap < 2) {
                                sleepTime = 2;
                        } else if (gap > 60) {
                                sleepTime = 60;
                        } else {
                                sleepTime = gap * 2;
                        }

                        try {
                                Thread.sleep(sleepTime * 1000);
                        } catch (InterruptedException e) {
                                // ignore
                        }
                } else {
                        lastConnectFailure = now;
                }
                
                connect();
        }
                
        @Override
        public void onCompleted() {
                if (outputStream != null) {
                        outputStream.onCompleted();
                }

                if (quit) {
                        return;
                }

                connect();
        }

        private void connect() {
                new Thread(() -> {
                                outputStream = stub.reportLog(this);
                                reportDeviceAndAppInfo();
                }).start();
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

