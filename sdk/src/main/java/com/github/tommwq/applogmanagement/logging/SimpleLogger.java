package com.github.tommwq.applogmanagement.logging;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ExceptionInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogHeader;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.AppLogManagementProto.MethodAndObjectInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.MethodInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ObjectInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.UserDefinedMessage;
import com.github.tommwq.applogmanagement.logging.LogRecordStorage;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage;
import com.github.tommwq.applogmanagement.logging.LogUtil;
import com.github.tommwq.applogmanagement.DeviceAndAppConfig;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage.Config;
import com.github.tommwq.applogmanagement.storage.BlockStorage;
import com.github.tommwq.utility.StringUtil;
import com.github.tommwq.utility.function.Call;
import com.github.tommwq.utility.ThreadUtil;
import com.github.tommwq.utility.Util;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.LoggerFactory;

public class SimpleLogger extends Logger {

        private static final org.slf4j.Logger debugLogger = LoggerFactory.getLogger(SimpleLogger.class);
        
        private DeviceAndAppConfig config = new DeviceAndAppConfig();
        private LogRecordStorage storage;
        private LinkedTransferQueue<LogRecord> queue = new LinkedTransferQueue<>();
        private boolean closed = true;
        private LogRecord deviceAndAppInfoLog;
        private FlushThread flushThread = null;
        private long maxLsn = 0;
        private long minLsn = 0;
        
        public void open(BlockStorage blockStorage, DeviceAndAppConfig aConfig) {
                config = aConfig;
                storage = new LogRecordStorage(blockStorage);

                new Call(() -> {
                                storage.open();
                                flushThread = new FlushThread(queue, storage);
                                flushThread.start();
                                closed = false;
                                maxLsn = storage.maxSequence();
                                minLsn = 0;
                }).rethrow();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> close()));

                Thread.UncaughtExceptionHandler next = Thread.currentThread().getUncaughtExceptionHandler();
                Thread.currentThread().setUncaughtExceptionHandler(
                        (thread, error) -> {
                                close();
                                if (next != null) {
                                        next.uncaughtException(thread, error);
                                }
                        });
    
                deviceAndAppInfoLog = LogUtil.newDeviceAndAppInfoLog(nextLsn(), config);
                write(deviceAndAppInfoLog);
        }
    
        public void close() {
                closed = true;
                if (flushThread != null) {
                        queue.offer(FlushThread.END_OF_QUEUE);
                        ThreadUtil.john(flushThread);
                        flushThread = null;
                }
        }

        public void write(LogRecord log) {
                if (closed) {
                        return;
                }

                debugLogger.debug("write log " + log.toString());
                queue.offer(log);
        }

        public void trace() {
                write(LogUtil.newMethodAndObjectInfoLog(nextLsn(), Util.currentFrame(3), new Object[]{}));
        }
  
        public void log(String message, Object... parameters) {
                String text = String.join(", ", Stream.concat(Stream.of(message), Stream.of(parameters))
                                          .map(x -> x == null ? "null" : x.toString())
                                          .collect(Collectors.toList()));
                write(LogUtil.newUserDefinedLog(nextLsn(), Util.currentFrame(3), text));
        }

        public void print(Object... parameters) {
                write(LogUtil.newMethodAndObjectInfoLog(nextLsn(), Util.currentFrame(3), parameters));
        }

        public void error(Throwable error) {
                write(LogUtil.newExceptionInfoLog(nextLsn(), error));
        }

        private synchronized long nextLsn() {
                return maxLsn++;
        }

        public List<LogRecord> readAll() {
                return storage.read(0, (int) maxLsn());
        }

        public LogRecord read() {
                return read(maxLsn() - 1);
        }

        public long maxLsn() {
                return maxLsn;
        }

        public long minLsn() {
                return minLsn;
        }
        
        public LogRecordReader moveTo(long sequence) {
                return this;
        }

        public LogRecord read(long sequence) {
                List<LogRecord> list = storage.read(sequence, 1);
                if (list.isEmpty()) {
                        return null;
                }

                return list.get(0);
        }
        
        public List<LogRecord> read(long sequence, int count) {
                return storage.read(sequence, count);
        }

        public LogRecord deviceAndAppInfoLog() {
                return deviceAndAppInfoLog;
        }
}
