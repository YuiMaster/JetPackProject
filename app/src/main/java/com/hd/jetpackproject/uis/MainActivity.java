package com.hd.jetpackproject.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hd.jetpackproject.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}