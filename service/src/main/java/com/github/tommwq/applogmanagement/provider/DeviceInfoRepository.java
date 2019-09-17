package com.github.tommwq.applogmanagement.provider;

import com.github.tommwq.applogmanagement.api.DeviceInfoRepositoryApi;
import com.github.tommwq.applogmanagement.api.DeviceInfoRepositoryApi.DeviceInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.utility.collection.MapUtil;
import com.github.tommwq.utility.database.SQLiteHelper;
import com.github.tommwq.utility.function.Call;
import java.sql.Connection;
import java.util.List;

public class DeviceInfoRepository implements DeviceInfoRepositoryApi {

        private SQLiteHelper helper;
        public DeviceInfoRepository(Connection connection) {
                helper = new SQLiteHelper(connection);
                new Call(() -> helper.createTableInNeed(DeviceInfoRepositoryApi.DeviceInfo.class)).rethrow();
        }

        public void deviceConnect(DeviceAndAppInfo info) {
                new Call(() -> helper.insert(new DeviceInfo(info))).rethrow();                
        }

        public void deviceDisconnect(String deviceId) {
                new Call(() -> helper.delete(DeviceInfo.class, MapUtil.<String>asMap("deviceId", deviceId))).rethrow();
        }

        public List<DeviceInfo> loadAll() {
                return (List<DeviceInfo>) new Call((Void) -> helper.select(DeviceInfo.class), null, null).rethrow().result();
        }
}
