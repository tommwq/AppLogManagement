package com.github.tommwq.applogmanagement.controller;

import com.github.tommwq.applogmanagement.AppLogManagementProto.LogRecord;
import com.github.tommwq.applogmanagement.http.codec.LogRecordCodec;
import com.github.tommwq.applogmanagement.http.LogRecordHttp;
import com.github.tommwq.applogmanagement.LogManagementServer;
import com.github.tommwq.applogmanagement.LogManagementService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

        @RequestMapping(value="/api/devices")
        @ResponseBody
        public Set<String> device() {
                return server.getService().getOnlineDeviceIdSet();
        }

        @RequestMapping(value="/api/log/{deviceId}")
        @ResponseBody
        public List<Object>log(@PathVariable("deviceId") String deviceId) throws Exception {
                LogSession session = server.getService().getLogSession(deviceId);
                if (session != null) {
                        session.command();
                }
      
                LogRecordRepository repository = new MemoryLogRecordRepository();
                        
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

        public class LookupResult {
                public boolean exist = false;
        }

        @RequestMapping("/api/lookup/{deviceId}")
        @ResponseBody
        public LookupResult lookup(@PathVariable("deviceId") String deviceId) {
                LookupResult result = new LookupResult();
                result.exist = server.getService()
                        .getOnlineDeviceIdSet()
                        .contains(deviceId);

                return result;
        }
}
