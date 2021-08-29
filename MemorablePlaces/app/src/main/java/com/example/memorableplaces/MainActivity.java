package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ListView placesListView;
    static ArrayList<String> markerTitles = new ArrayList<>();
    static ArrayList<MarkerInfo> markers = new ArrayList<>();
    static ArrayAdapter<String> placesAdapter;
    MarkerInfo markerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
        try {
            String markerTitlesValues = sharedPreferences.getString("markerTitles", ObjectSerializer.serialize(new ArrayList<String>(Arrays.asList("Add a new place..."))));
            String markersValues = sharedPreferences.getString("markers", new Gson().toJson(new ArrayList<MarkerInfo>()));
            markerTitles = (ArrayList<String>) ObjectSerializer.deserialize(markerTitlesValues);
            markers = new Gson().fromJson(markersValues, new TypeToken<ArrayList<MarkerInfo>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        placesListView = (ListView) findViewById(R.id.placesListView);

        // Settings of the list view
        placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, markerTitles);
        placesListView.setAdapter(placesAdapter);

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if(position == 0) {
                    markerInfo = new MarkerInfo("You are here", null);
                }
                else {
                    markerInfo = markers.get(position - 1);
                }
                intent.putExtra("MarkerInfo", markerInfo);
                startActivity(intent);
            }
        });
    }
}


class MarkerInfo implements Parcelable {
    private String title;
    private LatLng coordinates;

    public MarkerInfo(String title, LatLng coordinates) {
        this.title = title;
        this.coordinates = coordinates;
    }

    protected MarkerInfo(Parcel in) {
        title = in.readString();
        coordinates = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(coordinates, flags);
    }

    public String getTitle() {
        return title;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public static final Creator<MarkerInfo> CREATOR = new Creator<MarkerInfo>() {
        @Override
        public MarkerInfo createFromParcel(Parcel in) {
            return new MarkerInfo(in);
        }

        @Override
        public MarkerInfo[] newArray(int size) {
            return new MarkerInfo[size];
        }
    };
}
