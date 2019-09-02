package com.tq.applogmanagement;

import android.util.Log;
import android.content.Context;
import com.tq.applogmanagement.agent.LogReportAgent;
import com.tq.applogmanagement.Logger;
import com.tq.applogmanagement.SimpleLogger;
import com.tq.applogmanagement.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import com.tq.applogmanagement.*;
import com.tq.applogmanagement.AppLogManagementProto.*;

public class AndroidLogger implements Logger {

  private Logger delegate = SimpleLogger.instance();
  private LogReportAgent agent;
  private String tag;
  
  public AndroidLogger(String aHost, int aPort, Context context) {
    agent = new LogReportAgent(aHost, aPort, this);
    tag = context.getPackageName();
  }

  public void open(StorageConfig aConfig, DeviceAndAppConfig aInfo) {
    delegate.open(aConfig, aInfo);
    try {
      agent.start();
    } catch (Exception e) {
      android.util.Log.e("TEST", "start failed", e);
    }
  }
  
  public LogRecord deviceAndAppInfoLog() {
    return delegate.deviceAndAppInfoLog();
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
    return delegate.queryLogRecord(sequence, count);
  }
  public long maxSequence() {
    return delegate.maxSequence();
  }  
}


