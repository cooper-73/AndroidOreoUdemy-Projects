package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final double DOLLARS_TO_SOLES = 3.65;

    public void convertFunction(View view) {
        EditText amountEditText = findViewById(R.id.amountEditText);

        Log.i("Info", "Convert button pressed");

        double dollarsAmount = Double.parseDouble(amountEditText.getText().toString());
        double solesAmount = dollarsAmount * DOLLARS_TO_SOLES;
        @SuppressLint("DefaultLocale") String message = String.format("$%.2f is S/%.2f", dollarsAmount, solesAmount);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}