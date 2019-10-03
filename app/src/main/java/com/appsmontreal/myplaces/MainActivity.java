package com.appsmontreal.myplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.appsmontreal.myplaces.Model.Place;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageButton goMapsImageButton;
    private ListView placesListView;
    private EditText editTextFilter;
    private Intent intent;
    private ArrayAdapter<String> arrayAdapter;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;

    /////////////////
    private ArrayList<String> placesName;
    private ArrayList<String> latitudes;
    private ArrayList<String> longitudes;

    SharedPreferences sharedPreferences;
    ObjectSerializer objectSerializer;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goMapsImageButton = findViewById(R.id.goMapsImageButton);
        editTextFilter = findViewById(R.id.editTextFilter);
        placesListView = findViewById(R.id.placesListView);

        //Code for the expression's filter(search)
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (MainActivity.this).arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ///////////////////////////
        sharedPreferences = getSharedPreferences("placesData", Context.MODE_PRIVATE);
        placesName = new ArrayList<>();
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();

        locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        goMapsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMapActivity(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            }
        });

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                launchMapActivity(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            }
        }
        placesName.clear();
        latitudes.clear();
        longitudes.clear();
        fillUpList();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("------------>", "We're here onRestart ------->");
//        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Place.places);
//        placesListView.setAdapter(arrayAdapter);


        for (Place place : Place.places) {
            placesName.add(place.getAddress());
            latitudes.add(Double.toString(place.getLatitude()));
            longitudes.add(Double.toString(place.getLongitude()));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString("placesName", objectSerializer.serialize(placesName)).apply();
            editor.putString("latitudes", objectSerializer.serialize(latitudes)).apply();
            editor.putString("longitudes", objectSerializer.serialize(longitudes)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }


        fillUpList();
//        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                launchMapActivity(Place.places.get(i).getLatitude(), Place.places.get(i).getLongitude());
//                launchMapActivity(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
//            }
//        });
    }

    private void launchMapActivity(double latitude, double longitude) {
        intent = new Intent(this,MapsActivity.class);
        intent.putExtra("LATITUDE",latitude);
        intent.putExtra("LONGITUDE",longitude);
        startActivityForResult(intent,1);
    }

    private void fillUpList() {
        try {
            placesName = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString("placesName",objectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString("latitudes",objectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString("longitudes",objectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("------------>Array", placesName.toString());
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,placesName);
        placesListView.setAdapter(arrayAdapter);

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                launchMapActivity(Place.places.get(i).getLatitude(), Place.places.get(i).getLongitude());
                launchMapActivity(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
            }
        });
    }


}
