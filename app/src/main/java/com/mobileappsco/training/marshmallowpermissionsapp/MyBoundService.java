package com.mobileappsco.training.marshmallowpermissionsapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Chronometer;

public class MyBoundService extends Service {

    private IBinder mBinder = new MyBinder();
    private Chronometer mChronometer;

    @Override
    public void onCreate() {
        Log.d("MYTAG", "MyBoundService.onCreate()");
        super.onCreate();
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @Override
    public void onDestroy() {
        Log.d("MYTAG", "MyBoundService.onDestroy()");
        super.onDestroy();
        mChronometer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MYTAG", "MyBoundService.onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MYTAG", "MyBoundService.onUnbind()");
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MYTAG", "MyBoundService.onBind()");
        return mBinder;
    }

    public class MyBinder extends Binder {
        MyBoundService getService() {
            Log.d("MYTAG", "MyBoundService.MyBinder.getService()");
            return MyBoundService.this;
        }
    }

    public String getTimestamp() {
        Log.d("MYTAG", "MyBoundService.getTimestamp()");
        long elapsedMillis = SystemClock.elapsedRealtime()
                - mChronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        return hours + ":" + minutes + ":" + seconds + ":" + millis;
    }
}
