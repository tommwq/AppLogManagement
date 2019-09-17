package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface DeviceLogManagementServiceApi {
        void open();
        void close();
        void onDeviceConnected(DeviceAndAppInfo info);
        void onDeviceDisconnected(String deviceId);
        List<String> onlineDeviceIdList();
        List<LogRecord> readLogCache(String deviceId);
        void appendLogCache(String deviceId, List<LogRecord> logList);
        void appendLogCache(String deviceId, LogRecord log);

}
