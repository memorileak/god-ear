package com.tungnvan.godear;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import utils.FileUtils;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MediaRecorder recorder;
    private int GOD_EAR_PERMISSIONS;
    private int elapsed_time = 0;
    private boolean recording = false;
    private Button record_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record_button = (Button) findViewById(R.id.record_button);

        if (!isGrantedAllPermissions()) {
            grantPermission();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if (requestCode == GOD_EAR_PERMISSIONS) {
//            if (
//                grantResults.length > 0
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                && grantResults[1] == PackageManager.PERMISSION_GRANTED
//                && grantResults[2] == PackageManager.PERMISSION_GRANTED
//            ) {
//                setupRecorder();
//            }
//            return;
//        }
//    }

    private boolean isGrantedAllPermissions() {
        return  ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void grantPermission() {
        if (!isGrantedAllPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                GOD_EAR_PERMISSIONS
            );
        }
    }

    private void setupRecorder() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GodEar/";
            FileUtils.createDirectory(dir_path);
            recorder.setOutputFile(dir_path + FileUtils.generateRandomFileName("GodEar_") + ".amr");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRecorder() {
        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecorder() {
        try {
            recorder.stop();
            recorder.reset();
            recorder.release();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void handleRecordClick(View view) {
        if (recording) {
            stopRecorder();
            recording = false;
            record_button.setText(R.string.record_button_start);
            record_button.setTextColor(getResources().getColor(R.color.white));
            record_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            if (isGrantedAllPermissions()) {
                setupRecorder();
                startRecorder();
                recording = true;
                record_button.setText(R.string.record_button_stop);
                record_button.setTextColor(getResources().getColor(R.color.almost_black));
                record_button.setBackgroundColor(getResources().getColor(R.color.light_gray));
            } else {
                grantPermission();
            }
        }
    };

}
