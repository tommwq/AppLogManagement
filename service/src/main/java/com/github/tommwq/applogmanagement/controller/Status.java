package com.github.tommwq.applogmanagement.controller;

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
}
        
