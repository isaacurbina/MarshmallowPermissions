package com.mobileappsco.training.marshmallowpermissionsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

public class MainActivity extends AppCompatActivity implements OnPermissionCallback {

    // PERMISSIONS
    final String PERMISSION = Manifest.permission.READ_CALENDAR;
    PermissionHelper permissionHelper;
    TextView tvMessage;
    // SERVICES
    Intent service;
    // BOUND SERVICES
    Intent boundServiceIntent;
    MyBoundService boundService;
    boolean serviceBound = false;
    // INTENTSERVICE
    Intent intentService;
    MyIntentServiceBroadcastReceiver broadcastReceiver;
    IntentFilter filter;
    boolean broadcasting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // PERMISSIONS
        permissionHelper = PermissionHelper.getInstance(this);
        tvMessage = (TextView) findViewById(R.id.tv_message);
    }

    // PERMISSIONS
    public void checkPermission(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                PERMISSION);

        String message = (permissionCheck == 0) ? "Permission Check: Granted" : "Permission Check: Not Granted";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        tvMessage.setText(message);
        Log.d("MYTAG", message);
        // -1 No permission
        permissionHelper.setForceAccepting(false).request(PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("MYTAG", "onRequestPermissionsResult "+requestCode);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(String[] permissionName) {
        Log.d("MYTAG", "onPermissionGranted "+permissionName.toString());
    }

    @Override
    public void onPermissionDeclined(String[] permissionName) {
        Log.d("MYTAG", "onPermissionDeclined " + permissionName.toString());
    }

    @Override
    public void onPermissionPreGranted(String permissionName) {
        Log.d("MYTAG", "onPermissionPreGranted " + permissionName);
    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {
        Log.d("MYTAG", "onPermissionNeedExplanation " + permissionName);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Grant me access")
                .setMessage("Do I really need to explain you why?, just click on 'Allow'")
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionHelper.requestAfterExplanation(PERMISSION);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onPermissionReallyDeclined(String permissionName) {
        Log.d("MYTAG", "onPermissionReallyDeclined "+permissionName);
    }

    @Override
    public void onNoPermissionNeeded() {
        Log.d("MYTAG", "onNoPermissionNeeded");
    }

    // SERVICES
    public void startService(View view) {
        service = new Intent(MainActivity.this, MyService.class);
        service.putExtra("MSG", "Service running in UI");
        startService(service);
        Log.d("MYTAG", "MainActivity.startService()");
    }

    public void stopService(View view) {
        stopService(service);
        Log.d("MYTAG", "MainActivity.stopService()");
    }

    // BOUND SERVICES
    @Override
    protected void onStop() {
        Log.d("MYTAG", "MainActivity.onStop()");
        super.onStop();
        if ( serviceBound ) {
            unbindService(mBoundServiceConnection);
            serviceBound = false;
        }
        unregisterReceiver(broadcastReceiver);
    }

    public void bindService(View view) {
        Log.d("MYTAG", "MainActivity.bindService()");
        if ( serviceBound == false ) {
            Log.d("MYTAG", "MainActivity.bindService() binding service");
            boundServiceIntent = new Intent(MainActivity.this, MyBoundService.class);
            //startService(boundServiceIntent);
            bindService(boundServiceIntent, mBoundServiceConnection, Context.BIND_AUTO_CREATE);
        } else {
            Log.d("MYTAG", "MainActivity.bindService() service already bound");
            Toast.makeText(this, "Service already bound", Toast.LENGTH_SHORT).show();
        }
    }

    public void unbindService(View view) {
        Log.d("MYTAG", "MainActivity.unbindService()");
        if ( serviceBound ) {
            Log.d("MYTAG", "MainActivity.unbindService() unbinding");
            unbindService(mBoundServiceConnection);
            serviceBound = false;
        } else {
            Log.d("MYTAG", "MainActivity.unbindService() cannot unbind something not bound");
            Toast.makeText(this, "Cannot stop because the service is not bound", Toast.LENGTH_SHORT).show();
        }
    }

    public void boundMethod(View view) {
        Log.d("MYTAG", "MainActivity.boundMethod()");
        if ( serviceBound ) {
            Log.d("MYTAG", "MainActivity.boundMethod() boundService.getTimestamp()");
            Toast.makeText(this, "Bound Service method >> "+boundService.getTimestamp().toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bound Service method >> Service not bound, bind it first", Toast.LENGTH_SHORT).show();
            Log.d("MYTAG", "MainActivity.boundMethod() boundService not available");
        }
    }

    private ServiceConnection mBoundServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MYTAG", "MainActivity.ServiceConnection.onServiceDisconnected()");
            serviceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("MYTAG", "MainActivity.ServiceConnection.onServiceConnected()");
            MyBoundService.MyBinder myBinder = (MyBoundService.MyBinder) service;
            boundService = myBinder.getService();
            serviceBound = true;
        }
    };

    // INTENTSERVICE
    public void startIntentService(View view) {
        Log.d("MYTAG", "MainActivity.startIntentService()");
        filter = new IntentFilter(MyIntentServiceBroadcastReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastReceiver = new MyIntentServiceBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
        broadcasting = true;
    }

    public void sendIntentService(View view) {
        Log.d("MYTAG", "MainActivity.sendIntentService()");
        intentService = new Intent(MainActivity.this, MyIntentService.class);
        intentService.putExtra("MSG", "MyIntentService");
        startService(intentService);
    }

    public void stopIntentService(View view) {
        Log.d("MYTAG", "MainActivity.stopIntentService()");
        if ( broadcasting ) {
            Log.d("MYTAG", "MainActivity.stopIntentService() stopping broadcast");
            unregisterReceiver(broadcastReceiver);
            broadcasting = false;
        } else {
            Log.d("MYTAG", "MainActivity.stopIntentService() cannot unregister because broadcast it's not started");
            Toast.makeText(this, "Unregistering Broadcast >> You were not registered", Toast.LENGTH_SHORT).show();
        }
    }
}
