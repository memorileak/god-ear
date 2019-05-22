package com.tungnvan.godear.constants;

import android.os.Environment;

public class GlobalConstants {
    public static final String RECORD_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GodEar/";
    public static final String SOUND_RECORD_PREFIX = "Sound at ";
    public static final String INCOMING_CALL_RECORD_PREFIX = "Incoming call ";
    public static final String OUTGOING_CALL_RECORD_PREFIX = "Outgoing call ";
}
