package com.github.tommwq.applogmanagement;

import android.util.Log;
import android.content.Context;
import com.github.tommwq.applogmanagement.agent.LogReportAgent;
import com.github.tommwq.applogmanagement.logging.Logger;
import com.github.tommwq.applogmanagement.logging.SimpleLogger;
import com.github.tommwq.applogmanagement.*;
import com.github.tommwq.applogmanagement.storage.*;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage.Config;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import com.github.tommwq.applogmanagement.*;
import com.github.tommwq.applogmanagement.AppLogManagementProto.*;

public class AndroidLogger extends Logger {

        private Logger delegate = SimpleLogger.instance();
        private LogReportAgent agent;
        private String tag;

        public void write(LogRecord record) {
                delegate.write(record);
        }
  
        public AndroidLogger(String aHost, int aPort, Context context) {
                agent = new LogReportAgent(aHost, aPort, this);
                tag = context.getPackageName();
        }

        public void open(BlockStorage storage, DeviceAndAppConfig aInfo) {
                delegate.open(storage, aInfo);
                try {
                        agent.start();
                } catch (Exception e) {
                        android.util.Log.e("TEST", "start failed", e);
                }
        }
  
        public LogRecord deviceAndAppInfoLog() {
                // return delegate.deviceAndAppInfoLog();
                return null;
        }

        public void close() {
                try {
                        agent.shutdown();
                } catch (Exception e) {
                        android.util.Log.e("TEST", "shutdown failed", e);
                }

                try {
                        delegate.close();
                } catch (Exception e) {

                }
        }
  
        public void trace() {
                delegate.trace();
        }
  
        public void log(String message, Object... parameters) {
                delegate.log(message, parameters);
        }
        public void print(Object... parameters) {
                android.util.Log.d("TEST", String.join(", ", Stream.of(parameters)
                                                       .map(x -> x == null ? "null" : x.toString())
                                                       .collect(Collectors.toList())));
                delegate.print(parameters);
        }
        public void error(Throwable error) {
                delegate.error(error);
        }
        public List<LogRecord> queryLogRecord(long sequence, int count) {
                // return delegate.queryLogRecord(sequence, count);
                return null;
        }
        public long maxSequence() {
                // return delegate.maxSequence();
                return 0;
        }  
}


