package com.example.poruhakaseno.mapalert;

import android.app.NotificationManager;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.example.poruhakaseno.mapalert.ProximityIntentReceiver.NOTIFICATION_ID;


public class Map extends FragmentActivity implements OnMapReadyCallback {
    private static final String PROX_ALERT_INTENT = "com.example.poruhakaseno.mapalert";

    private GoogleMap mMap;
    GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;
    private GoogleApiClient googleApiClient;
    NotificationManager manager;




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

        Button yourButton = (Button) findViewById(R.id.button3);
        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mMap.clear();
                mydest = null;
                Toast.makeText(getApplicationContext(),"Already Cleared..",Toast.LENGTH_SHORT).show();
               // mMap.moveCamera();
            }
        });

        Button butclear = (Button) findViewById(R.id.button2);
        butclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.cancel(1000);
                Toast.makeText(getApplicationContext(),"Alert Stopped.. ",Toast.LENGTH_SHORT).show();
            }
        });


        Button butstart = (Button) findViewById(R.id.button4);
        butstart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                double lat2 = mydest.latitude;
                double lon2 = mydest.longitude;
                double lat1 = mMap.getMyLocation().getLatitude();
                double lon1 = mMap.getMyLocation().getLongitude();

                addProximityAlert();
                Location locationA = new Location("point A");
                locationA.setLatitude(lat1);
                locationA.setLongitude(lon1);
                Location locationB = new Location("point B");
                locationB.setLatitude(lat2);
                locationB.setLongitude(lon2);
                float distance = locationA.distanceTo(locationB);
                Toast.makeText(getApplicationContext(), "Distance: "
                        + distance, Toast.LENGTH_SHORT).show();
                //add proxi alert
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(checkPermission()){}
                locationManager.addProximityAlert(mydest.latitude,mydest.longitude, 50, 60000, proximityIntent);
                IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
                registerReceiver(new ProximityIntentReceiver(), filter);
                Toast.makeText(getApplicationContext(),"Alert Started.. ",Toast.LENGTH_SHORT).show();




            }
        });

    }


    PendingIntent proximityIntent;
    private void addProximityAlert() {
        Intent intent = new Intent(PROX_ALERT_INTENT);
         proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
    }
    private static final String TAG = "location";
    static final int  REQUEST_locate_PERMISSION = 1;
    private boolean checkPermission(){
        if (android.os.Build.VERSION.SDK_INT >= 23 ) {
            //requestPermissions(new String[]{Manifest.permission.LOCATION}, REQUEST_locate_PERMISSION);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]   permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_locate_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Premission granted");
                }else {
                    Log.d(TAG,"Premission denied");
                }
                break;
        }
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
    LatLng mydest = null;
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
                mydest = latLng;
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
