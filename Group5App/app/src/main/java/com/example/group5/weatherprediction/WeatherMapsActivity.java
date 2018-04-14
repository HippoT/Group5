package com.example.group5.weatherprediction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import utils.WeatherAsyncTask;

public class WeatherMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Loading ...");
        myProgress.setMessage("Please wait!");
        myProgress.setCancelable(true);
        myProgress.show();

    }

    public void addEvents()
    {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                moveAndShowWeatherNewPlace(latLng);
            }
        });
    }

    private void moveAndShowWeatherNewPlace(LatLng latLng) {
        if (latLng != null)
        {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latLng.latitude, latLng.longitude), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latLng.latitude, latLng.longitude))
                    .zoom(15)
                    .bearing(90)
                    .tilt(40)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            MarkerOptions option=new MarkerOptions();
            option.position(new LatLng(latLng.latitude, latLng.longitude));
            option.title("Location").snippet("You are staying here!");
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            Marker maker = googleMap.addMarker(option);
            WeatherAsyncTask task=new WeatherAsyncTask(maker,googleMap,WeatherMapsActivity.this,latLng.latitude,latLng.longitude);
            task.execute();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        //LatLng sydney = new LatLng(-34, 151);
        //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        addEvents();
        if(checkPermission()){
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMyLocationEnabled(true);
            myProgress.dismiss();
            GetWeatherPrediction();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(WeatherMapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(WeatherMapsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }).create().show();
            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetWeatherPrediction();
                }
                return;
            }

        }
    }

    public void GetWeatherPrediction(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (lastLocation != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                        .zoom(15)
                        .bearing(90)
                        .tilt(40)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                MarkerOptions option=new MarkerOptions();
                option.position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                option.title("Cho o cua tui").snippet("Tran Duy Thanh");
                option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                Marker maker = googleMap.addMarker(option);
                WeatherAsyncTask task=new WeatherAsyncTask(maker,googleMap,WeatherMapsActivity.this,lastLocation.getLatitude(),lastLocation.getLongitude());
                task.execute();
            }
        }
    }


}
