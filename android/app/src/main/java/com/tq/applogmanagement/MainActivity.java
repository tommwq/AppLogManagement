package com.tq.applogmanagement;

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
import com.tq.applogmanagement.agent.LogAgent;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.lang.ref.WeakReference;
import com.tq.applogmanagement.agent.LogAgent;
import com.tq.applogmanagement.Logger;
import com.tq.applogmanagement.*;

public class MainActivity extends AppCompatActivity {
  private static Logger logger;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    init();

    Button button = (Button) findViewById(R.id.button);
    button.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          // new MyTask(MainActivity.this).execute();
          logger.print(view, button, MainActivity.this);
        }
      });
  }

  private void init() {
    try {
      String host = "172.24.20.112";
      host = "192.168.1.105";
      int port = 50051;

      DeviceAndAppConfig info = new DeviceAndAppConfig()
        .setAppVersion("0.1.0")
        .setModuleVersion("client", "0.1.0")
        .setDeviceId(UUID.randomUUID().toString());

      String fileName = new File(Environment.getExternalStorageDirectory(), "a.blk").getAbsolutePath();
      //Log.e("AndroidRuntime", fileName);
      
      StorageConfig config = new StorageConfig()
        .setFileName(fileName)
        .setBlockSize(4096)
        .setBlockCount(8);
    
      logger = new Log(host, port);
      logger.open(config, info);

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
    } catch (Exception e) {
      //Log.e("TEST", e.getMessage(), e);
    }
  }
}
