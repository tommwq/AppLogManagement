package com.github.tommwq.applogmanagement.http.codec;

public class MethodAndObjectInfoCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.MethodAndObjectInfo toProto(com.github.tommwq.applogmanagement.http.MethodAndObjectInfoHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.MethodAndObjectInfo.newBuilder()
            .setMethod(com.github.tommwq.applogmanagement.http.codec.MethodInfoCodec.toProto(pojo.getMethod()))
            .addAllVariable(pojo.getVariable().stream().map(com.github.tommwq.applogmanagement.http.codec.ObjectInfoCodec::toProto).collect(java.util.stream.Collectors.toList()))
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.MethodAndObjectInfoHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.MethodAndObjectInfo proto) {
        com.github.tommwq.applogmanagement.http.MethodAndObjectInfoHttp pojo = new com.github.tommwq.applogmanagement.http.MethodAndObjectInfoHttp();
        pojo.setMethod(com.github.tommwq.applogmanagement.http.codec.MethodInfoCodec.toPojo(proto.getMethod()));
        pojo.setVariable(proto.getVariableList().stream().map(com.github.tommwq.applogmanagement.http.codec.ObjectInfoCodec::toPojo).collect(java.util.stream.Collectors.toList()));
        return pojo;
    }
}