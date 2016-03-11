package com.mobileappsco.training.marshmallowpermissionsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyIntentServiceBroadcastReceiver extends BroadcastReceiver{

    public static final String ACTION_RESP =
            "com.mobileappsco.training.marshmallowpermissionsapp.intent.action.MESSAGE_PROCESSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MYTAG", "MyIntentBroadcastReceiver.onReceive()");
        Toast.makeText(context, "IntentService Broadcasting", Toast.LENGTH_SHORT).show();
    }
}
