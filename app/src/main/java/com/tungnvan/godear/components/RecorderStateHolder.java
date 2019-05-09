package com.tungnvan.godear.components;

public class RecorderStateHolder {

    private static boolean is_recording = false;
    private static String file_path = null;

    public static void setRecordingState(boolean is_recording_state) {
        is_recording = is_recording_state;
    }

    public static void setRecordFilePath(String record_file_path) {
        file_path = record_file_path;
    }

    public static boolean getRecordingState() {
        return is_recording;
    }

    public static String getRecordFilePath() {
        return file_path;
    }

}
