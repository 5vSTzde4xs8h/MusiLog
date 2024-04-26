package com.jvn.musilog;

import android.os.Bundle;
import android.text.TextUtils;
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

public class TestActivity extends AppCompatActivity {
  Button LogOut, deleteAccount;
  EditText test, passwordTest;
  TextInputEditText INPUT;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_test);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
    // String emailTEST;
    test = findViewById(R.id.TestUserName);
    INPUT = findViewById(R.id.TEXTINPUT);
    passwordTest = findViewById(R.id.editTextPassword);
    // emailTEST   = test.getText().toString();
    LogOut = findViewById(R.id.Log_outButtonTest);
    LogOut.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String emailTEST, input_text, passwordTEST;
            emailTEST = test.getText().toString();
            input_text = INPUT.getText().toString();
            passwordTEST = passwordTest.getText().toString();
            System.out.println(emailTEST);
            System.out.println(passwordTEST);
            System.out.println(input_text);
            if (TextUtils.isEmpty(input_text)) {
              Toast.makeText(TestActivity.this, "This is empty!", Toast.LENGTH_LONG).show();
            }
            // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            // startActivity(intent);
            // finish();
            // FirebaseAuth.getInstance().getCurrentUser();
            // FirebaseAuth.getInstance().signOut();
          }
        });
  }
}

              /*
              String emailTEST, passwordTEST;
              emailTEST = String.valueOf(inputEmail.getText());
              passwordTEST = String.valueOf(inputPassword.getText());
              System.out.println(emailTEST);
              System.out.println(passwordTEST);
                if(TextUtils.isEmpty(emailTEST)){
                    Toast.makeText(Login.this,"Email is empty",Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(passwordTEST)){
                    Toast.makeText(Login.this,"Password is empty",Toast.LENGTH_LONG).show();
                }
                 */

//                                  //userID[0] = String.valueOf(user);
//                                  final String[] userID = new String[1];
