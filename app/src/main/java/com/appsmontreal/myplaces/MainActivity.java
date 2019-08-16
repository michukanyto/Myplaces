package com.appsmontreal.myplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MemorablePlace {
    private ImageButton goMapsImageButton;
    private ListView placesListView;
    private EditText filterEditText;
    private Intent intent;

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
                startActivity(intent);
//                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    public void getNewMemorablePlace(double latitude, double longitude, String address) {
        Log.i("\n\n------------>", "We're here" + " address\n\n");
    }
}
