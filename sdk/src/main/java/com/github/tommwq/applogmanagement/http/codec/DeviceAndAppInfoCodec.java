package com.github.tommwq.applogmanagement.http.codec;

public class DeviceAndAppInfoCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo toProto(com.github.tommwq.applogmanagement.http.DeviceAndAppInfoHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo.newBuilder()
            .setDeviceId(pojo.getDeviceId())
            .setDeviceVersion(pojo.getDeviceVersion())
            .setBaseOsName(pojo.getBaseOsName())
            .setBaseOsVersion(pojo.getBaseOsVersion())
            .setOsName(pojo.getOsName())
            .setOsVersion(pojo.getOsVersion())
            .setAppVersion(pojo.getAppVersion())
            .addAllModuleInfo(pojo.getModuleInfo().stream().map(com.github.tommwq.applogmanagement.http.codec.ModuleInfoCodec::toProto).collect(java.util.stream.Collectors.toList()))
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.DeviceAndAppInfoHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo proto) {
        com.github.tommwq.applogmanagement.http.DeviceAndAppInfoHttp pojo = new com.github.tommwq.applogmanagement.http.DeviceAndAppInfoHttp();
        pojo.setDeviceId(proto.getDeviceId());
        pojo.setDeviceVersion(proto.getDeviceVersion());
        pojo.setBaseOsName(proto.getBaseOsName());
        pojo.setBaseOsVersion(proto.getBaseOsVersion());
        pojo.setOsName(proto.getOsName());
        pojo.setOsVersion(proto.getOsVersion());
        pojo.setAppVersion(proto.getAppVersion());
        pojo.setModuleInfo(proto.getModuleInfoList().stream().map(com.github.tommwq.applogmanagement.http.codec.ModuleInfoCodec::toPojo).collect(java.util.stream.Collectors.toList()));
        return pojo;
    }
}