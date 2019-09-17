package com.github.tommwq.applogmanagement.api;

import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import java.util.List;

public interface DeviceInfoRepositoryApi {

        public static class DeviceInfo {
                public String deviceId;
                public String deviceInfo;
                public int isOnline; // 0 for false, 1 for true

                public DeviceInfo() {
                }
                
                public DeviceInfo(DeviceAndAppInfo aInfo) {
                        deviceId = aInfo.getDeviceId();
                        deviceInfo = aInfo.toString();
                        isOnline = 1;
                }

                public String toString() {
                        return String.format("<DeviceInfo deviceId=%s isOnline=%d deviceInfo=%s>",
                                             deviceId,
                                             isOnline,
                                             deviceInfo);
                }
        }
        
        void deviceConnect(DeviceAndAppInfo info);
        void deviceDisconnect(String deviceId);
        List<DeviceInfo> loadAll();
}
