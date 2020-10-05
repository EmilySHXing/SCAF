package com.example.scaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scaf.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    EditText usernameEditText;
    EditText nameEditText;
    EditText petnameEditText;
    EditText passwordEditText;
    Button signupButton;
    TextView loginTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        nameEditText = findViewById(R.id.name);
        petnameEditText = findViewById(R.id.petname);
        passwordEditText = findViewById(R.id.password);
        signupButton = findViewById(R.id.signup);
        loginTextView = findViewById(R.id.loginlink);
        mFirebaseAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String petname = petnameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty()) {
                    usernameEditText.setError("Please enter an email address!");
                    usernameEditText.requestFocus();
                }
                else if (password.isEmpty()) {
                    passwordEditText.setError("Please enter a password!");
                    passwordEditText.requestFocus();
                }
                else if (password.length()<8) {
                    passwordEditText.setError("Password needs to at least 8 characters!");
                    passwordEditText.requestFocus();
                }
                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Sign up unsuccessful, please try again!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                i.putExtra("USER_EMAIL", username);
                                i.putExtra("USER_NAME", "User");
                                startActivity(i);
                            }
                        }
                    });
                }

            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}