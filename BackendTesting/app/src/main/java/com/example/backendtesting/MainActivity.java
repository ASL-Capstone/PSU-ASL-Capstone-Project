package com.example.backendtesting;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.backendtesting.backend.api.*;
import com.example.backendtesting.backend.data.Answer;
import com.example.backendtesting.backend.db.AslDbHelper;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AslDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
