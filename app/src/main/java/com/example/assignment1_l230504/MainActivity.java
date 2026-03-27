package com.example.assignment1_l230504;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CineFAST");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new Home_Fragment())
                .commit();
    }
}