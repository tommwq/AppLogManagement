package com.github.tommwq.applogmanagement.http;

public class MethodInfoHttp {

        private String sourceFile;
        private int lineNumber;
        private String className;
        private String methodName;

        public String getSourceFile() {
                return sourceFile;
        }

        public void setSourceFile(String value) {
                sourceFile = value;
        }
        public int getLineNumber() {
                return lineNumber;
        }

        public void setLineNumber(int value) {
                lineNumber = value;
        }
        public String getClassName() {
                return className;
        }

        public void setClassName(String value) {
                className = value;
        }
        public String getMethodName() {
                return methodName;
        }

        public void setMethodName(String value) {
                methodName = value;
        }
}
