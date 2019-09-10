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
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFlushThread extends Thread {
        
        public void run() {
                // TODO
                // try {
                //         while (true) {
                //                 LogRecord log = instance.logQueue.take();
                //                 instance.write(log);
                //         }
                // } catch (InterruptedException e) {
                //         ArrayList<LogRecord> tails = new ArrayList<>();
                //         tails.addAll(instance.logQueue);
                //         tails.stream().forEach(log -> {try {instance.write(log);} catch (Exception ex){}});
                // } finally {
                //         // SimpleLogger.instance().backgroundWriteThread = null;
                // }
        }
}

