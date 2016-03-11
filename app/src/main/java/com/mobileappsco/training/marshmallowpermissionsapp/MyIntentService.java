package com.mobileappsco.training.marshmallowpermissionsapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class MyIntentService extends IntentService {

    Intent broadcast;

    public MyIntentService() {
        super("MyIntentService");
    }

    public MyIntentService(String name) {
        super(name);
        Log.d("MYTAG", "MyIntentService.constructor()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MYTAG", "MyIntentService.onHandleIntent()");
        String msg = intent.getStringExtra("MSG");
        SystemClock.sleep(3000); // 3 seconds
        String resultTxt = msg + " "
                + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis());
        broadcast = new Intent();
        broadcast.setAction(MyIntentServiceBroadcastReceiver.ACTION_RESP);
        broadcast.addCategory(Intent.CATEGORY_DEFAULT);
        broadcast.putExtra("MSG", resultTxt);
        sendBroadcast(broadcast);
    }
}
