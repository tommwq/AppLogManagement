package com.github.tommwq.applogmanagement;

import com.github.tommwq.applogmanagement.agent.LogReportAgent;
import com.github.tommwq.applogmanagement.logging.Logger;
import com.github.tommwq.applogmanagement.logging.SimpleLogger;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage.Config;
import java.util.Date;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppLogClientApplication implements CommandLineRunner {

        private static final Logger logger = new SimpleLogger();

        public static void main(String[] args) throws Exception {    
                SpringApplication.run(AppLogClientApplication.class, args);
        }

        @Override
        public void run(String... args) throws Exception {

                DeviceAndAppConfig info = new DeviceAndAppConfig()
                        .setAppVersion("0.1.0")
                        .setModuleVersion("client", "0.1.0")
                        .setDeviceId("abc");

                Config config = new Config()
                        .fileName("a.blk")
                        .blockSize(4096)
                        .blockCount(8);
    
                logger.open(new SimpleBlockStorage(config), info);
    
                // report device and app info on log report agent start.
                LogReportAgent agent = new LogReportAgent("localhost", 50051, logger);
                agent.start();
                System.out.println("LogReportAgent started.");

                // record user defined message
                logger.log("hello", info, config);

                // record method and variables
                logger.trace();
                int x = 1;
                logger.print(x);
    
                // record exception
                Throwable error = new Throwable("Test");
                logger.error(error);

                // record user defined message
                // while (true) {
                //         Thread.sleep(3 * 1000);
                //         logger.print(new Date());
                // }

                Thread.sleep(1000);

                logger.close();
                System.out.println("logger closed");
                agent.shutdown();
        }
}
