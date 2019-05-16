package com.tungnvan.godear.controllers;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private final String PREFERENCES_FILE_KEY = "com.tungnvan.godear.AUTO_HEAR_CONF";
    private final String AUTO_HEAR_INCOMING_CALL = "AUTO_HEAR_INCOMING_CALL";
    private final String AUTO_HEAR_OUTGOING_CALL = "AUTO_HEAR_OUTGOING_CALL";

    private SharedPreferences shared_preferences;

    public SettingsManager(Context context) {
        shared_preferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    public void setAutoHearIncomingCall(boolean is_auto_hear) {
        SharedPreferences.Editor shared_preference_editor = shared_preferences.edit();
        shared_preference_editor.putBoolean(AUTO_HEAR_INCOMING_CALL, is_auto_hear);
        shared_preference_editor.apply();
    }

    public void setAutoHearOutgoingCall(boolean is_auto_hear) {
        SharedPreferences.Editor shared_preference_editor = shared_preferences.edit();
        shared_preference_editor.putBoolean(AUTO_HEAR_OUTGOING_CALL, is_auto_hear);
        shared_preference_editor.apply();
    }

    public boolean getAutoHearIncomingCall() {
        return shared_preferences.getBoolean(AUTO_HEAR_INCOMING_CALL, false);
    }

    public boolean getAutoHearOutgoingCall() {
        return shared_preferences.getBoolean(AUTO_HEAR_OUTGOING_CALL, false);
    }

}
