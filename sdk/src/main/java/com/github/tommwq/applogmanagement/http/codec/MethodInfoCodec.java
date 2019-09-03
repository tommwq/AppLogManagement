package com.github.tommwq.applogmanagement.http.codec;

public class MethodInfoCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.MethodInfo toProto(com.github.tommwq.applogmanagement.http.MethodInfoHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.MethodInfo.newBuilder()
            .setSourceFile(pojo.getSourceFile())
            .setLineNumber(pojo.getLineNumber())
            .setClassName(pojo.getClassName())
            .setMethodName(pojo.getMethodName())
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.MethodInfoHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.MethodInfo proto) {
        com.github.tommwq.applogmanagement.http.MethodInfoHttp pojo = new com.github.tommwq.applogmanagement.http.MethodInfoHttp();
        pojo.setSourceFile(proto.getSourceFile());
        pojo.setLineNumber(proto.getLineNumber());
        pojo.setClassName(proto.getClassName());
        pojo.setMethodName(proto.getMethodName());
        return pojo;
    }
}