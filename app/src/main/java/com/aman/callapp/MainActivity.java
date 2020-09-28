package com.aman.callapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    DbHelper myDb;
    //location code starts
    SupportMapFragment supportMapFragment;
    //end
    String edhiNumber= "12345";
    String fireBrigadeNumber= "678910";
    double latitude;
    double longitude;
    public static SharedPreferences sharedPreferences;
    public static boolean isLoggedIn = false;


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DbHelper(this);





        sharedPreferences = getSharedPreferences("callapp", Context.MODE_PRIVATE);

        int users = sharedPreferences.getInt("users",0);

        if(!isLoggedIn) {
            Intent intent;
            if(users == 0) {
                intent = new Intent(this, SignupActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        } else {

            if(ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION_PERMISSION
                );

            } else {
                getCurrentLocation();
            }
        }



    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);


        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();

                            longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            Log.i("PEEP", String.format(
                                    "Latitude: %s\nLongitude: %s",
                                    latitude, longitude
                            ));
//
//                            showLocationOnMap();

                            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //init lat lng
                            Log.i("PEEP", "Map is ready");

                            LatLng latLng = new LatLng(latitude,longitude);
//                            create marker options

                            Log.i("PEEP", "onMapReady Originla: "+latitude+" - "+longitude);

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Here I am!");
                            //zoom map

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            //add marker on map
                            googleMap.addMarker(options);
                        }
                    });





                        }
                    }
                }, Looper.getMainLooper());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public void getUserLocation() {
//
//        //location code starts
//
//        client = LocationServices.getFusedLocationProviderClient(this);
//
//        //check permission
//        if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            //when permission granted
//            Log.i("PEEP", "onRequestPermissionsResult: Permission is already Granted");
//
//            getCurrentLocation();
//        } else {
//            //when permission is not granted
//            //request permission
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//        }
//        //end
//    }

//    private void showLocationOnMap() {
//        //initialize task location
//        Task<Location> task = client.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(final Location location) {
//                //When success
//                if(location != null) {
//                    //Sync Map
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(GoogleMap googleMap) {
//                            //init lat lng
//                            Log.i("PEEP", "Map is ready");
//
//                            LatLng latLng = new LatLng(latitude,longitude);
////                            create marker options
//
//                            Log.i("PEEP", "onMapReady Originla: "+latitude+" - "+longitude);
//
//                            MarkerOptions options = new MarkerOptions().position(latLng)
//                                    .title("Here I am!");
//                            //zoom map
//
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                            //add marker on map
//                            googleMap.addMarker(options);
//                        }
//                    });
//                } else
//                    Log.i("PEEP", "Fucking same problem again"+location);
//
//            }
//        });
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        if (requestCode == 44) {
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //When permission granted
//                //call method
//                Log.i("PEEP", "onRequestPermissionsResult: Permission Granted");
//                getCurrentLocation();
//            }
//        }
//    }

    public void callEdhi(View view) {
        Toast.makeText(getApplicationContext(), "Calling Edhi Helpline!",
                Toast.LENGTH_SHORT).show();
        call(edhiNumber);
    }

    public void callFireBrigade(View view) {
        Toast.makeText(getApplicationContext(), "Calling Fire Brigade!",
                Toast.LENGTH_SHORT).show();
        call(fireBrigadeNumber);
    }


    //Code to call on specific number
    public void call(String number) {
        //another piece of code
        Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
        callIntent.setData(Uri.parse("tel:"+number));    //this is the phone number calling
        //check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            call(number);
        }else {
            //have got permission
            try{
                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
