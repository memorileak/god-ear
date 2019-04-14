package com.tungnvan.godear.components;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionController {

    private final String[] PERMISSIONS = new String[] {
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Activity activity;
    private int GOD_EAR_PERMISSIONS;

    public PermissionController(Activity _activity) {
        activity = _activity;
    }

    public boolean isGrantedAllPermissions() {
        boolean inverse_result = false;
        for (String permission: PERMISSIONS) {
            inverse_result = inverse_result || !(ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
        }
        return !inverse_result;
    }

    public void grantPermission() {
        if (!isGrantedAllPermissions()) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, GOD_EAR_PERMISSIONS);
        }
    }
}
