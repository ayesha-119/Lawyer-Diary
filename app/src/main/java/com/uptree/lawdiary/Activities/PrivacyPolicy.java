package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.uptree.lawdiary.R;

public class PrivacyPolicy extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}