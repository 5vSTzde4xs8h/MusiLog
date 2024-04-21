package com.jvn.musilog.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private Switch switchSetting;
    private Switch switchNotification;
    private Switch switchAutoDownload;
    private Switch switchBackgroundSync;
    private Switch switchDarkMode;

    // SharedPreferences for storing settings state
    private SharedPreferences sharedPreferences;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize switches and SharedPreferences
        initializeViews();
        sharedPreferences = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        // Load the previous state of the switches from SharedPreferences
        loadPreviousSettings();

        // Set listeners for switch changes
        setSwitchListeners();
    }

    /**
     * Initialize switches and set their initial state.
     */
    private void initializeViews() {
        switchSetting = findViewById(R.id.switch_setting);
        switchNotification = findViewById(R.id.switch_notification);
        switchAutoDownload = findViewById(R.id.switch_auto_download);
        switchBackgroundSync = findViewById(R.id.switch_background_sync);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
    }

    /**
     * Load the previous state of the switches from SharedPreferences.
     */
    private void loadPreviousSettings() {
        boolean isSwitchOn = sharedPreferences.getBoolean("isSwitchOn", false);
        boolean isNotificationOn = sharedPreferences.getBoolean("isNotificationOn", false);
        boolean isAutoDownloadOn = sharedPreferences.getBoolean("isAutoDownloadOn", false);
        boolean isBackgroundSyncOn = sharedPreferences.getBoolean("isBackgroundSyncOn", false);
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        switchSetting.setChecked(isSwitchOn);
        switchNotification.setChecked(isNotificationOn);
        switchAutoDownload.setChecked(isAutoDownloadOn);
        switchBackgroundSync.setChecked(isBackgroundSyncOn);
        switchDarkMode.setChecked(isDarkModeOn);
    }

    /**
     * Set listeners for switch changes.
     */
    private void setSwitchListeners() {
        // Listener for existing switch
        switchSetting.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isSwitchOn", isChecked));

        // Listeners for new switches
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isNotificationOn", isChecked));
        switchAutoDownload.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isAutoDownloadOn", isChecked));
        switchBackgroundSync.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isBackgroundSyncOn", isChecked));

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save state to SharedPreferences
            saveSwitchState("isDarkModeOn", isChecked);

            // Apply dark mode theme if the switch is checked
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Recreate the activity to apply the theme changes
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
