package com.example.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public void fade(View view) {
        Log.i("Info", "Imageview tapped!");

        ImageView bartImageView = findViewById(R.id.bartImageView);

        bartImageView.animate().scaleX(0.5f).scaleY(0.5f).setDuration(1000);
    }

    public void initialAnimation() {
        ImageView bartImageView = findViewById(R.id.bartImageView);

        bartImageView.setX(-1200);

        bartImageView.animate().translationXBy(1200).rotation(3600).setDuration(2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialAnimation();



    }
}