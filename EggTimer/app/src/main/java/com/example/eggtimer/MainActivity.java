package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar timeSeekBar;
    TextView timeTextView;
    MediaPlayer countDownAlarm;
    int timeCountDown = 30;
    int maxTimeCountDown = 300;
    boolean isWhite = true;

    public void countDown(View view) {

        Log.i("Seconds left!", Integer.toString(timeCountDown));

        view.setEnabled(false);
        timeSeekBar.setEnabled(false);

        new CountDownTimer(timeCountDown * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                setCountDown(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                countDownAlarm.start();

                new CountDownTimer(5000, 500) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(isWhite) timeTextView.setTextColor(Color.RED);
                        else    timeTextView.setTextColor(Color.WHITE);
                        isWhite = !isWhite;
                    }

                    @Override
                    public void onFinish() {
                        countDownAlarm.stop();

                        view.setEnabled(true);
                        timeSeekBar.setEnabled(true);
                        setInitialSettings();
                    }
                }.start();
            }
        }.start();

    }

    public void setInitialSettings() {
        isWhite = true;
        timeTextView.setTextColor(Color.WHITE);
        timeSeekBar.setProgress(60);
        setCountDown(60 * 1000);
    }

    public void setCountDown(long timeUntilFinished) {
        timeTextView.setText(String.format("%02d:%02d", timeUntilFinished / 60000, (timeUntilFinished / 1000) % 60));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTextView = findViewById(R.id.timeTextView);

        timeSeekBar = findViewById(R.id.timeSeekBar);
        timeSeekBar.setMax(maxTimeCountDown);

        countDownAlarm = MediaPlayer.create(getApplicationContext(), R.raw.alarm_ringtone);

        setInitialSettings();

        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeCountDown = progress;
                setCountDown(timeCountDown * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Button goButton = findViewById(R.id.goButton);
                goButton.setEnabled(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Button goButton = findViewById(R.id.goButton);
                goButton.setEnabled(true);
            }
        });
    }
}