package com.tungnvan.godear.components;

import android.media.MediaRecorder;
import android.os.Environment;

import com.tungnvan.godear.utils.FileUtils;

public class Recorder {

    private MediaRecorder recorder;
    private boolean recording = false;

    public void setupRecorder() {
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

    public void startRecorder() {
        try {
            recorder.prepare();
            recorder.start();
            recording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecorder() {
        try {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recording = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRecording() {
        return recording;
    }

}
