package com.tq.applogmanagement.api;

import com.tq.applogmanagement.AppLogManagementProto.Empty;
import com.tq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.tq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface DeviceLogManagementService {
  void open();
  void close();
  void onDeviceConnected(DeviceAndAppInfo info);
  void onDeviceDisconnected(String deviceId);
  List<String> onlineDeviceIdList();
  List<LogRecord> readLogCache(String deviceId);
  void appendLogCache(String deviceId, List<LogRecord> logList);
  void appendLogCache(String deviceId, LogRecord log);
}
