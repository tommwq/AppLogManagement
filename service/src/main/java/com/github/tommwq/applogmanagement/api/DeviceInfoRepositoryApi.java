package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import java.util.List;

public interface DeviceInfoRepositoryApi {

        public static class DeviceInfo {
                public String deviceId;
                public String deviceInfo;
                public boolean isOnline;
                public DeviceInfo(DeviceAndAppInfo aInfo) {
                        deviceId = aInfo.getDeviceId();
                        deviceInfo = aInfo.toString();
                        isOnline = true;
                }
        }
        
        void deviceConnect(DeviceAndAppInfo info);
        void deviceDisconnect(String deviceId);
        List<DeviceInfo> loadAll();
}
