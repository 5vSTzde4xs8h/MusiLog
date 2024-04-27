package com.jvn.musilog.util;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jvn.musilog.MainActivity;
import com.jvn.musilog.R;
import com.jvn.musilog.UserActivity;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * SettingsActivity class allows users to customize app settings.
 */
public class SettingsActivity extends AppCompatActivity {

    // Switches for various settings options
    private Switch switchNotification;
    private Switch switchBackgroundSync;
    private Switch switchLanguage;
    private Switch switchDarkMode;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // SharedPreferences for storing settings state
    private SharedPreferences sharedPreferences;

    // Logout button , Delete Button, Reset link Button , Return Button
    private Button LogoutButton, DeleteButton , ResetLinkButton;

    Button returnButtonSettings;

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

        // Set click listener for creators info button
        btnCreatorsInfo.setOnClickListener(v -> {
            // Redirect the user to the creators information URL
            String creatorsInfoUrl = "https://github.com/5vSTzde4xs8h/MusiLog/wiki"; // Replace with your URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(creatorsInfoUrl));
            startActivity(browserIntent);
        });
        /**
         * The logout Button log outs the user.
         * @param FirebaseAuth gets an instance and calls signout function.
         * This button returns the user to the main activity
         */

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                if ((FirebaseAuth.getInstance().getCurrentUser()) == null) {
                    Toast.makeText(SettingsActivity.this, "Signing out", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        /**
         * When the delete button is pressed, then the user account is deleted from Firebase.
         */
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete()
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                            Toast.makeText(SettingsActivity.this, "Account Deleted", Toast.LENGTH_LONG)
                                                    .show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
            }
        }); // end of Onclick for Delete button

        /**
         * This button resets the password via email
         * @param Firebase Auth grabs an instance of the firebase and
         * @string emailAddress gets the curent user's email
         */

        ResetLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                // This needs to call to a function that grabs user email
                String emailAddress = user.getEmail();
                Log.d(TAG, emailAddress);
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Email Sent", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });   
            }
        }); // end of ONclickListener for Reset link button
        returnButtonSettings = findViewById(R.id.button4);
        returnButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Returning", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }


    /**
     * Initialize switches, logout button, and creators info button.
     */
    private void initializeViews() {
        // These are the switches on the settings page
        switchNotification = findViewById(R.id.switch_notification);
        switchBackgroundSync = findViewById(R.id.switch_background_sync);
        switchLanguage = findViewById(R.id.switch_language);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        // These are all the buttons on the settings page
        LogoutButton = findViewById(R.id.Logout_Button);
        DeleteButton = findViewById(R.id.DeleteButtonSettings);
        ResetLinkButton = findViewById(R.id.Reset_Password);
        btnCreatorsInfo = findViewById(R.id.creator_button);

    }

    /**
     * Load the previous state of the switches from SharedPreferences.
     */
    private void loadPreviousSettings() {
        boolean isNotificationOn = sharedPreferences.getBoolean("isNotificationOn", false);
        boolean isBackgroundSyncOn = sharedPreferences.getBoolean("isBackgroundSyncOn", false);
        boolean isLanguageOn = sharedPreferences.getBoolean("isLanguageOn", false);
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        switchNotification.setChecked(isNotificationOn);
        switchBackgroundSync.setChecked(isBackgroundSyncOn);
        switchLanguage.setChecked(isLanguageOn);
        switchDarkMode.setChecked(isDarkModeOn);
    }

    /**
     * Set listeners for switch changes.
     */
    private void setSwitchListeners() {
        // Listeners for switches
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isNotificationOn", isChecked));
        switchBackgroundSync.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isBackgroundSyncOn", isChecked));
        switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("isLanguageOn", isChecked));
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
