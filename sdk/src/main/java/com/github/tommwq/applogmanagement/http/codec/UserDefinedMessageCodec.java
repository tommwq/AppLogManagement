package com.github.tommwq.applogmanagement.http.codec;

public class UserDefinedMessageCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.UserDefinedMessage toProto(com.github.tommwq.applogmanagement.http.UserDefinedMessageHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.UserDefinedMessage.newBuilder()
            .setSourceFile(pojo.getSourceFile())
            .setLineNumber(pojo.getLineNumber())
            .setPackageName(pojo.getPackageName())
            .setClassName(pojo.getClassName())
            .setMethodName(pojo.getMethodName())
            .setUserDefinedMessage(pojo.getUserDefinedMessage())
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.UserDefinedMessageHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.UserDefinedMessage proto) {
        com.github.tommwq.applogmanagement.http.UserDefinedMessageHttp pojo = new com.github.tommwq.applogmanagement.http.UserDefinedMessageHttp();
        pojo.setSourceFile(proto.getSourceFile());
        pojo.setLineNumber(proto.getLineNumber());
        pojo.setPackageName(proto.getPackageName());
        pojo.setClassName(proto.getClassName());
        pojo.setMethodName(proto.getMethodName());
        pojo.setUserDefinedMessage(proto.getUserDefinedMessage());
        return pojo;
    }
}