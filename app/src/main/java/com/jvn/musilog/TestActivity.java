package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.IdTokenListener;

public class TestActivity extends AppCompatActivity {
    Button LogOut;
    FirebaseAuth auth;
    EditText test;
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
   //String emailTEST;
   test = findViewById(R.id.editText);
   INPUT = findViewById(R.id.TEXTINPUT);

  // emailTEST   = test.getText().toString();
   LogOut = findViewById(R.id.Log_outButton);
    LogOut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String emailTEST, input_text;
            emailTEST   = test.getText().toString();
            input_text = INPUT.getText().toString();
            System.out.println(emailTEST);

            System.out.println(input_text);
            // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);
            //finish();
            //FirebaseAuth.getInstance().getCurrentUser();
            //FirebaseAuth.getInstance().signOut();
        }
    });



  }
}
