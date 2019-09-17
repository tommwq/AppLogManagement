package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Empty;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import java.util.List;

public interface DeviceLogManagementServiceApi {
        void open();
        void close();
        void deviceConnect(DeviceAndAppInfo info, Object context);
        void deviceDisconnect(String deviceId);
        void deviceDisconnect(String deviceId, Throwable error);
        List<String> onlineDeviceIdList();
        void saveLog(String deviceId, LogRecord log);
        List<LogRecord> loadLog(String deviceId);
        Object getContext(String deviceId);
}
