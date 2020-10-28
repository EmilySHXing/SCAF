package com.example.scaf.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scaf.MainActivity;
import com.example.scaf.PswrstActivity;
import com.example.scaf.R;
import com.example.scaf.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView registerText;
    TextView pswrstText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerText = findViewById(R.id.signuplink);
        pswrstText = findViewById(R.id.pswrstlink);
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please log in.", Toast.LENGTH_SHORT).show();
                }
            }
        };


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    usernameEditText.setError("Please enter an email address!");
                    usernameEditText.requestFocus();
                }
                else if (password.isEmpty()) {
                    passwordEditText.setError("Please enter a password!");
                    passwordEditText.requestFocus();
                }
                else if (password.length()<8) {
                    passwordEditText.setError("Password is at least 8 characters!");
                    passwordEditText.requestFocus();
                }
                else {
                    Log.d("email", email);
                    Log.d("password", password);
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Sign In unsuccessful, please try again!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);

                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                String name = user.getDisplayName();
                                for (UserInfo userInfo : user.getProviderData()) {
                                    if (name == null && userInfo.getDisplayName() != null) {
                                        name = userInfo.getDisplayName();
                                    }
                                }

                                if (name == null){
                                    name = "User";
                                }

                                Toast.makeText(LoginActivity.this, "Welcome back, "+name+"!", Toast.LENGTH_SHORT).show();
                                i.putExtra("USER_NAME", name);
                                startActivity(i);
                            }
                        }
                    });

                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        pswrstText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PswrstActivity.class);
                startActivity(intent);
            }
        });

    }


}