package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeLeftTextView;
    TextView sumTextView;
    TextView scoreTextView;
    TextView commentaryTextView;
    GridLayout gridLayout;
    Button playAgainButton;
    long secondsLeft = 60;
    int rounds = 0;
    int score = 0;
    int sum;
    boolean isActive = false;
    ArrayList<Integer> options = new ArrayList<Integer>();

    @SuppressLint("SetTextI18n")
    public void setOptions(int sum) {
        Random r = new Random();
        int pos = r.nextInt(4);
        boolean isAnswerSet = false;

        Collections.shuffle(options);

        for(int i = 0; i < gridLayout.getChildCount(); i++) {
            Button child = (Button) gridLayout.getChildAt(i);
            int value = options.get(i);

            if(value == sum)    isAnswerSet = true;

            child.setText(String.format("%d", value));
            child.setTag(value);
        }

        if(!isAnswerSet) {
            Button child = (Button) gridLayout.getChildAt(pos);
            child.setText(String.format("%d", sum));
            child.setTag(sum);
        }
    }

    @SuppressLint("DefaultLocale")
    public void setRound() {
        int num1, num2;
        Random r = new Random();

        num1 = r.nextInt(15) + 1;
        num2 = r.nextInt(15) + 1;
        sum = num1 + num2;

        sumTextView.setText(String.format("%d+%d", num1, num2));
        scoreTextView.setText(String.format("%d/%d", score, rounds));

        setOptions(sum);
    }

    @SuppressLint("SetTextI18n")
    public void setGame() {


        timeLeftTextView.setVisibility(View.VISIBLE);
        sumTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);

        setRound();
    }

    public void enableButtons() {
        for(int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);

            button.setEnabled(true);
        }
    }

    public void disableButtons() {
        for(int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);

            button.setEnabled(false);
        }
    }

    public void go(View view) {
        view.setVisibility(View.INVISIBLE);

        isActive = true;
        enableButtons();
        setGame();

        new CountDownTimer((secondsLeft + 1) * 1000, 1000) {

            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText(String.format("%ds", millisUntilFinished / 1000));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                playAgainButton.setVisibility(View.VISIBLE);
                isActive = false;

                commentaryTextView.setText("Done!");

                disableButtons();
            }
        }.start();

    }

    public void checkAnswer(View view) {
        int answer = (int) view.getTag();
        String commentary;

        if(answer == sum) {
            commentary = "Correct!";
            score++;
        }
        else {
            commentary = "Wrong!";
        }

        commentaryTextView.setText(commentary);
        commentaryTextView.setVisibility(View.VISIBLE);
        rounds++;

        if(isActive)    setRound();
    }

    public void playAgain(View view) {
        score = 0;
        rounds = 0;
        go(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLeftTextView = findViewById(R.id.timeLeftTextView);
        sumTextView = findViewById(R.id.sumTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        commentaryTextView = findViewById(R.id.commentaryTextView);
        gridLayout = findViewById(R.id.gridLayout);
        playAgainButton = findViewById(R.id.playAgainButton);

        for(int i = 1; i <= 30; i++) {
            options.add(i);
        }

    }
}