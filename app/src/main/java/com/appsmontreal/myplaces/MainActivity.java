package com.appsmontreal.myplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
                intent = new Intent(view.getContext(),MapsActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("------------>", "We're here onRestart ------->");
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Place.places);
        placesListView.setAdapter(arrayAdapter);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("\n\n------------>", "We're here" + "\n\n");
//        if (requestCode == 1) {
//            if (requestCode == RESULT_OK) {
//               Place place = (Place) data.getSerializableExtra("NEW_POINT");
//                Log.i("\n\n------------>", "We're here" + place + "\n\n");
//
////                arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Place.places);
////                placesListView.setAdapter(arrayAdapter);
//
//            } else if (requestCode == RESULT_CANCELED) {
//                Log.i("\n\n------------>", "Any place selected" + "\n\n");
//            }
//
//        }
//    }

    @Override
    public void getNewMemorablePlace(Place place) {
        Log.i("\n\n------------>", "interface method" + place + "\n\n");
    }
}
