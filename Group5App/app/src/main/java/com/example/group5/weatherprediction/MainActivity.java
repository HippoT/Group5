package com.example.group5.weatherprediction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClkDisplayCurrentWeather(View view) {
        if(!isNetworkConnected()){
            Toast.makeText(MainActivity.this, "Please check your connection", Toast.LENGTH_LONG).show();
            return;
        }
        Intent iCurrentWeather = new Intent(MainActivity.this, WeatherCurrentLocationActivity.class);
        startActivity(iCurrentWeather);
    }

    public void onClkDisplayWeatherByAddress(View view) {
        if(!isNetworkConnected()){
            Toast.makeText(MainActivity.this, "Please check your connection", Toast.LENGTH_LONG).show();
            return;
        }
        Intent iWeatherByAddress = new Intent(MainActivity.this, AutoCompletePlaceActivity.class);
        startActivity(iWeatherByAddress);
    }

    public void onClkShowMap(View view) {
        if(!isNetworkConnected()){
            Toast.makeText(MainActivity.this, "Please check your connection", Toast.LENGTH_LONG).show();
            return;
        }
        Intent iWeatherMaps = new Intent(MainActivity.this, WeatherMapsActivity.class);
        startActivity(iWeatherMaps);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connection.getActiveNetworkInfo();
        if(netInfo == null){
            return false;
        }
        return netInfo.isConnectedOrConnecting();
    }
}
