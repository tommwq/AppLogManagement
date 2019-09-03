package com.github.tommwq.applogmanagement.http.codec;

import com.github.tommwq.applogmanagement.AppLogManagementProto.*;
import com.github.tommwq.applogmanagement.http.*;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

public class LogRecordCodec {

        public static com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord toProto(com.github.tommwq.applogmanagement.http.LogRecordHttp pojo) {

                Object body = pojo.getBody();
                Any protoBody = null;
                if (body instanceof DeviceAndAppInfoHttp) {
                        protoBody = Any.pack(DeviceAndAppInfoCodec.toProto((DeviceAndAppInfoHttp) body));
                } else if (body instanceof ExceptionInfoHttp) {
                        protoBody = Any.pack(ExceptionInfoCodec.toProto((ExceptionInfoHttp) body));
                } else if (body instanceof MethodAndObjectInfoHttp) {
                        protoBody = Any.pack(MethodAndObjectInfoCodec.toProto((MethodAndObjectInfoHttp) body));
                } else if (body instanceof UserDefinedMessageHttp) {
                        protoBody = Any.pack(UserDefinedMessageCodec.toProto((UserDefinedMessageHttp) body));
                }
                
                return com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord.newBuilder()
                        .setHeader(com.github.tommwq.applogmanagement.http.codec.LogHeaderCodec.toProto(pojo.getHeader()))
                        .setBody(protoBody)
                        .build();
        }

        public static com.github.tommwq.applogmanagement.http.LogRecordHttp toPojo(com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord proto) {
                com.github.tommwq.applogmanagement.http.LogRecordHttp pojo = new com.github.tommwq.applogmanagement.http.LogRecordHttp();
                pojo.setHeader(com.github.tommwq.applogmanagement.http.codec.LogHeaderCodec.toPojo(proto.getHeader()));
                Any body = proto.getBody();
                Object pojoBody = null;

                try {
                        if (body.is(DeviceAndAppInfo.class)) {
                                pojoBody = body.unpack(DeviceAndAppInfo.class);
                        } else if (body.is(ExceptionInfo.class)) {
                                pojoBody = body.unpack(ExceptionInfo.class);
                        } else if (body.is(MethodAndObjectInfo.class)) {
                                pojoBody = body.unpack(MethodAndObjectInfo.class);
                        } else if (body.is(UserDefinedMessage.class)) {
                                pojoBody = body.unpack(UserDefinedMessage.class);
                        }
                } catch (InvalidProtocolBufferException e) {
                        // ignore
                }

                pojo.setBody(pojoBody);
                return pojo;
        }
}
