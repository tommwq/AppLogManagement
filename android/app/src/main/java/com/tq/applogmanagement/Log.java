package com.tq.applogmanagement;

import com.tq.applogmanagement.agent.LogAgent;
import com.tq.applogmanagement.Logger;
import com.tq.applogmanagement.SimpleLogger;
import com.tq.applogmanagement.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import com.tq.applogmanagement.*;
import com.tq.applogmanagement.AppLogManagementProto.*;

public class Log implements Logger {

  private Logger delegate = SimpleLogger.instance();
  private LogAgent agent;

  public Log(String aHost, int aPort) {
    agent = new LogAgent(aHost, aPort);
  }

  // public void setSubscriber(LogRecordSubscriber aSubscriber) {
  //   delegate.setSubscriber(aSubscriber);
  // }

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
    delegate.close();
    try {
    agent.shutdown();
    } catch (Exception e) {
      android.util.Log.e("TEST", "shutdown failed", e);
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


