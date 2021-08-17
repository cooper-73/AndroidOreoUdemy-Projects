package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    MarkerInfo markerInfo;
    LatLng userCoordinate;
    Marker userMarker;

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
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        markerInfo = intent.getParcelableExtra("MarkerInfo");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set passed marker if it is not null
        if(markerInfo.getCoordinates() != null) {
            mMap.addMarker(new MarkerOptions().position(markerInfo.getCoordinates())
                    .title(markerInfo.getTitle())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerInfo.getCoordinates(), 14));
        }

        // COMPLETE - Set listener for long clicks on map
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String newTitle = "";
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses != null && addresses.size() > 0) {
                        if(addresses.get(0).getThoroughfare() != null) {
                            if(addresses.get(0).getSubThoroughfare() != null) {
                                newTitle += addresses.get(0).getSubThoroughfare() + " ";
                            }
                            newTitle += addresses.get(0).getThoroughfare();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(newTitle.equals("")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    Date date = new Date();
                    newTitle = formatter.format(date);
                }

                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(newTitle)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                MarkerInfo newMarkerInfo = new MarkerInfo(newTitle, latLng);
                MainActivity.markerTitles.add(newTitle);
                MainActivity.markers.add(newMarkerInfo);
                MainActivity.placesAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Location saved!", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting listener for location changes
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userCoordinate = new LatLng(location.getLatitude(), location.getLongitude());
                if(userMarker == null) {
                    userMarker = mMap.addMarker(new MarkerOptions().position(userCoordinate).title("You are here!"));
                    if(markerInfo == null) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoordinate, 14));
                }
                else {
                    userMarker.remove();
                    userMarker = mMap.addMarker(new MarkerOptions().position(userCoordinate).title("You are here!"));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        // Setting location permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null) {
                userCoordinate = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                userMarker = mMap.addMarker(new MarkerOptions().position(userCoordinate).title("You are here!"));
                if(markerInfo.getCoordinates() == null) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoordinate, 14));
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
