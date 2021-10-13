package com.hd.jetpackproject.uis;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hd.jetpackproject.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}