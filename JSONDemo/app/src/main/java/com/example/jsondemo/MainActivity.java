package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherTask task = new WeatherTask();

        task.execute("http://api.openweathermap.org/data/2.5/weather?q=Lima,pe&appid=334c05e3c93928fc8c915581f74b0a1d&units=metric");
    }
}

class WeatherTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        try {
            String result = "";
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while(data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);

            String weatherInfo = jsonObject.getString("weather");
            Log.i("Weather", weatherInfo);

            JSONArray jsonArray = new JSONArray(weatherInfo);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPart = jsonArray.getJSONObject(i);
                Log.i("main", jsonPart.getString("main"));
                Log.i("description", jsonPart.getString("description"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}