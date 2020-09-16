package com.example.scaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.scaf.ui.functions.FunctionsFragment;

public class Functions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.functions_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FunctionsFragment.newInstance())
                    .commitNow();
        }
    }
}