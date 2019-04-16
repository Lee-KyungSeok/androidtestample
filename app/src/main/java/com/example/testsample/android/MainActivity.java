package com.example.testsample.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testsample.R;

public class MainActivity extends AppCompatActivity {

    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCount++;
    }

    public int getCount() {
        return mCount;
    }
}
