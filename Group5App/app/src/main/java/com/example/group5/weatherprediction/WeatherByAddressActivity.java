package com.example.group5.weatherprediction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import utils.WeatherAsyncTask;

public class WeatherByAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_by_address);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherByAddress();
    }

    /**
     * hàm gọi đa tiến trình truy suất thời tiết theo địa chỉ
     */
    private void getWeatherByAddress() {
        Intent i=getIntent();
        //lấy địa chỉ từ bên ChooseAddressActivity gửi qua:
        String q=i.getStringExtra("ADDRESS");
        WeatherAsyncTask task=new WeatherAsyncTask(this,q);
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
