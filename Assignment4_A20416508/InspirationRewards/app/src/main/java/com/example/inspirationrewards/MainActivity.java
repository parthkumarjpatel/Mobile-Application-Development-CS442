package com.example.inspirationrewards;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private LocationManager locationManager;
    private Location currentLocation;
    public static final String MyPREFERENCES = "MyPrefs";
    private Criteria criteria;
    private ProgressBar progressBar;
    String pswd;

    SharedPreferences shared;
    String loc;
    public String lnM, fnM, usM, lctM, deptM, posM, styM, imM, rr1;
    int resultM;
    private static int MY_LOCATION_REQUEST_CODE = 329;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        TextView link = findViewById(R.id.create);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
//               String l= getPlace(currentLocation);
                i.putExtra("location", loc);
                startActivity(i);
            }

        });

        shared = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String value1 = shared.getString("username", "");
        String value2 = shared.getString("password", "");
        Log.d(TAG, "onCreate: Shared Username is: " + value1);
        Log.d(TAG, "onCreate: Shared Password is: " + value2);
        ((EditText) findViewById(R.id.username)).setText(value1);
        ((EditText) findViewById(R.id.password)).setText(value2);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        //
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE);
        } else {
            setLocation();
        }
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


    }

    public void itemClicked(View v) {
        CheckBox check = (CheckBox) v;
        String uName12 = ((EditText) findViewById(R.id.username)).getText().toString();
        String pswd12 = ((EditText) findViewById(R.id.password)).getText().toString();
        if (check.isChecked()) {
            //create shared preferences
            //sp=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor Ed1 = shared.edit();
            Ed1.putString("username", uName12);
            Ed1.putString("password", pswd12);
            Ed1.commit();
            Log.d(TAG, "itemClicked: Stored in shared preferences");
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                return;
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: No Permission");
    }

    @SuppressLint("MissingPermission")
    private void setLocation() {

        String bestProvider = locationManager.getBestProvider(criteria, true);

        currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {
            loc = getPlace(currentLocation);
            Log.d(TAG, "setLocation: Location Available" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ", " + loc);

        } else {
            Log.d(TAG, "setLocation: Location Unavailable");
        }
    }

    private String getPlace(Location loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            return city + ", " + state;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void onClick(View v) {

        String uName = ((EditText) findViewById(R.id.username)).getText().toString();
        pswd = ((EditText) findViewById(R.id.password)).getText().toString();
        String sId = "A20416508";
        progressBar.setVisibility(View.VISIBLE);

        new LoginAPI(this).execute(sId, uName, pswd, "1");
        Log.d(TAG, "login: Username" + uName);
        Log.d(TAG, "login: Password" + pswd);
       // progressBar.setVisibility(View.INVISIBLE);
    }

    public void setData(String ln2, String fn2, String us2, String lct1, int result11, String dept1, String pos1, String sty1, String im1, String rr) {
        lnM = ln2;
        fnM = fn2;
        usM = us2;
        lctM = lct1;
        resultM = result11;
        deptM = dept1;
        posM = pos1;
        styM = sty1;
        imM = im1;
        rr1 = rr;
        Log.d(TAG,"send rewards-----------"+rr1);
    }

    public void loggedIn() {
        Intent intent = new Intent(MainActivity.this, YourProfile.class);
        intent.putExtra("lastName", lnM);
        intent.putExtra("firstName", fnM);
        intent.putExtra("username", usM);
        intent.putExtra("location", lctM);
        intent.putExtra("pointsToAward", resultM);
        intent.putExtra("department", deptM);
        intent.putExtra("position", posM);
        intent.putExtra("story", styM);
        intent.putExtra("imageBytes", imM);
        intent.putExtra("password", pswd);
        intent.putExtra("studentId", "A20416508");
        intent.putExtra("rewards",rr1);
        startActivity(intent);
    }

    public void makeToast() {
        Toast t = Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_SHORT);
       // t.setMargin(60, 60);
        t.show();
        progressBar.setVisibility(View.INVISIBLE);
        //  finish();
    }

    public void sendResults(String s) {

                Intent intent = new Intent(MainActivity.this, YourProfile.class);
                Log.d("TAG", "JSONOBJECT of Login-******---------" + s);

                intent.putExtra("json", s);
        progressBar.setVisibility(View.INVISIBLE);
                startActivity(intent);
                //displayProfile.showResult(result);

            }
    public void sendResults1(String s) {



        Toast.makeText(this,""+s,Toast.LENGTH_SHORT).show();
        //displayProfile.showResult(result);

    }
        }


