package com.jvn.musilog.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.jvn.musilog.R; // Add this import statement to resolve 'R' symbol

public class SettingsActivity extends AppCompatActivity {

    private Switch switchSetting;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchSetting = findViewById(R.id.switch_setting);
        sharedPreferences = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        // Load the previous state of the switch from SharedPreferences
        boolean isSwitchOn = sharedPreferences.getBoolean("isSwitchOn", false);
        switchSetting.setChecked(isSwitchOn);

        switchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state of the switch to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isSwitchOn", isChecked);
                editor.apply();
            }
        });
    }
}
