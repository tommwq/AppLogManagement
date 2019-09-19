package com.github.tommwq.applogmanagement.controller;

import com.github.tommwq.utility.ProcessUtil;
import com.github.tommwq.utility.network.NetworkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Status {
        @Value("${node.port}")
        public int nodePort;

        @Value("${server.port}")
        public int serverPort;

        @Value("${peer.address}")
        public String peerAddress;

        public long startTime;
        public long pid;
        public String hostAddress = "";

        public Status refresh() {
                pid = ProcessUtil.pid();
                startTime = ProcessUtil.startTime();
                try {
                        hostAddress = NetworkUtil.hostAddress();
                } catch (Exception e) {
                        // ignore
                }
                return this;
        }
}
        
