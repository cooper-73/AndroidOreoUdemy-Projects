package com.example.higherorlower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int number;

    public void generateRandomNumber() {
        number = (int) Math.floor(1 + Math.random() * 20);
    }

    public void guessFunction(View view) {
        EditText guessEditText = findViewById(R.id.guessEditText);

        Log.i("Info", "Button pressed!");

        int guess = Integer.parseInt(guessEditText.getText().toString());

        Log.i("Info", "User's guess is " + guess);

        if(guess == number) {
            Log.i("Info", "User's guess is correct!");
            Toast.makeText(this, "You got it! Try again!", Toast.LENGTH_SHORT).show();
            generateRandomNumber();
        }
        else if(guess < number) {
            Log.i("Info", "User's guess is lower!");
            Toast.makeText(this, "Higher!", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.i("Info", "User's guess is higher!");
            Toast.makeText(this, "Lower!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateRandomNumber();
        Log.i("Info", "The random number is " + number);
    }
}