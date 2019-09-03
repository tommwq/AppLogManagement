package com.github.tommwq.applogmanagement.http.codec;

public class ExceptionInfoCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.ExceptionInfo toProto(com.github.tommwq.applogmanagement.http.ExceptionInfoHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.ExceptionInfo.newBuilder()
            .setException(com.github.tommwq.applogmanagement.http.codec.ObjectInfoCodec.toProto(pojo.getException()))
            .addAllStack(pojo.getStack().stream().map(com.github.tommwq.applogmanagement.http.codec.MethodInfoCodec::toProto).collect(java.util.stream.Collectors.toList()))
            .build();
    }

    public static com.github.tommwq.applogmanagement.http.ExceptionInfoHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.ExceptionInfo proto) {
        com.github.tommwq.applogmanagement.http.ExceptionInfoHttp pojo = new com.github.tommwq.applogmanagement.http.ExceptionInfoHttp();
        pojo.setException(com.github.tommwq.applogmanagement.http.codec.ObjectInfoCodec.toPojo(proto.getException()));
        pojo.setStack(proto.getStackList().stream().map(com.github.tommwq.applogmanagement.http.codec.MethodInfoCodec::toPojo).collect(java.util.stream.Collectors.toList()));
        return pojo;
    }
}