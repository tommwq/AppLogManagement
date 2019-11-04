package com.github.tommwq.applogmanagement.logging;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogHeader;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.logging.LogRecordStorage;
import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;

public class FlushThread extends Thread {

        public static final LogRecord END_OF_QUEUE = LogRecord.newBuilder()
                .setHeader(LogHeader.newBuilder().setSequence(0L).build())
                .build();

        private LinkedTransferQueue<LogRecord> queue;
        private LogRecordStorage storage;
        
        public FlushThread(LinkedTransferQueue<LogRecord> aQueue, LogRecordStorage aStorage) {
                queue = aQueue;
                storage = aStorage;
        }
        
        public void run() {
                while (true) {
                        try {                        
                                LogRecord record = queue.take();
                                if (record == END_OF_QUEUE) {
                                        break;
                                }
                        
                                storage.write(record);
                        } catch (InterruptedException e) {
                                // ignore
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                        }
                }
        }
}

