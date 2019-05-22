package com.tungnvan.godear.components;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tungnvan.godear.R;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar settings_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings_toolbar = (Toolbar) findViewById(R.id.settings_toolbar);

        setTitle(R.string.settings_activity_title);
        setSupportActionBar(settings_toolbar);
    }
}
