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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    EditText usernameEditText;
    EditText nameEditText;
    EditText passwordEditText;
    EditText repasswordEditText;
    Button signupButton;
    TextView loginTextView;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        nameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        repasswordEditText = findViewById(R.id.repassword);
        signupButton = findViewById(R.id.signup);
        loginTextView = findViewById(R.id.loginlink);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repassword = repasswordEditText.getText().toString();

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
                else if (!repassword.equals(password)) {
                    repasswordEditText.setError("Passwords not match!");
                    repasswordEditText.requestFocus();
                }
                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Sign up unsuccessful, please try again!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user != null){

                                    String uid = user.getUid();
                                    mDatabase.child("users").child(uid).child("email").setValue(username);
                                    mDatabase.child("users").child(uid).child("username").setValue(name);
                                    mDatabase.child("users").child(uid).child("petname").setValue("Default");
                                    mDatabase.child("users").child(uid).child("age").setValue("Default");
                                    mDatabase.child("users").child(uid).child("gender").setValue("Default");
                                    mDatabase.child("users").child(uid).child("weight").setValue("Default");

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }

                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                i.putExtra("USER_NAME", name);
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