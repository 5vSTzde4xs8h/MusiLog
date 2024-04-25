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

import org.checkerframework.checker.nullness.qual.NonNull;

/** RegisterPage.java creates a new account using email and password. */
public class RegisterPage extends AppCompatActivity {
  /*
   * The following lines creates a button and EditText objects!
   * */
  Button MainButton, RegisterButton;
  EditText inputEmail, inputPassword;

  // This creates the firebase authentication object.
  FirebaseAuth mAuth;

  /*
   * The following OnCreate allows the user to create an account using Email and Password
   * */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_register_page);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.RegisterPage),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });

    /*
    This section assigns the Button and
    returns the user to the main activity which is the first page of the application.
     */
    MainButton = findViewById(R.id.MainActivityButton);
    MainButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
          } // this ends bracket for ONclick
        });

    // Object Declarations
    // EditText and Button
    mAuth = FirebaseAuth.getInstance();
    inputEmail = findViewById(R.id.Email_Registation);
    inputPassword = findViewById(R.id.password_Registation);

    RegisterButton = findViewById(R.id.register_button);
    // This creates an Onclick lister to grab the strings placed inside the EditText,
    // check bounds and use the firebase function to create a new account with an email and
    // password.
    RegisterButton.setOnClickListener(
        v -> {
          String UserEmail, UserPassword;
          UserEmail = String.valueOf(inputEmail.getText());
          UserPassword = String.valueOf(inputPassword.getText());

          if (TextUtils.isEmpty(UserEmail) || (TextUtils.isEmpty(UserPassword))) {
            Toast.makeText(RegisterPage.this, "Email/Password is empty", Toast.LENGTH_SHORT).show();
            return;
          }

          mAuth
              .createUserWithEmailAndPassword(UserEmail, UserPassword)
              .addOnCompleteListener(
                  new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(
                                RegisterPage.this, "Authentication Successful.", Toast.LENGTH_SHORT)
                            .show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getApplicationContext(), Landing_page.class);
                        startActivity(intent);
                        finish();

                      } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(
                                RegisterPage.this, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show();
                      }
                    }
                  });
        });
  } // end of ON create class
} // end of class
