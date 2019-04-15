package com.tungnvan.godear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tungnvan.godear.components.PermissionController;

public class MainActivity extends AppCompatActivity {

    public static final String PROBE_SERVICE = "PROBE_SERVICE";

    private boolean is_recording = false;

    private PermissionController permission_controller;

    private Button record_button;
    private TextView record_timer;

    private void probeRunningService() {
        Intent probe_intent = new Intent(MainActivity.PROBE_SERVICE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(probe_intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record_button = (Button) findViewById(R.id.record_button);
        record_timer = (TextView) findViewById(R.id.record_timer);

        record_timer.setText(String.format("%03d", 0));

        permission_controller = new PermissionController(this);

        if (!permission_controller.isGrantedAllPermissions()) {
            permission_controller.grantPermission();
        }

        probeRunningService();

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                is_recording = intent.getBooleanExtra(RecordService.IS_RECORDING, false);
                changeRecordButtonUI(is_recording);
            }
        }, new IntentFilter(RecordService.BROADCAST_RECORDER));

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                record_timer.setText(String.format("%03d", intent.getIntExtra(RecordService.ELAPSED_TIME, 0)));
            }
        }, new IntentFilter(RecordService.BROADCAST_CLOCK));
    }

    private void changeRecordButtonUI(boolean is_recording) {
        if (is_recording) {
            record_button.setText(R.string.record_button_stop);
            record_button.setTextColor(getResources().getColor(R.color.almost_black));
            record_button.setBackgroundColor(getResources().getColor(R.color.light_gray));
        } else {
            record_button.setText(R.string.record_button_start);
            record_button.setTextColor(getResources().getColor(R.color.white));
            record_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void handleRecordClick(View view) {
        Intent record_service_intent = new Intent(this, RecordService.class);
        if (is_recording) {
            stopService(record_service_intent);
        } else {
            if (permission_controller.isGrantedAllPermissions()) {
                startService(record_service_intent);
            } else {
                permission_controller.grantPermission();
            }
        }
    };

}
