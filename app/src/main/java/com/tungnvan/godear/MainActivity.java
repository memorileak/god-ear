package com.tungnvan.godear;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tungnvan.godear.components.PermissionController;
import com.tungnvan.godear.components.RecordTimer;
import com.tungnvan.godear.components.Recorder;

public class MainActivity extends AppCompatActivity {

    private PermissionController permission_controller;
    private Recorder recorder;
    private RecordTimer clock;

    private Button record_button;
    private TextView record_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record_button = (Button) findViewById(R.id.record_button);
        record_timer = (TextView) findViewById(R.id.record_timer);

        permission_controller = new PermissionController(this);
        recorder = new Recorder();
        clock = new RecordTimer(record_timer);

        clock.resetTimer();
        if (!permission_controller.isGrantedAllPermissions()) {
            permission_controller.grantPermission();
        }
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
        if (recorder.isRecording()) {
            recorder.stopRecorder();
            clock.stopTimer();
            clock.resetTimer();
        } else {
            if (permission_controller.isGrantedAllPermissions()) {
                recorder.setupRecorder();
                recorder.startRecorder();
                clock.startTimer();
            } else {
                permission_controller.grantPermission();
            }
        }
        changeRecordButtonUI(recorder.isRecording());
    };

}
