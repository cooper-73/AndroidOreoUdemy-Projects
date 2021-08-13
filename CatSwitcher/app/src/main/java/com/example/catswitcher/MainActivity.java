package com.example.catswitcher;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    boolean cat = false;

    public void switchCat(View view) {
        ImageView catImageView = findViewById(R.id.catImageView);

        cat = !cat;

        if(cat == true) catImageView.setImageResource(R.drawable.sylvester);
        else catImageView.setImageResource(R.drawable.garfield);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}