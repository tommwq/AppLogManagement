package com.github.tommwq.applogmanagement.http.codec;

public class LogRecordCodec {

        public static com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord toProto(com.github.tommwq.applogmanagement.http.LogRecordHttp pojo) {
                return com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord.newBuilder()
                        .setHeader(com.github.tommwq.applogmanagement.http.codec.LogHeaderCodec.toProto(pojo.getHeader()))
                        // TODO .setBody(google.protobuf.http.codec.AnyCodec.toProto(pojo.getBody()))
                        .build();
        }

        public static com.github.tommwq.applogmanagement.http.LogRecordHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord proto) {
                com.github.tommwq.applogmanagement.http.LogRecordHttp pojo = new com.github.tommwq.applogmanagement.http.LogRecordHttp();
                pojo.setHeader(com.github.tommwq.applogmanagement.http.codec.LogHeaderCodec.toPojo(proto.getHeader()));
                // TODO pojo.setBody(google.protobuf.http.codec.AnyCodec.toPojo(proto.getBody()));
                return pojo;
        }
}
