package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

class MarkerInfo implements Serializable {
    String title;
    Location location;

    public MarkerInfo(String title, Location location) {
        this.title = title;
        this.location = location;
    }
}

public class MainActivity extends AppCompatActivity {
    ListView placesListView;
    ArrayList<String> placeList;
    ArrayList<Location> placeLocations;
    LocationManager locationManager;
    LocationListener locationListener;
    MarkerInfo markerInfo;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesListView = (ListView) findViewById(R.id.placesListView);
        placeList = new ArrayList<>();
        placeList.add("Add a new place...");
        placeLocations = new ArrayList<>();
        placeLocations.add(null);

        //Setting of location service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                placeLocations.set(0, location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        // Settings of the list view
        ArrayAdapter<String> placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, placeList);
        placesListView.setAdapter(placesAdapter);
        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if(position == 0) {
                    markerInfo = new MarkerInfo("You are here", placeLocations.get(0));
                }
                else {
                    markerInfo = new MarkerInfo(placeList.get(position), placeLocations.get(position));
                }
                intent.putExtra("markerInfo", markerInfo);
                startActivity(intent);
            }
        });

        //Setting location permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            placeLocations.set(0, locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}