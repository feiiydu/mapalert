package com.example.poruhakaseno.mapalert;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Spinner  dropdown = (Spinner)findViewById(R.id.spinner1);

        String[] items = new String[]{"Choose radius","0.5 km", "1 km", "2 km"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);







    }
    public  int  returnradious(){
        Spinner  dropdown = (Spinner)findViewById(R.id.spinner1);
        int ans;
        if(dropdown.getSelectedItemPosition()==0){
            ans = 0;
            Toast.makeText(getBaseContext(), "Please choose radius.", Toast.LENGTH_SHORT).show();
        }else if(dropdown.getSelectedItemPosition()==1){
            ans = 500;
        }else if(dropdown.getSelectedItemPosition()==2){
            ans = 1000;
        }else{
            ans = 2000;
        }
        return ans;

    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng kmitl = new LatLng(13.752092, 100.500893);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kmitl,10.0f));
        googleMap.setOnMapClickListener(new OnMapClickListener() {
            Marker mydestination;
            @Override
            public void onMapClick(LatLng latLng) {
                mMap = googleMap;
                mMap.clear();
                mydestination = mMap.addMarker(new MarkerOptions().position(latLng).title("My Destination"));
                if(returnradious()!=0)Toast.makeText(getBaseContext(), "Proximity Alert is added", Toast.LENGTH_SHORT).show();
                mMap.addCircle(new CircleOptions().center(latLng).radius(returnradious()).fillColor(Color.BLUE).strokeWidth(2).strokeColor(Color.BLACK));
            }
        });

    }


    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);

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


}
