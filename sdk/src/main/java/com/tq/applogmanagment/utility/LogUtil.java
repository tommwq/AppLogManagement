package com.tq.applogmanagement.utility;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.tq.applogmanagement.AppLogManagementProto.Command;
import com.tq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.tq.applogmanagement.AppLogManagementProto.ExceptionInfo;
import com.tq.applogmanagement.AppLogManagementProto.Log;
import com.tq.applogmanagement.AppLogManagementProto.LogHeader;
import com.tq.applogmanagement.AppLogManagementProto.LogType;
import com.tq.applogmanagement.AppLogManagementProto.MethodAndObjectInfo;
import com.tq.applogmanagement.AppLogManagementProto.MethodInfo;
import com.tq.applogmanagement.AppLogManagementProto.ModuleInfo;
import com.tq.applogmanagement.AppLogManagementProto.ObjectInfo;
import com.tq.applogmanagement.AppLogManagementProto.UserDefinedMessage;
import com.tq.applogmanagement.storage.LogStorage;
import com.tq.applogmanagement.storage.SimpleBlockStorage;
import com.tq.applogmanagement.utility.StringUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedTransferQueue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.tq.applogmanagement.Logger.LogSubscriber;
import com.tq.applogmanagement.DeviceAndAppConfig;

public class LogUtil {
  
  public static Log newDeviceAndAppInfoLog(long sequence, DeviceAndAppConfig info) {
    return Log.newBuilder()
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
  
  public static Log newMethodAndObjectInfoLog(long sequence, StackTraceElement frame, Object... variables) {
    return Log.newBuilder()
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
  
  public static Log newExceptionInfoLog(long sequence, Throwable exception) {
    return Log.newBuilder()
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

  public static Log newUserDefinedLog(long sequence, StackTraceElement frame, String message) {
    return Log.newBuilder()
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
}
