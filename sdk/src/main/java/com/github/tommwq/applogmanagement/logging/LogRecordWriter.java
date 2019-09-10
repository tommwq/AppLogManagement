package com.github.tommwq.applogmanagement.logging;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;

public interface LogRecordWriter {
        void write(LogRecord record);
}
