package com.jvn.musilog;

import static android.content.ContentValues.TAG;

import static java.sql.Types.NULL;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class Login extends AppCompatActivity {

    Button MainButton, Login_Button;
    TextInputEditText inputEmail, inputPassword;
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

    inputEmail = findViewById(R.id.Email);
      MainButton = findViewById(R.id.MainActivityButton2);
      MainButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getApplicationContext(), MainActivity.class);
              startActivity(intent);
              finish();

          } // this ends bracket for ONclick
          });


  Login_Button = findViewById(R.id.Login_button);
  Login_Button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String emailTEST, passwordTEST;
            emailTEST = String.valueOf(inputEmail.getText());
            passwordTEST = String.valueOf(inputPassword.getText());

              System.out.println(passwordTEST);

          }
        });
    /*
      ///////////////
      String email_temp;
      String password_temp;
      final String[] userID = new String[1];

      email_temp = "cmoralesportillo@horizon.csueastbay.edu";
      password_temp = "Outsidelands12";

/////////////
      mAuth.signInWithEmailAndPassword(email_temp, password_temp)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {
                          // Sign in success, update UI with the signed-in user's information
                          Log.d(TAG, "signInWithEmail:success");
                          FirebaseUser user = mAuth.getCurrentUser();

                          userID[0] = String.valueOf(user);
                          Toast.makeText(Login.this, userID[0],Toast.LENGTH_SHORT).show();

                          Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                          startActivity(intent);
                          finish();
                      } else {
                          // If sign in fails, display a message to the user.
                          Log.w(TAG, "signInWithEmail:failure", task.getException());
                          Toast.makeText(Login.this, "Authentication failed.",
                                  Toast.LENGTH_SHORT).show();
                      }
                  }
              });

*/

  }
}
