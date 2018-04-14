package com.example.group5.weatherprediction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SelectCityActivity extends AppCompatActivity {

    AutoCompleteTextView txtAddress;
    ListView lvTinhThanh;
    String []arrTinhThanh;
    ArrayAdapter<String> adapterTinhThanh;
    Button btnCheckWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        addControls();
        addEvents();
    }

    public void  addControls()
    {
        txtAddress=(AutoCompleteTextView)findViewById(R.id.txtAddressCheck);
        lvTinhThanh= (ListView) findViewById(R.id.lvTinhThanh);
        arrTinhThanh=getResources().getStringArray(R.array.listCities);
        adapterTinhThanh=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrTinhThanh);
        lvTinhThanh.setAdapter(adapterTinhThanh);
        btnCheckWeather= (Button) findViewById(R.id.btnCheckWeather);
        txtAddress.setAdapter(adapterTinhThanh);
    }
    public  void addEvents() {
        txtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapterTinhThanh.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnCheckWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mở màn hình xem thời tiết theo địa chỉ nhập bất kỳ
                Intent intent = new Intent(SelectCityActivity.this, WeatherByAddressActivity.class);
                //truyền địa chỉ qua bên kia để xử lý
                intent.putExtra("ADDRESS", txtAddress.getText().toString());
                startActivity(intent);
            }
        });
        lvTinhThanh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtAddress.setText(arrTinhThanh[position]);
            }
        });
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
