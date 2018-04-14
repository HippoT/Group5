package com.example.group5.weatherprediction;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


public class AutoCompletePlaceActivity extends FragmentActivity {
    protected GeoDataClient mGeoDataClient;
    protected PlaceAutocompleteFragment autocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_place);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.frame_autocomplete);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(AutoCompletePlaceActivity.this, place.getName().toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(AutoCompletePlaceActivity.this, place.getLatLng().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AutoCompletePlaceActivity.this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}