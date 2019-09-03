package com.github.tommwq.applogmanagement.http.codec;

public class ObjectInfoCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.ObjectInfo toProto(com.github.tommwq.applogmanagement.http.ObjectInfoHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.ObjectInfo.newBuilder()
            .setObjectType(pojo.getObjectType())
            .setObjectValue(pojo.getObjectValue())
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.ObjectInfoHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.ObjectInfo proto) {
        com.github.tommwq.applogmanagement.http.ObjectInfoHttp pojo = new com.github.tommwq.applogmanagement.http.ObjectInfoHttp();
        pojo.setObjectType(proto.getObjectType());
        pojo.setObjectValue(proto.getObjectValue());
        return pojo;
    }
}