package com.github.tommwq.applogmanagement.logging;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.DeviceAndAppConfig;
import  com.github.tommwq.applogmanagement.storage.BlockStorage;

public abstract class Logger implements LoggerApi, LogRecordWriter {
        
        public abstract void open(BlockStorage storage, DeviceAndAppConfig config);
        public abstract void close();
}
