package com.github.tommwq.applogmanagement.http;

public class LogRecordHttp {

        private com.github.tommwq.applogmanagement.http.LogHeaderHttp header;
        private Object body;

        public com.github.tommwq.applogmanagement.http.LogHeaderHttp getHeader() {
                return header;
        }

        public void setHeader(com.github.tommwq.applogmanagement.http.LogHeaderHttp value) {
                header = value;
        }
        public Object getBody() {
                return body;
        }

        public void setBody(Object value) {
                body = value;
        }
}
