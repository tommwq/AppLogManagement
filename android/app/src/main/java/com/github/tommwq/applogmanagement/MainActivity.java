package com.github.tommwq.applogmanagement;

import android.content.Intent;
import android.os.Environment;
import java.io.File;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.UUID;
import android.app.Activity;
import android.os.AsyncTask;
import com.github.tommwq.applogmanagement.agent.LogReportAgent;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.lang.ref.WeakReference;
import com.github.tommwq.applogmanagement.agent.LogReportAgent;
import com.github.tommwq.applogmanagement.logging.Logger;
import com.github.tommwq.applogmanagement.*;
import com.github.tommwq.applogmanagement.storage.*;
import com.github.tommwq.applogmanagement.storage.SimpleBlockStorage.Config;

public class MainActivity extends AppCompatActivity {
        private final static String HOST = "HOST";
        private final static String PORT = "PORT";
        
        private static Logger logger;

        private String host = "";
        private int port = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                try {
                        initialize();
                } catch (Exception e) {
                        android.util.Log.e("TQ", "initialize failed", e);
                }

                Button button = (Button) findViewById(R.id.button);
                String text = String.format("%s:%d", host, port);
                button.setText(text);
                button.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        logger.print(view, button, MainActivity.this);
                                }
                        });
        }

        private String getStringExtra(String key, String defaultValue) {
                Intent intent = getIntent();
                if (intent == null) {
                        return defaultValue;
                }

                String value = intent.getStringExtra(key);
                return value == null ? defaultValue : value;
        }

        private void initialize() throws Exception {
                host = getStringExtra(HOST, "192.168.1.105");
                String portString = getStringExtra(PORT, "50051");
                port = Integer.valueOf(portString);
                // String host = "172.24.20.112";
                // host = "192.168.1.105";
                // int port = 50051;

                DeviceAndAppConfig info = new DeviceAndAppConfig()
                        .setAppVersion("0.1.0")
                        .setModuleVersion("client", "0.1.0")
                        .setDeviceId(UUID.randomUUID().toString());

                String fileName = new File(Environment.getExternalStorageDirectory(), "a.blk").getAbsolutePath();
                //Log.e("AndroidRuntime", fileName);
      
                Config config = new Config()
                        .fileName(fileName)
                        .blockSize(4096)
                        .blockCount(8);
    
                logger = new AndroidLogger(host, port, this);
                logger.open(new SimpleBlockStorage(config), info);

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
                logger.log("bye");
        }
}
