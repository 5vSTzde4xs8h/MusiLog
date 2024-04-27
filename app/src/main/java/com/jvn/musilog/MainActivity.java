package com.jvn.musilog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


/**
 * The type Main activity.
 *
 */
public class MainActivity extends AppCompatActivity {

  /** The Main activity button object declarations. */
  Button MainActivity_RegisterButton, MainActivity_LoginButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.Mainpage),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
/*
 * MainActivity Register Button slows the user the registration Page
 *
 */
    MainActivity_RegisterButton = findViewById(R.id.GOTOregister_button);
    MainActivity_RegisterButton.setOnClickListener(
        v -> {
          Intent intent = new Intent(getApplicationContext(), RegisterPage.class);
          startActivity(intent);
          finish();
        });
/*
 * MainActivity Login Button slows the user the Login Page
 *
 */
    MainActivity_LoginButton = findViewById(R.id.GOTOLogin_button);
    MainActivity_LoginButton.setOnClickListener(
        v -> {
          Intent intent = new Intent(getApplicationContext(), Login.class);
          startActivity(intent);
          finish();
        });
  }
}
