package com.github.tommwq.applogmanagement.http.codec;

public class LogHeaderCodec {

        public static com.github.tommwq.applogmanagement.AppLogManagementProto.LogHeader toProto(com.github.tommwq.applogmanagement.http.LogHeaderHttp pojo) {
                return com.github.tommwq.applogmanagement.AppLogManagementProto.LogHeader.newBuilder()
                        .setSequence(pojo.getSequence())
                        .setTime(pojo.getTime())
                        .setLogType(com.github.tommwq.applogmanagement.AppLogManagementProto.LogType.forNumber(pojo.getLogType()))
                        .build();
        }

        public static com.github.tommwq.applogmanagement.http.LogHeaderHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.LogHeader proto) {
                com.github.tommwq.applogmanagement.http.LogHeaderHttp pojo = new com.github.tommwq.applogmanagement.http.LogHeaderHttp();
                pojo.setSequence(proto.getSequence());
                pojo.setTime(proto.getTime());
                pojo.setLogType(proto.getLogType().getNumber());
                return pojo;
        }
}
