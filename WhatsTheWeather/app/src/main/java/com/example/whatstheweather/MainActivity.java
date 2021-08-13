package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView infoTextView;

    public void setWeatherInfo(JSONObject jsonInfo) {
        String message = "";
        try {
            JSONArray jsonArray = jsonInfo.getJSONArray("weather");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject current = jsonArray.getJSONObject(i);
                String main = current.get("main").toString();
                String description = current.get("description").toString();
                message += String.format("%s: %s\n", main, description);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            message = "No Result Found";
        }

        infoTextView.setText(message);
    }

    public void checkWeather(View view) {
        String city = cityEditText.getText().toString();

        if(city.equals("")) {
            Toast.makeText(this, "Enter a city", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=70079ae0da1bcaf813150ef5564872ba&units=metric";

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        WeatherTask task = new WeatherTask();

        try {
            JSONObject jsonObject = task.execute(url).get();
            setWeatherInfo(jsonObject);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class WeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                String result = "";
                JSONObject jsonResult;
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                jsonResult = new JSONObject(result);

                return jsonResult;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        infoTextView = findViewById(R.id.infoTextView);
    }
}

