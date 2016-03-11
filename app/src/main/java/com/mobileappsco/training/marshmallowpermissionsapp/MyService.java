package com.mobileappsco.training.marshmallowpermissionsapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    private String msg;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        Log.d("MYTAG", "MyService.onCreate()");
        super.onCreate();
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("MYTAG", "MyService.runnable.run()");
                Toast.makeText(getApplicationContext(), "MyService >> "+msg, Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        Log.d("MYTAG", "MyService.onDestroy()");
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MYTAG", "MyService.onStartCommand()");
        Bundle bundle = intent.getExtras();
        if ( bundle != null )
            msg = bundle.getString("MSG");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MYTAG", "MyService.onBind()");
        return null;
    }
}
