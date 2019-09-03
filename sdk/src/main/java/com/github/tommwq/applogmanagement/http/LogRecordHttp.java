package com.github.tommwq.applogmanagement.http;

public class LogRecordHttp {

        private com.github.tommwq.applogmanagement.http.LogHeaderHttp Header;
        private Object Body;

        public com.github.tommwq.applogmanagement.http.LogHeaderHttp getHeader() {
                return Header;
        }

        public void setHeader(com.github.tommwq.applogmanagement.http.LogHeaderHttp value) {
                Header = value;
        }
        public Object getBody() {
                return Body;
        }

        public void setBody(Object value) {
                Body = value;
        }
}
