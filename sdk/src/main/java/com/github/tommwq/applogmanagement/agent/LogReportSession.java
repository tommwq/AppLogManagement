package com.github.tommwq.applogmanagement.agent;

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
import static com.github.tommwq.applogmanagement.Constant.DEFAULT_LOG_COUNT;
import static com.github.tommwq.applogmanagement.Constant.INVALID_COUNT;
import static com.github.tommwq.applogmanagement.Constant.INVALID_SEQUENCE;

/**
 * Receive Command, send requested logs.
 */
public class LogReportSession implements StreamObserver<Command> {

        private static enum ConnectStatus {
                Disconnected, Connected, Connecting;
        }

        private LogReportAgent agent;
        private StreamObserver<LogRecord> outputStream = null;
        private Logger logger;
        private LogManagementServiceGrpc.LogManagementServiceStub stub;
        private ConnectStatus connectStatus = ConnectStatus.Disconnected;
        private boolean quit = false;
        
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
                if (connectStatus != ConnectStatus.Connected) {
                        connectStatus = ConnectStatus.Connected;
                }
                
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
                if (connectStatus != ConnectStatus.Connecting) {
                        connectStatus = ConnectStatus.Disconnected;
                }
                
                if (!quit) {
                        connect();
                }
        }
                
        @Override
        public void onCompleted() {
                connectStatus = ConnectStatus.Disconnected;
                if (outputStream != null) {
                        outputStream.onCompleted();
                }

                if (!quit) {
                        connect();
                }
        }

        private void connect() {
                if (connectStatus != ConnectStatus.Disconnected) {
                        return;
                }

                connectStatus = ConnectStatus.Connecting;                
                int[] backoff = new int[]{ 4, 8, 16, 32, 64, 128 };
                int retry = 0;

                while (connectStatus != ConnectStatus.Connected) {
                        reconnect();
                        try {
                                long time = 1000 * (retry < backoff.length ? backoff[retry] : backoff[backoff.length-1]);
                                Thread.sleep(time);
                        } catch (InterruptedException e) {
                                break;
                        }
                        retry++;
                }
        }

        private void reconnect() {
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

