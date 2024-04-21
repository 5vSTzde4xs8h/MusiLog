package com.jvn.musilog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {
Button MainButton, RegisterButton;

TextInputEditText inputEmail, inputPassword;

FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // Object Declarations
    mAuth = FirebaseAuth.getInstance();
    inputEmail = findViewById(R.id.Email);
    inputPassword = findViewById(R.id.password);
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
      This section is a button to go to the main activity page
       */
    MainButton = findViewById(R.id.MainActivityButton);
    MainButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        } // this ends bracket for ONclick

    }); //MAINBUTTON onClicklistner for the "main button" test below this line
      //RegisterButton = findViewById(R.id.register_button);
      //EditText text = (EditText)findViewById(R.id.TextEmailAddress);
     // String value = String.valueOf(inputEmail.getText());
      String value = "hello";
      RegisterButton = findViewById(R.id.register_button);
      RegisterButton.setOnClickListener(v -> Toast.makeText(RegisterPage.this, "Hello", Toast.LENGTH_SHORT).show());
      System.out.println(value);
/*
      RegisterButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //start
              String email ,password;
              email = String.valueOf(inputEmail.getText());
              password = String.valueOf(inputPassword.getText());

              if ( TextUtils.isEmpty(email) ){
                  Toast.makeText(RegisterPage.this,
                          "Email or Password is empty", Toast.LENGTH_SHORT).show();
              }
              if (TextUtils.isEmpty(password) ){
                  Toast.makeText(RegisterPage.this,
                          "Email or Password is empty", Toast.LENGTH_SHORT).show();
              }

              mAuth.createUserWithEmailAndPassword(email, password)
                      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              if (task.isSuccessful()) {
                                  // Sign in success, update UI with the signed-in user's information
                                  Log.d(TAG, "createUserWithEmail:success");
                                  Toast.makeText(RegisterPage.this, "Authentication Successful.",
                                          Toast.LENGTH_SHORT).show();
                                  FirebaseUser user = mAuth.getCurrentUser();

                              } else {
                                  // If sign in fails, display a message to the user.
                                  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                  Toast.makeText(RegisterPage.this, "Authentication failed.",
                                          Toast.LENGTH_SHORT).show();

                              }
                          }
                      });



          }//onCLICK
      });//end of registerButton
*/





  }// end of ON create class
} //end of class
