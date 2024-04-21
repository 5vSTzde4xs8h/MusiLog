package com.jvn.musilog.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.jvn.musilog.R;

/**
 * SettingsActivity class allows users to customize app settings.
 */
public class SettingsActivity extends AppCompatActivity {

    // Switches for various settings options
    private Switch switchBackgroundSync;
    private Switch switchNotification;
    private Switch switchLanguage;
    private Switch switchDarkMode;

    // SharedPreferences for storing settings state
    private SharedPreferences sharedPreferences;

    // Logout button
    private Button btnLogout;

    // Button to redirect to creators information
    private Button btnCreatorsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views and SharedPreferences
        initializeViews();
        sharedPreferences = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        // Load the previous state of the switches from SharedPreferences
        loadPreviousSettings();

        // Set listeners for switch changes
        setSwitchListeners();

        // Set click listener for logout button
        btnLogout.setOnClickListener(v -> {
            // Handle logout logic here
            // (You can add your Firebase logout logic here)
        });

        // Set click listener for creators info button
        btnCreatorsInfo.setOnClickListener(v -> {
            // Redirect the user to the creators information URL
            String creatorsInfoUrl = "https://github.com/5vSTzde4xs8h/MusiLog/wiki"; // Replace with your URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(creatorsInfoUrl));
            startActivity(browserIntent);
        });
    }

    /**
     * Initialize switches, logout button, and creators info button.
     */
    private void initializeViews() {
        switchBackgroundSync = findViewById(R.id.switch_background_sync);
        switchNotification = findViewById(R.id.switch_notification);
        switchLanguage = findViewById(R.id.switch_language);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        btnLogout = findViewById(R.id.btn_logout);
        btnCreatorsInfo = findViewById(R.id.btn_about_creators);
    }

    /**
     * Load the previous state of the switches from SharedPreferences.
     */
    private void loadPreviousSettings() {
        boolean isBackgroundSyncOn = sharedPreferences.getBoolean("isBackgroundSyncOn", false);
        boolean isNotificationOn = sharedPreferences.getBoolean("isNotificationOn", false);
        boolean isLanguageOn = sharedPreferences.getBoolean("isLanguageOn", false);
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        switchBackgroundSync.setChecked(isBackgroundSyncOn);
        switchNotification.setChecked(isNotificationOn);
        switchLanguage.setChecked(isLanguageOn);
        switchDarkMode.setChecked(isDarkModeOn);
    }

    /**
     * Set listeners for switch changes.
     */
    private void setSwitchListeners() {
        // Listeners for switches
        switchBackgroundSync.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isBackgroundSyncOn", isChecked));
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isNotificationOn", isChecked));
        switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isLanguageOn", isChecked));
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSwitchState("isDarkModeOn", isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        });
    }

    /**
     * Save the state of the switch to SharedPreferences.
     *
     * @param key      The key with which the value will be associated in SharedPreferences.
     * @param isChecked The state of the switch.
     */
    private void saveSwitchState(String key, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isChecked);
        editor.apply();
    }
}
