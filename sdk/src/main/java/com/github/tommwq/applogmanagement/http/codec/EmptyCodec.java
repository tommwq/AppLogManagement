package com.github.tommwq.applogmanagement.http.codec;

public class EmptyCodec {

    public static com.github.tommwq.applogmanagement.AppLogManagementProto.Empty toProto(com.github.tommwq.applogmanagement.http.EmptyHttp pojo) {
        return com.github.tommwq.applogmanagement.AppLogManagementProto.Empty.newBuilder()

            .build();
    }

    public static com.github.tommwq.applogmanagement.http.EmptyHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.Empty proto) {
        com.github.tommwq.applogmanagement.http.EmptyHttp pojo = new com.github.tommwq.applogmanagement.http.EmptyHttp();

        return pojo;
    }
}