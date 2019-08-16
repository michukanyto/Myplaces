package com.appsmontreal.myplaces;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Address> listAddresses;
    private Geocoder geocoder;
    private String newAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    listAddresses =  geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    getAddress();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(newAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                android.util.Log.i("onMapClick", "new point ===> !"+latLng.latitude + ","+latLng.longitude + "," + newAddress);
            }
        });

    }

    private void getAddress() {
        newAddress = "";
        if (listAddresses.get(0).getThoroughfare() != null) {
            newAddress += listAddresses.get(0).getThoroughfare() + ", ";
        }


        if (listAddresses.get(0).getLocality() != null) {
            newAddress += listAddresses.get(0).getLocality();
        }

//        if (listAddresses.get(0).getAdminArea() != null) {
//            anAddress += listAddresses.get(0).getAdminArea();
//        }
    }
}
