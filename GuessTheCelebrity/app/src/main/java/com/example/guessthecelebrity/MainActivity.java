package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    ArrayList<Integer> indexes = new ArrayList<>();
    ImageView celebrityImageView;
    int chosenIndex;
    int numberOfOptions = 4;

    public void clickOption(View view) {
        Integer tag = Integer.parseInt((String) view.getTag());
        String message = null;

        if(tag == chosenIndex) {
            message = "Correct!";
            Log.i("Info", "Correct");
        }
        else {
            message = "Wrong! It was " + celebNames.get(indexes.get(tag));
            Log.i("Info", "Incorrect");
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        setRound();
    }

    public void setRoundOptions() {
        for(int i = 0; i < numberOfOptions; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button option = findViewById(id);

            option.setText(celebNames.get(indexes.get(i)));
        }
    }

    public void setRoundImage() {
        try {
            ImageDownloader imageTask = new ImageDownloader();
            Bitmap celebImage = imageTask.execute(celebURLs.get(indexes.get(chosenIndex))).get();
            celebrityImageView.setImageBitmap(celebImage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setRound() {
        Collections.shuffle(indexes);
        Random rand = new Random();
        chosenIndex = rand.nextInt(numberOfOptions);

        setRoundImage();
        setRoundOptions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebrityImageView = findViewById(R.id.celebrityImageView);

        for(int i = 0; i < 100; i++) {
            indexes.add(i);
        }

        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            result = task.execute("https://www.imdb.com/list/ls052283250/").get();

            String[] splitResult = result.split("<div class=\"lister-list\">");

            Pattern p = Pattern.compile("img alt=\"(.*?)\"height=\"(.*?)\"src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[1]);

            while (m.find()) {
                celebURLs.add(m.group(3));
                celebNames.add(m.group(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setRound();
    }
}

// Get code from webpage
class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }
}

// Get image bitmap
class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);

            return myBitmap;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}