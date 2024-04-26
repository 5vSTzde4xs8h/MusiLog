package com.jvn.musilog;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jvn.musilog.util.SettingsActivity;

import org.checkerframework.checker.nullness.qual.NonNull;

/** Login.java allows the user to login using an email and password. */
public class Login extends AppCompatActivity {
  EditText inputEmail, inputPassword;
  Button MainButton, Login_Button;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    FirebaseAuth mAuth;
    mAuth = FirebaseAuth.getInstance();
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_login);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });

    inputEmail = findViewById(R.id.Email_Registeration);
    inputPassword = findViewById(R.id.password_Registation);
    MainButton = findViewById(R.id.MainActivityButton2);
    MainButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
          } // this ends bracket for ONclick
        });

    Login_Button = findViewById(R.id.Login_button);
    Login_Button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            String UserEmail, UserPassword;
            UserEmail = String.valueOf(inputEmail.getText());
            UserPassword = String.valueOf(inputPassword.getText());

            if (TextUtils.isEmpty(UserEmail) || (TextUtils.isEmpty(UserPassword))) {
              Toast.makeText(Login.this, "Email/Password is empty", Toast.LENGTH_SHORT).show();
              return;
            }

            mAuth
                .signInWithEmailAndPassword(UserEmail, UserPassword)
                .addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          // Sign in success, update UI with the signed-in user's information
                          Log.d(TAG, "signInWithEmail:success");
                          FirebaseUser user = mAuth.getCurrentUser();

                          // userID[0] = String.valueOf(user);
                          Toast.makeText(Login.this, "Welcome to MusiLog!", Toast.LENGTH_SHORT)
                              .show();

                          Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                          startActivity(intent);
                          finish();
                        } else {
                          // If sign in fails, display a message to the user.
                          Log.w(TAG, "signInWithEmail:failure!", task.getException());
                          Toast.makeText(
                                  Login.this,
                                  "Unable to login, Please check credentials",
                                  Toast.LENGTH_SHORT)
                              .show();
                        }
                      }
                    });
          } // ONCLICK
        }); // onclick for login button
  } // ON CREATE
} // Class ending bracket
