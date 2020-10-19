package com.aman.callapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.format.Time;
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

import java.text.DecimalFormat;

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
    String callerNumber= "";
    String distance= "";

    //location variables

    LatLng latLng;
    LatLng callLocation;


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REQUEST_READ_PHONE_STATE = 11;


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void calculateDistance() {
        if(latLng!= null && callLocation!=null) {
            CalculationByDistance(latLng, callLocation);

        }
    }

    public String CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Toast.makeText(this, "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec, Toast.LENGTH_SHORT).show();

        return kmInDec+" Km";
    }

    public void showRecord() {
        Cursor cursor = myDb.getRecords();

        if (cursor.moveToFirst()){
            do{
                Double lat = cursor.getDouble(cursor.getColumnIndex("LAT"));
                Double lng = cursor.getDouble(cursor.getColumnIndex("LNG"));
                String number = cursor.getString(cursor.getColumnIndex("CALLER_NUMBER"));
                String time = cursor.getString(cursor.getColumnIndex("DATE_TIME"));
                // do what ever you want here


            }while(cursor.moveToNext());
        }
        cursor.close();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("callapp", Context.MODE_PRIVATE);

        myDb = new DbHelper(this);




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
                showRecord();
            }
        }



    }

    public void showReport(View view) {
//      Toast.makeText(this, "Show report", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);


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

                            latLng = new LatLng(latitude,longitude);
//                            create marker options
                            callLocation = new LatLng(25.4084,68.2605);
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

        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i("PEEP", "onRequestPermissionsResult: Mili wai");
                    setCallerNumber();
                }
                break;
            default:
                break;
        }

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
        call(edhiNumber);
    }

    public void callFireBrigade(View view) {
        call(fireBrigadeNumber);
    }


    //Code to call on specific number
    public void call(String number) {
        //another piece of code
        Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
        callIntent.setData(Uri.parse("tel:"+number));    //this is the phone number calling



        //CHECK PERMISSION TO READ PHONE NUM

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS}, REQUEST_READ_PHONE_STATE);
            setCallerNumber();
        } else {

            setCallerNumber();
        }

        //END

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
                Log.i("PEEP", String.format(
                        "%s %s %s %s",
                        String.valueOf(latitude), String.valueOf(longitude), callerNumber, "date"
                ));

                Time dtNow = new Time();
                dtNow.setToNow();
                int hours = dtNow.hour;
                String dateTime = dtNow.format("%Y.%m.%d %H:%M");
                String lsYMD = dtNow.toString();
                String distance = "0 KM";

                Log.i("PEEP", dateTime);
                if(latLng!= null && callLocation!=null) {
                    distance = CalculationByDistance(latLng, callLocation);
                }
                boolean isInserted = myDb.addCallRecord(latitude, longitude, callerNumber,  dateTime, distance);
                Log.i("PEEP", "call:+ "+distance);


                if(isInserted) {

                    Log.i("PEEP", "Record added successfully!");


                } else {
                    Toast.makeText(this, "There was problem adding record!", Toast.LENGTH_SHORT).show();
                }

                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"yourActivity not found",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void setCallerNumber() {
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        this.callerNumber = tMgr.getLine1Number();
        Log.i("PEEP", "call: "+callerNumber);
    }
}
