package com.example.connect3game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int player = 0;
    int[] game = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
    boolean gameActive = true;

    public void clickCell(View view) {

        ImageView tokenImageView = (ImageView) view;
        int tag = Integer.parseInt(tokenImageView.getTag().toString());

        if(!gameActive) {
            Toast.makeText(this, "Touch PLAY AGAIN to start a new game", Toast.LENGTH_SHORT).show();
        }
        else if(game[tag] == -1) {
            if(player == 0)   tokenImageView.setImageResource(R.drawable.blue);
            else    tokenImageView.setImageResource(R.drawable.green);

            game[tag] = player;

            tokenImageView.setTranslationY(-1500);
            tokenImageView.animate().translationYBy(1500).setDuration(300);

            if(checkWinner()) {
                gameActive = false;
                showResults(player);
            }

            player = (player + 1) % 2;

        }
        else {
            Log.i("Info", "Invalid movement");
            Toast.makeText(this, "Invalid movement. Try another position!", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkWinner() {
        for(int i = 0; i < 9; i += 3) {
            if(game[i] != -1 && game[i] == game[i + 1] && game[i + 1] == game[i + 2])    return true;
        }
        for(int i = 0; i < 3; i++) {
            if(game[i] != -1 && game[i] == game[i + 3] && game[i + 3] == game[i + 6])    return true;
        }
        if((game[0] != -1 && game[0] == game[4] && game[4] == game[8]) || (game[2] != -1 && game[2] == game[4] && game[4] == game[6])) return true;
        return false;
    }

    public void playAgain(View view) {
        for(int i = 0; i < 9; i++)  game[i] = -1;

        findViewById(R.id.winnerTextView).setVisibility(View.INVISIBLE);
        findViewById(R.id.playAgainButton).setVisibility(View.INVISIBLE);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        for(int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView tokenImageView = (ImageView) gridLayout.getChildAt(i);

            tokenImageView.setImageDrawable(null);
        }

        gameActive = true;
    }

    public void showResults(int player) {
        TextView winnerTextView = findViewById(R.id.winnerTextView);

        String playerName;

        if(player == 0) playerName = "Blue";
        else    playerName = "Green";

        winnerTextView.setText(playerName + " has won!");

        winnerTextView.setVisibility(View.VISIBLE);
        findViewById(R.id.playAgainButton).setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}