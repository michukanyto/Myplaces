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
//                intent = new Intent(view.getContext(),MapsActivity.class);
//                startActivityForResult(intent,1);
                launchMapActivity();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("------------>", "We're here onRestart ------->");
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Place.places);
        placesListView.setAdapter(arrayAdapter);
        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchMapActivity();
            }
        });
    }

    private void launchMapActivity() {
        intent = new Intent(this,MapsActivity.class);
        startActivityForResult(intent,1);
    }



    @Override
    public void getNewMemorablePlace(Place place) {
        Log.i("\n\n------------>", "interface method" + place + "\n\n");
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
}
