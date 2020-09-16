package com.example.scaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.scaf.ui.notices.NoticesFragment;

public class Notices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notices_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, NoticesFragment.newInstance())
                    .commitNow();
        }
    }
}