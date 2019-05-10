package com.tungnvan.godear.components;

import java.util.prefs.Preferences;

public class SettingsManager {

    private static SettingsManager instance = null;

    private final Preferences preferences = Preferences.userNodeForPackage(com.tungnvan.godear.MainActivity.class);
    private final String AUTO_HEAR_INCOMING_CALL = "AUTO_HEAR_INCOMING_CALL";
    private final String AUTO_HEAR_OUTGOING_CALL = "AUTO_HEAR_OUTGOING_CALL";

    public static SettingsManager getInstance() {
        if (instance == null) instance = new SettingsManager();
        return instance;
    }

    private SettingsManager() {}

    public void setAutoHearIncomingCall(boolean is_auto_hear) {
        preferences.putBoolean(AUTO_HEAR_INCOMING_CALL, is_auto_hear);
    }

    public void setAutoHearOutgoingCall(boolean is_auto_hear) {
        preferences.putBoolean(AUTO_HEAR_OUTGOING_CALL, is_auto_hear);
    }

    public boolean setAutoHearIncomingCall() {
        return preferences.getBoolean(AUTO_HEAR_INCOMING_CALL, false);
    }

    public boolean setAutoHearOutgoingCall() {
        return preferences.getBoolean(AUTO_HEAR_OUTGOING_CALL, false);
    }

}
