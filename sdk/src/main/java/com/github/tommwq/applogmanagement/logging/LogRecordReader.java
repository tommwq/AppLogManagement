package com.github.tommwq.applogmanagement.logging;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface LogRecordReader {

        LogRecord read();
        List<LogRecord> readAll();
        long maxLsn();
        long minLsn();
        LogRecordReader moveTo(long sequence);
        LogRecord read(long sequence);
        List<LogRecord> read(long sequence, int count);
}
