package com.example.locationpermissionexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView permissionTextView;
    private static final int PERMISSION_REQUEST_CODE = 1;
    int rejectCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionTextView = findViewById((R.id.permissionTextViewId));

        if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)){
            requestRuntimePermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
        }
    }

    private void requestRuntimePermission(Activity activity, String runtimePermission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{runtimePermission}, requestCode);
    }

    private boolean hasRuntimePermission(Context context, String runtimePermission) {
        boolean ret = false;
        int currentAndroidVersion = Build.VERSION.SDK_INT;

        if(currentAndroidVersion > Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(context, runtimePermission) == PackageManager.PERMISSION_GRANTED) {
                ret = true;
            }
        }else {
            ret = true;
        }
        return ret;
    }

    public void onStart() {
        super.onStart();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            rejectCounter++;
            if (rejectCounter == 3) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            } else if (rejectCounter == 2) {
                if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestRuntimePermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
                }
            } else if (rejectCounter == 4){
                rejectCounter = 0;
                if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
                    finishAffinity();
                } else if(Build.VERSION.SDK_INT>=21){
                    finishAndRemoveTask();
                }
            }
        }

        permissionTextView.setText("The location permission is already allowed!");

        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}