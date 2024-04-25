package com.jvn.musilog;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;


public class Landing_page extends AppCompatActivity {
Button LogOutButton, DeleteUser, PasswordReset;
FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_landing_page);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });

    LogOutButton = findViewById(R.id.Log_Out);
    LogOutButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();

            if((FirebaseAuth.getInstance().getCurrentUser()) == null) {
                Toast.makeText(Landing_page.this,"Signing out",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(Landing_page.this,"Error",Toast.LENGTH_LONG).show();
            }
        }
    });

    DeleteUser = findViewById(R.id.deleteButton);

    DeleteUser.setOnClickListener((new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                                Toast.makeText(Landing_page.this,"Account Deleted",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Landing_page.this,"Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }));

    PasswordReset = findViewById(R.id.passwordResetbutton);
    PasswordReset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            // This needs to call to a function that grabs user email
            String emailAddress = user.getEmail();
            Log.d(TAG,emailAddress);
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Landing_page.this,"Email Sent",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    });

  } // ONCREATE ENDING
}
