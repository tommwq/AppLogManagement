package com.github.tommwq.applogmanagement;

import com.github.tommwq.applogmanagement.controller.Status;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

@Component
public class LogManagementServer implements InitializingBean {

        @Value("${node.port}")
        public int port = -1;
        
        private Server underlyingServer = null;
        private LogManagementService service;

        public LogManagementService getService() {
                return service;
        }

        public int port() {
                return port;
        }

        @Override
        public void afterPropertiesSet() {
                try {
                        System.out.println("LogManagementService started on port: " + port());
                        service = new LogManagementService();
                        underlyingServer = ServerBuilder.forPort(port())
                                .addService(service)
                                .build();
                        underlyingServer.start();
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        public void destroy() {
                if (underlyingServer != null) {
                        underlyingServer.shutdown();
                }
        }
}
