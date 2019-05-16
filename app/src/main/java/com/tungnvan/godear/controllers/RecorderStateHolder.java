package com.tungnvan.godear.controllers;

public class RecorderStateHolder {

    private static RecorderStateHolder instance = null;

    private boolean is_recording = false;
    private String file_path = null;

    public static RecorderStateHolder getInstance() {
        if (instance == null) instance = new RecorderStateHolder();
        return instance;
    }

    private RecorderStateHolder() {}

    public void setRecordingState(boolean is_recording_state) {
        is_recording = is_recording_state;
    }

    public void setRecordFilePath(String record_file_path) {
        file_path = record_file_path;
    }

    public boolean getRecordingState() {
        return is_recording;
    }

    public String getRecordFilePath() {
        return file_path;
    }

}
