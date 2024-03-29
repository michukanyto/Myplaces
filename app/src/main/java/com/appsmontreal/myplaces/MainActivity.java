package com.appsmontreal.myplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.appsmontreal.myplaces.Model.Place;
import com.appsmontreal.myplaces.Serializer.ObjectSerializer;

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
    private ArrayList<String> placesName;
    private ArrayList<String> latitudes;
    private ArrayList<String> longitudes;

    SharedPreferences sharedPreferences;
    ObjectSerializer objectSerializer;

    public enum constants {
        LATITUDE("LATITUDE"),
        LONGITUDE("LONGITUDE"),
        FILENAME("placesData"),
        PLACESNAME("placesName"),
        PLACESLATITUDES("latitudes"),
        PLACESLONGITUDES("longitudes");

        public String getValue () {
            return value;
        }

        private String value;
        constants(String value) {
            this.value = value;
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }

    }


    //To inflate the menu in this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.profil:
                Log.i("----------------->", "you pressed profile");
                return true;

            case    R.id.help:
                Log.i("----------------->", "you pressed Help");
                return true;

            case    R.id.exit:
                finish();
                return true;

            case    R.id.map:
                launchMapActivity(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                return true;

            default:
                return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goMapsImageButton = findViewById(R.id.goMapsImageButton);
        editTextFilter = findViewById(R.id.editTextFilter);
        placesListView = findViewById(R.id.placesListView);
        launchAlert();


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

        sharedPreferences = getSharedPreferences(constants.FILENAME.getValue(), Context.MODE_PRIVATE);
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

    private void launchAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert")
                .setMessage("Do you want to save your locations?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("------------>", "You pressed Yes");
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("------------>", "We're here onRestart ------->");

        for (Place place : Place.places) {
            placesName.add(place.getAddress());
            latitudes.add(Double.toString(place.getLatitude()));
            longitudes.add(Double.toString(place.getLongitude()));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(constants.PLACESNAME.getValue(), objectSerializer.serialize(placesName)).apply();
            editor.putString(constants.PLACESLATITUDES.getValue(), objectSerializer.serialize(latitudes)).apply();
            editor.putString(constants.PLACESLONGITUDES.getValue(), objectSerializer.serialize(longitudes)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fillUpList();
    }


    private void launchMapActivity(double latitude, double longitude) {
        intent = new Intent(this,MapsActivity.class);
        intent.putExtra(constants.LATITUDE.getValue(),latitude);
        intent.putExtra(constants.LONGITUDE.getValue(),longitude);
        startActivityForResult(intent,1);
    }


    private void fillUpList() {
        try {
            placesName = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString(constants.PLACESNAME.getValue(),objectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString(constants.PLACESLATITUDES.getValue(),objectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString(constants.PLACESLONGITUDES.getValue(),objectSerializer.serialize(new ArrayList<String>())));
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
