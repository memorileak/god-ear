package com.tungnvan.godear.components;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.tungnvan.godear.R;
import com.tungnvan.godear.controllers.SettingsManager;

public class AutoHearSettingsFragment extends PreferenceFragmentCompat {

    private SettingsManager settings_manager;
    private SwitchPreference auto_hear_incoming_call_preference;
    private SwitchPreference auto_hear_outgoing_call_preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings_manager = new SettingsManager(this.getActivity());

        auto_hear_incoming_call_preference = (SwitchPreference) findPreference(getResources().getString(R.string.auto_hear_incoming_call_key));
        auto_hear_outgoing_call_preference = (SwitchPreference) findPreference(getResources().getString(R.string.auto_hear_outgoing_call_key));

        auto_hear_incoming_call_preference.setChecked(settings_manager.getAutoHearIncomingCall());
        auto_hear_outgoing_call_preference.setChecked(settings_manager.getAutoHearOutgoingCall());

        auto_hear_incoming_call_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (auto_hear_incoming_call_preference.isChecked())  {
                    settings_manager.setAutoHearIncomingCall(false);
                    auto_hear_incoming_call_preference.setChecked(false);
                } else {
                    settings_manager.setAutoHearIncomingCall(true);
                    auto_hear_incoming_call_preference.setChecked(true);
                }
                return false;
            }
        });

        auto_hear_outgoing_call_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (auto_hear_outgoing_call_preference.isChecked())  {
                    settings_manager.setAutoHearOutgoingCall(false);
                    auto_hear_outgoing_call_preference.setChecked(false);
                } else {
                    settings_manager.setAutoHearOutgoingCall(true);
                    auto_hear_outgoing_call_preference.setChecked(true);
                }
                return false;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.settings_preference, s);
    }

}
