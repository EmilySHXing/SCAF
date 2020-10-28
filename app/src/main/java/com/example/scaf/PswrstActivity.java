package com.example.scaf;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scaf.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PswrstActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText resetemailEditText;
    Button resetButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pswreset);

        resetemailEditText = findViewById(R.id.reset_email);
        resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = resetemailEditText.getText().toString();

                if (email.isEmpty()) {
                    resetemailEditText.setError("Please enter an email address!");
                    resetemailEditText.requestFocus();
                }
                else {
                    Log.d("email", email);

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("password_reset", "Email sent.");
                                        Intent i = new Intent(PswrstActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(PswrstActivity.this, "Password reset unsuccessful, please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

}