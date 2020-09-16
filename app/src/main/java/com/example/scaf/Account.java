package com.example.scaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.scaf.ui.account.AccountFragment;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AccountFragment.newInstance())
                    .commitNow();
        }
    }
}