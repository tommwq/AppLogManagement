package com.github.tommwq.applogmanagement.logging;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;

public interface LoggerApi {
        void log(String message, Object... parameters);
        void print(Object... parameters);
        void trace();  
        void error(Throwable error);
}
