package com.github.tommwq.applogmanagement.http.codec;

public class ModuleInfoCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo toProto(com.github.tommwq.applogmanagement.http.ModuleInfoHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo.newBuilder()
            .setModuleName(pojo.getModuleName())
            .setModuleVersion(pojo.getModuleVersion())
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.ModuleInfoHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo proto) {
        com.github.tommwq.applogmanagement.http.ModuleInfoHttp pojo = new com.github.tommwq.applogmanagement.http.ModuleInfoHttp();
        pojo.setModuleName(proto.getModuleName());
        pojo.setModuleVersion(proto.getModuleVersion());
        return pojo;
    }
}