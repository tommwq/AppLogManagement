package com.github.tommwq.applogmanagement.logging;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ExceptionInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogHeader;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogType;
import com.github.tommwq.applogmanagement.AppLogManagementProto.MethodAndObjectInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.MethodInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.ObjectInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.UserDefinedMessage;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage;
import com.github.tommwq.utility.StringUtil;
import com.github.tommwq.utility.Util;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.github.tommwq.applogmanagement.DeviceAndAppConfig;

public class LogUtil {
  
        public static LogRecord newDeviceAndAppInfoLog(long sequence, DeviceAndAppConfig info) {
                return LogRecord.newBuilder()
                        .setHeader(LogHeader.newBuilder()
                                   .setSequence(sequence)
                                   .setTime(Util.currentTime())
                                   .setLogType(LogType.DEVICE_AND_APP_INFO)
                                   .build())
                        .setBody(Any.pack(DeviceAndAppInfo.newBuilder()
                                          .setDeviceId(info.getDeviceId())
                                          .setDeviceVersion(info.getDeviceVersion())
                                          .setBaseOsName(info.getBaseOsName())
                                          .setBaseOsVersion(info.getBaseOsVersion())
                                          .setOsName(info.getOsName())
                                          .setOsVersion(info.getOsVersion())
                                          .setAppVersion(info.getAppVersion())
                                          .addAllModuleInfo(info.getModuleVersions()
                                                            .entrySet()
                                                            .stream()
                                                            .map(entry -> ModuleInfo.newBuilder()
                                                                 .setModuleName(entry.getKey())
                                                                 .setModuleVersion(entry.getValue())
                                                                 .build())
                                                            .collect(Collectors.toList()))
                                          .build()))
                        .build();
        }
  
        public static LogRecord newMethodAndObjectInfoLog(long sequence, StackTraceElement frame, Object... variables) {
                return LogRecord.newBuilder()
                        .setHeader(LogHeader.newBuilder()
                                   .setSequence(sequence)
                                   .setTime(Util.currentTime())
                                   .setLogType(LogType.METHOD_AND_OBJECT_INFO)
                                   .build())
                        .setBody(Any.pack(MethodAndObjectInfo.newBuilder()
                                          .setMethod(MethodInfo.newBuilder()
                                                     .setSourceFile(frame.getFileName())
                                                     .setLineNumber(frame.getLineNumber())
                                                     .setClassName(frame.getClassName())
                                                     .setMethodName(frame.getMethodName())
                                                     .build())
                                          .addAllVariable(Stream.of(variables)
                                                          .map(x -> ObjectInfo.newBuilder()
                                                               .setObjectType(x == null ? "Object" : x.getClass().getName())
                                                               .setObjectValue(x == null ? "null" : x.toString())
                                                               .build())
                                                          .collect(Collectors.toList()))
                                          .build()))
                        .build();
        }
  
        public static LogRecord newExceptionInfoLog(long sequence, Throwable exception) {
                return LogRecord.newBuilder()
                        .setHeader(LogHeader.newBuilder()
                                   .setSequence(sequence)
                                   .setTime(Util.currentTime())
                                   .setLogType(LogType.EXCEPTION_INFO)
                                   .build())
                        .setBody(Any.pack(ExceptionInfo.newBuilder()
                                          .setException(ObjectInfo.newBuilder()
                                                        .setObjectType(exception.getClass().getName())
                                                        .setObjectValue(exception.toString())
                                                        .build())
                                          .addAllStack(Stream.of(exception.getStackTrace())
                                                       .map(frame -> MethodInfo.newBuilder()
                                                            .setSourceFile(frame.getFileName())
                                                            .setLineNumber(frame.getLineNumber())
                                                            .setClassName(frame.getClassName())
                                                            .setMethodName(frame.getMethodName())
                                                            .build())
                                                       .collect(Collectors.toList()))
                                          .build()))
                        .build();
        }

        public static LogRecord newUserDefinedLog(long sequence, StackTraceElement frame, String message) {
                return LogRecord.newBuilder()
                        .setHeader(LogHeader.newBuilder()
                                   .setSequence(sequence)
                                   .setTime(Util.currentTime())
                                   .setLogType(LogType.USER_DEFINED)
                                   .build())
                        .setBody(Any.pack(UserDefinedMessage.newBuilder()
                                          .setSourceFile(frame.getFileName())
                                          .setLineNumber(frame.getLineNumber())
                                          .setClassName(frame.getClassName())
                                          .setMethodName(frame.getMethodName())
                                          .setUserDefinedMessage(message)
                                          .build()))
                        .build();
        }

        public static void printLog(LogRecord log, PrintStream printStream) {
                printStream.print(log.getHeader());
                Any any = log.getBody();

                Stream.of(DeviceAndAppInfo.class,
                          ExceptionInfo.class,
                          MethodAndObjectInfo.class,
                          UserDefinedMessage.class)
                        .forEach(clazz -> {
                                        if (any.is(clazz)) {
                                                try {
                                                        printStream.print(any.unpack(clazz));
                                                } catch (InvalidProtocolBufferException e) {
                                                        // ignore
                                                }
                                        }
                                });
                printStream.println();
        }

}
