package com.example.numbershapes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

class Number {
    int value;

    public boolean isTriangular() {
        int tmp = (int) Math.floor(Math.sqrt(value * 2));

        return (tmp * (tmp + 1) == value * 2);
    }

    public boolean isSquare() {
        int tmp = (int) Math.floor(Math.sqrt(value));

        return (tmp * tmp == value);
    }
}

public class MainActivity extends AppCompatActivity {

    public void testFunction(View view) {
        EditText numberEditText = findViewById(R.id.numberEditText);

        Log.i("Info", "Button pressed!");

        Number num = new Number();
        num.value = Integer.parseInt(numberEditText.getText().toString());

        String message;

        if(num.isSquare() && num.isTriangular())    message = "It is both a triangular and square number!";
        else if(num.isTriangular()) message = "It is only a triangular number!";
        else if(num.isSquare()) message = "It is only a square number!";
        else    message = "It is neither a triangular number nor a square number!";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}