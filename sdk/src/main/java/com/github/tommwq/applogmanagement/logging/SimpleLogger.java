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
import  com.github.tommwq.applogmanagement.storage.SimpleBlockStorage.Config;
import  com.github.tommwq.applogmanagement.storage.BlockStorage;
import com.github.tommwq.utility.StringUtil;
import com.github.tommwq.utility.function.Call;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleLogger extends Logger {

        private DeviceAndAppConfig config = new DeviceAndAppConfig();
        private long sequence = 1;
        private LogRecordStorage storage;
        private LogRecord deviceAndAppInfoLog;
        private LinkedTransferQueue<LogRecord> logQueue = new LinkedTransferQueue<>();
        
        public void open(BlockStorage blockStorage, DeviceAndAppConfig aConfig) {
                config = aConfig;
                storage = new LogRecordStorage(blockStorage);

                new Call(() -> {
                                storage.open();
                                // TODO start background thread
                });

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                                        close();
                }));

                Thread.UncaughtExceptionHandler next = Thread.currentThread().getUncaughtExceptionHandler();
                Thread.currentThread().setUncaughtExceptionHandler(
                        (thread, error) -> {
                                // TODO log
                                if (next != null) {
                                        next.uncaughtException(thread, error);
                                }
                        });
    
                deviceAndAppInfoLog = LogUtil.newDeviceAndAppInfoLog(nextSequence(), config);
        }

        // public LogRecord deviceAndAppInfoLog() {
        //         return deviceAndAppInfoLog;
        // }

        public void close() {
                // TODO flush
        }

        public void write(LogRecord log) {
                try {
                        storage.write(log);
                        // TODO test
                } catch (IOException e) {
                        // TODO 根据策略决定忽略或强制退出进程。
                }
        }

        private StackTraceElement currentFrame() {
                StackTraceElement[] stack = new Throwable().getStackTrace();

                final int stackDepth = 3;
                if (stack.length < stackDepth) {
                        throw new RuntimeException("cannot get stack information");
                }

                return stack[stackDepth - 1];
        }

        public void trace() {
                write(LogUtil.newMethodAndObjectInfoLog(nextSequence(), currentFrame(), new Object[]{}));
        }
  
        public void log(String message, Object... parameters) {
                String text = String.join(", ", Stream.concat(Stream.of(message), Stream.of(parameters))
                                          .map(x -> x == null ? "null" : x.toString())
                                          .collect(Collectors.toList()));
                write(LogUtil.newUserDefinedLog(nextSequence(), currentFrame(), text));
        }

        public void print(Object... parameters) {
                write(LogUtil.newMethodAndObjectInfoLog(nextSequence(), currentFrame(), parameters));
        }

        public void error(Throwable error) {
                write(LogUtil.newExceptionInfoLog(nextSequence(), error));
        }

        public List<LogRecord> queryLogRecord(long sequence, int count) {
                return storage.read(sequence, count);
        }

        public long maxSequence() {
                return storage.maxSequence();
        }

        // TODO 处理并发。
        private long nextSequence() {
                return sequence++;
        }

        private long currentTime() {
                return System.currentTimeMillis();
        }

        public List<LogRecord> readAll() {
                return null;
        }

        public LogRecord read() {
                return null;
        }
}
