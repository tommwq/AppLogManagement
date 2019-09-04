package com.github.tommwq.applogmanagement.controller;

import com.github.tommwq.applogmanagement.AppLogManagementProto.Command;
import com.github.tommwq.applogmanagement.AppLogManagementProto.DeviceAndAppInfo;
import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.http.codec.LogRecordCodec;
import com.github.tommwq.applogmanagement.http.codec.DeviceAndAppInfoCodec;
import com.github.tommwq.applogmanagement.http.DeviceAndAppInfoHttp;
import com.github.tommwq.applogmanagement.http.LogRecordHttp;
import com.github.tommwq.applogmanagement.LogManagementServer;
import com.github.tommwq.applogmanagement.LogManagementService;
import com.github.tommwq.applogmanagement.LogManagementServiceGrpc;
import com.github.tommwq.utility.network.SocketAddressParser;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.github.tommwq.applogmanagement.repository.LogRecordRepository;
import com.github.tommwq.applogmanagement.repository.MemoryLogRecordRepository;
import com.github.tommwq.applogmanagement.LogSession;
import java.util.logging.Logger;

@RestController
public class ApiController {

        private static final Logger logger = Logger.getGlobal();

        @Autowired
        private LogManagementServer server;

        @Autowired
        Status status;

        @Value("${peer.address}")
        String peerAddress;

        @RequestMapping(value="/api/devices")
        @ResponseBody
        public Set<String> device() {
                return server.getService().getOnlineDeviceIdSet();
        }

        @RequestMapping(value="/api/log/{deviceId}")
        @ResponseBody
        public List<LogRecordHttp>log(@PathVariable("deviceId") String deviceId) throws Exception {
                LogSession session = server.getService().getLogSession(deviceId);
                if (session != null) {
                        session.command();
                }
      
                LogRecordRepository repository = MemoryLogRecordRepository.instance();
                        
                return repository.load(deviceId, 0L, 0)
                        .stream()
                        .map(LogRecordCodec::toPojo)
                        .collect(Collectors.toList());
        }
        
        @RequestMapping("/api/status")
        @ResponseBody
        public Status status() {
                return status;
        }

        @RequestMapping("/api/lookup/{deviceId}")
        @ResponseBody
        public List<DeviceAndAppInfoHttp> lookup(@PathVariable("deviceId") String deviceId) {
                System.out.println("lookup");
                Stream<ManagedChannel> channelStream = Stream.of(peerAddress.split(","))
                        .map(address -> new SocketAddressParser(address))
                        // TODO 
                        .filter(x -> x.port() != status.nodePort)
                        .map(x -> {
                                        System.out.println(x.address() + x.port());
                                        return ManagedChannelBuilder.forAddress(x.address(), x.port())
                                                .usePlaintext()
                                                .build();
                                });

                System.out.println("lookup 2");

                List<DeviceAndAppInfoHttp> infoList = channelStream.map(channel -> LogManagementServiceGrpc.newBlockingStub(channel))
                        .map(stub -> {
                                        System.out.println("lookup 2.1");
                                        return stub.queryDeviceInfo(Command.newBuilder()
                                                                    .setDeviceId(deviceId)
                                                                    .build());
                                })
                        .map(x -> {
                                        System.out.println("lookup 2.2");
                                        return DeviceAndAppInfoCodec.toPojo(x);
                                })
                        .collect(Collectors.toList());

                System.out.println("lookup 3");

                // TODO
                // channelStream.forEach(ManagedChannel::shutdown);
                return infoList;
        }
}
