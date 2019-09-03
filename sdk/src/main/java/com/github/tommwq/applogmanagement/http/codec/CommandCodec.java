package com.github.tommwq.applogmanagement.http.codec;

public class CommandCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.Command toProto(com.github.tommwq.applogmanagement.http.CommandHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.Command.newBuilder()
            .setDeviceId(pojo.getDeviceId())
            .setSequence(pojo.getSequence())
            .setCount(pojo.getCount())
            .setPackageName(pojo.getPackageName())
            .setIncludeSubPackage(pojo.getIncludeSubPackage())
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.CommandHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.Command proto) {
        com.github.tommwq.applogmanagement.http.CommandHttp pojo = new com.github.tommwq.applogmanagement.http.CommandHttp();
        pojo.setDeviceId(proto.getDeviceId());
        pojo.setSequence(proto.getSequence());
        pojo.setCount(proto.getCount());
        pojo.setPackageName(proto.getPackageName());
        pojo.setIncludeSubPackage(proto.getIncludeSubPackage());
        return pojo;
    }
}