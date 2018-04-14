package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group5.weatherprediction.R;
import android.location.Geocoder;
import android.location.Address;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.group5.weatherprediction.WeatherByAddressActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import models.OpenWeatherJSon;

public class WeatherAsyncTask extends AsyncTask<Void, Void, OpenWeatherJSon> {
    ProgressDialog dialog;
    Activity activity;
    TypePrediction typePrediction;
    String q;
    double latitude;
    double longitude;
    NumberFormat format = new DecimalFormat("#0.0");
    Bitmap myBitmap=null;

    Marker marker=null;
    GoogleMap map=null;


    public WeatherAsyncTask(Activity activity,String q)
    {
        this.activity=activity;
        this.typePrediction=TypePrediction.ADDRESS_NAME;
        this.q=q;
        this.dialog=new ProgressDialog(activity);
        this.dialog.setTitle("Đang tải thông tin ...");
        this.dialog.setMessage("Vui lòng chờ...");
        this.dialog.setCancelable(true);
    }

    public WeatherAsyncTask(Activity activity,double latitude,double longitude)
    {
        this.activity=activity;
        this.typePrediction=TypePrediction.LATITUDE_LONGITUDE;
        this.latitude=latitude;
        this.longitude=longitude;
        this.dialog=new ProgressDialog(activity);
        this.dialog.setTitle("Đang tải thông tin ...");
        this.dialog.setMessage("Vui lòng chờ...");
        this.dialog.setCancelable(true);
    }

    public WeatherAsyncTask(Marker marker,GoogleMap map,Activity activity,double latitude,double longitude)
    {
        this(activity,latitude,longitude);
        this.marker=marker;
        this.map=map;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog.show();
    }

    @Override
    protected OpenWeatherJSon doInBackground(Void... voids) {
        OpenWeatherJSon openWeatherJSon=null;
        if(typePrediction== TypePrediction.LATITUDE_LONGITUDE)
            openWeatherJSon= OpenWeatherAPI.prediction(latitude,longitude);
        else
            openWeatherJSon= OpenWeatherAPI.prediction(q);
        try {
            String idIcon = openWeatherJSon.getWeather().get(0).getIcon().toString();
            String urlIcon = "http://openweathermap.org/img/w/"+idIcon+".png";

            URL urlConnection = new URL(urlIcon);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            //Tiến hành convert qua hình ảnh
            myBitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            //e.printStackTrace();
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            dialog.setMessage(s);
        }
        return openWeatherJSon;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(OpenWeatherJSon openWeatherJSon) {
        super.onPostExecute(openWeatherJSon);
        if(map!=null) {
            map.setInfoWindowAdapter(new MyInfoWindowAdapter(openWeatherJSon,myBitmap,marker, this.activity,latitude,longitude));
            marker.showInfoWindow();
            this.dialog.dismiss();
            return;
        }
        if(openWeatherJSon == null){
            this.dialog.dismiss();
            return;
        }
        TextView txtTemperature=(TextView) activity.findViewById(R.id.txtTemperature);
        TextView txtCurrentAddressName=(TextView) activity.findViewById(R.id.txtCurrentAddressName);
        ImageView imageView=(ImageView) activity.findViewById(R.id.imgBauTroi);
        TextView txtMaxtemp=(TextView) activity.findViewById(R.id.txtMaxTemp);
        TextView txtMinTemp=(TextView) activity.findViewById(R.id.txtMinTemp);
        TextView txtWind=(TextView) activity.findViewById(R.id.txtWind);
        TextView txtCloudliness= (TextView) activity.findViewById(R.id.txtCloudliness);
        TextView txtPressure= (TextView) activity.findViewById(R.id.txtPressure);
        TextView txtHumidty= (TextView) activity.findViewById(R.id.txtHumidty);
        TextView txtSunrise= (TextView) activity.findViewById(R.id.txtSunrise);
        TextView txtSunset= (TextView) activity.findViewById(R.id.txtSunset);
        String temperature=format.format(openWeatherJSon.getMain().getTemp()-273.15) + "°C";;
        String maxtemp= format.format(openWeatherJSon.getMain().getTemp_max()-273.15)+"°C";
        String mintemp= format.format(openWeatherJSon.getMain().getTemp_min()-273.15)+"°C";
        String wind= openWeatherJSon.getWind().getSpeed()+" m/s";
        String mesg = openWeatherJSon.getWeather().get(0).getMain();

        String cloudiness=mesg;
        String pressure= openWeatherJSon.getMain().getPressure()+" hpa";
        String humidity=openWeatherJSon.getMain().getHumidity()+" %";

        Calendar timeSunrise = Calendar.getInstance();
        timeSunrise.setTime(new Date(openWeatherJSon.getSys().getSunrise()*1000));
        String Sunrise= timeSunrise.get(Calendar.HOUR)+":"+timeSunrise.get(Calendar.MINUTE)+" AM";


        //Date timeSunSet = new Date(openWeatherJSon.getSys().getSunset()*1000);
        Calendar timeSunSet = Calendar.getInstance();
        timeSunSet.setTime(new Date(openWeatherJSon.getSys().getSunset()*1000));
        String sunset= timeSunSet.get(Calendar.HOUR)+":"+timeSunSet.get(Calendar.MINUTE);

        txtTemperature.setText(temperature);
        imageView.setImageBitmap(myBitmap);
        txtMaxtemp.setText(maxtemp);
        txtMinTemp.setText(mintemp);
        txtWind.setText(wind);
        txtCloudliness.setText(cloudiness);
        txtPressure.setText(pressure);
        txtHumidty.setText(humidity);
        txtSunrise.setText(Sunrise);
        txtSunset.setText(sunset);

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this.activity, Locale.getDefault());
            if(typePrediction==TypePrediction.LATITUDE_LONGITUDE)
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            else
            {
                addresses = geocoder.getFromLocationName(q, 1);
            }
            Address address=null;
            if(addresses.size()>0)
                address=addresses.get(0);
            if(address!=null)
            {
                if(typePrediction==TypePrediction.LATITUDE_LONGITUDE)
                    txtCurrentAddressName.setText(address.getAddressLine(0));
                else
                    txtCurrentAddressName.setText(q);
                /*String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String postalCode = address.getPostalCode();
                String knownName = address.getFeatureName();*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.dialog.dismiss();
    }
}
