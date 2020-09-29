package com.example.scaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText nameEditText = findViewById(R.id.name);
        final EditText petnameEditText = findViewById(R.id.petname);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button signupButton = findViewById(R.id.signup);

    }
}