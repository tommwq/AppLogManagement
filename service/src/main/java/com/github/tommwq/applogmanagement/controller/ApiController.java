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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.github.tommwq.applogmanagement.storage.LogStorage;
import com.github.tommwq.applogmanagement.storage.Memory;

import com.github.tommwq.applogmanagement.LogSession;

@RestController
public class ApiController {

        @Autowired
        private LogManagementServer server;

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
      
                LogStorage storage = new Memory();
                storage.load(deviceId, 0L, 0)
                        .stream()
                        .map(LogRecordCodec::toPojo)
                        .forEach(x -> System.out.println("" + x.getBody()));

                return storage.load(deviceId, 0L, 0)
                        .stream()
                        .map(LogRecordCodec::toPojo)
                        .map(x -> x.toString())
                        .collect(Collectors.toList());
        }
}
