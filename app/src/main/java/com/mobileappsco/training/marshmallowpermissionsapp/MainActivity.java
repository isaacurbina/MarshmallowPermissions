package com.mobileappsco.training.marshmallowpermissionsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    final String PERMISSION = Manifest.permission.READ_CALENDAR;
    PermissionHelper permissionHelper;
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionHelper = PermissionHelper.getInstance(this);
        tvMessage = (TextView) findViewById(R.id.tv_message);
    }

    public void checkPermission(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                PERMISSION);

        String message = "Permission Check: "+permissionCheck;

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
        Log.d("MYTAG", "onPermissionDeclined "+permissionName.toString());
    }

    @Override
    public void onPermissionPreGranted(String permissionName) {
        Log.d("MYTAG", "onPermissionPreGranted "+permissionName);
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
}
