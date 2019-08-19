package com.appsmontreal.myplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.appsmontreal.myplaces.Model.Place;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements MemorablePlace {
    private ImageButton goMapsImageButton;
    private ListView placesListView;
    private EditText filterEditText;
    private Intent intent;
    private ArrayAdapter<Place> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goMapsImageButton = findViewById(R.id.goMapsImageButton);
        filterEditText = findViewById(R.id.filterEditText);
        placesListView = findViewById(R.id.placesListView);

        goMapsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMapActivity(45.5017,73.5673);
            }
        });
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.i("------------>", "We're here onRestart ------->");
//        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Place.places);
//        placesListView.setAdapter(arrayAdapter);
//        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                launchMapActivity(Place.places.get(i).getLatitude(), Place.places.get(i).getLongitude());
//            }
//        });
//    }

    private void launchMapActivity(double latitude, double longitude) {
        intent = new Intent(this,MapsActivity.class);
        intent.putExtra("LATITUDE",latitude);
        intent.putExtra("LONGITUDE",longitude);
        startActivityForResult(intent,1);
    }


    @Override
    public void getNewMemorablePlace(Place place) {
        Log.i("\n\n------------>", "interface method" + place + "\n\n");
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Place.places);
        placesListView.setAdapter(arrayAdapter);
        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchMapActivity(Place.places.get(i).getLatitude(), Place.places.get(i).getLongitude());
            }
        });
    }

}
