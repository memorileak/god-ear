package com.tungnvan.godear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tungnvan.godear.components.PermissionController;
import com.tungnvan.godear.utils.TimeUtils;

public class MainActivity extends AppCompatActivity {

    public static final String PROBE_SERVICE = "PROBE_SERVICE";

    private boolean is_recording = false;
    private PermissionController permission_controller;
    private Button record_button;
    private TextView record_timer;
    private Toolbar main_toolbar;

    private void probeRunningService() {
        Intent probe_intent = new Intent(MainActivity.PROBE_SERVICE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(probe_intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        record_button = (Button) findViewById(R.id.record_button);
        record_timer = (TextView) findViewById(R.id.record_timer);
        record_timer.setText(TimeUtils.toHMSString(0));
        setSupportActionBar(main_toolbar);
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
                record_timer.setText(TimeUtils.toHMSString(intent.getIntExtra(RecordService.ELAPSED_TIME, 0)));
            }
        }, new IntentFilter(RecordService.BROADCAST_CLOCK));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_records:
                startListRecordsActivity();
                return true;
            case R.id.settings:
                startSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeRecordButtonUI(boolean is_recording) {
        if (is_recording) {
            record_button.setBackground(getResources().getDrawable(R.drawable.stop_button_shape));
        } else {
            record_button.setBackground(getResources().getDrawable(R.drawable.record_button_shape));
        }
    }

    private void startListRecordsActivity() {
        Toast.makeText(this, "This feature is in development!", Toast.LENGTH_SHORT).show();
    }

    private void startSettingsActivity() {
        Toast.makeText(this, "This feature is in development!", Toast.LENGTH_SHORT).show();
    }

    public void handleRecordClick(View view) {
        Intent record_service_intent = new Intent(this, RecordService.class);
        if (is_recording) {
            stopService(record_service_intent);
            Toast.makeText(this, "Record file has successfully saved!", Toast.LENGTH_SHORT).show();
        } else if (permission_controller.isGrantedAllPermissions()) {
            startService(record_service_intent);
        } else {
            permission_controller.grantPermission();
        }
    };

}
