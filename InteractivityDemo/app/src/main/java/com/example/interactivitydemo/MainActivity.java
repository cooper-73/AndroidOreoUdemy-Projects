package com.example.interactivitydemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public void clickFunction(View view) {

        EditText nameEditText = findViewById(R.id.nameEditText);

        Log.i("Info", "Hey! You pressed the button");

        Log.i("Values", nameEditText.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}