package com.example.shaym.minesweeper.UI.LeaderBoards;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.shaym.minesweeper.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LeaderBoardsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LAT = "LAT";
    private static final String LNG = "LNG";
    private Double lat;
    private Double lng;
    private SupportMapFragment map;
    private Location mChosenLocation;

    public static void start(Activity context, Double lat, Double lng) {
        final Intent intent = new Intent(context, LeaderBoardsActivity.class);
        intent.putExtra(LAT, lat);
        intent.putExtra(LNG, lng);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inits map with location received from records fragment
        super.onCreate(savedInstanceState);
        final Bundle extras = getIntent().getExtras();
        this.lat = extras.getDouble(LAT);
        this.lng = extras.getDouble(LNG);
        this.mChosenLocation = new Location("");
        this.mChosenLocation.setLatitude(this.lat);
        this.mChosenLocation.setLongitude(this.lng);
        setContentView(R.layout.activity_leaderboards);
        this.map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.map.getMapAsync(this);//remember getMap() is deprecated!
    }
    // get map to location
    @Override
    public void onMapReady(GoogleMap map) {
        Log.v(LAT, mChosenLocation.toString());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mChosenLocation.getLatitude(), mChosenLocation.getLongitude()), 16));
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(mChosenLocation.getLatitude(), mChosenLocation.getLongitude())));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);

    }


}