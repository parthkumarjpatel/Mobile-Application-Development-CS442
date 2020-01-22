package com.example.inspirationrewards;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.graphics.Color.parseColor;

public class EditProfile extends AppCompatActivity {
    EditText edit1,pass1,fName1,lName1,dept1,pos1;
    public String convertedImage;
    private String admin;
    String  img1;
    CheckBox cb1;
    TextView user1;
    ImageView mm;
    String loc;
    private static int MY_LOCATION_REQUEST_CODE = 329;
    private String rewards;
    private Criteria criteria;
    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_CAMERA =100;
   // EditText editText1;
    TextView charCountText1;
    private LocationManager locationManager;
    private Location currentLocation;
    //private Criteria criteria;

    String passimg;

    public static int MAX_CHARS = 360;

    int points1;
    private File currentImageFile;
     ImageView dummy1;
     ImageView addImage1;
    AlertDialog.Builder dialog;
    String loct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         edit1=(EditText)findViewById(R.id.content2);
         user1=(TextView) findViewById(R.id.uname2);
         pass1=(EditText) findViewById(R.id.psswd2);
         fName1=(EditText)findViewById(R.id.fName2);
         lName1=(EditText)findViewById(R.id.lName2);
         dept1=(EditText)findViewById(R.id.dep_enter2);
         pos1= (EditText)findViewById(R.id.pos_enter2);
         cb1=(CheckBox)findViewById(R.id.checkBox22);
        charCountText1=(TextView)findViewById(R.id.charCount2);
        addTextListener();
        addImage1 = (ImageView) findViewById(R.id.imageView42);
        dummy1 =(ImageView) findViewById(R.id.person2);
        dialog=new AlertDialog.Builder(EditProfile.this);
        edit1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        String a = getIntent().getStringExtra("username");
        Log.d("what1", "onCreate: "+a);
        String a1=getIntent().getStringExtra("password");
        String c= getIntent().getStringExtra("lastName");
        String b=getIntent().getStringExtra("firstName");
        //String c= getIntent().getStringExtra("lastName");
        loct=getIntent().getStringExtra("location");
        points1=getIntent().getIntExtra("pointsToAward",0);
        String d=getIntent().getStringExtra("position");
        String e=getIntent().getStringExtra("department");
        String f = getIntent().getStringExtra("story");
        convertedImage=getIntent().getStringExtra("imageBytes");
        rewards = getIntent().getStringExtra("rewards");
        passimg = img1;
        Boolean i=getIntent().getBooleanExtra("admin",false);
        if(i.equals("false")){
            cb1.setChecked(false);
        }else{
            cb1.setChecked(true);
        }
        user1.setText(""+a);
        pass1.setText(""+a1);
        fName1.setText(""+b);
        lName1.setText(""+c);
        pos1.setText(""+d);
        dept1.setText(""+e);
        edit1.setText(""+f);


        mm=(ImageView)findViewById(R.id.person2);
        if(convertedImage!=null){
            byte[] imageBytes = Base64.decode(convertedImage,Base64.DEFAULT);
            Log.d("edit profile","image updated"+convertedImage.length());
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Log.d("TA", "" + bitmap);
            mm.setImageBitmap(bitmap);
        }


        addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PICK_FROM_GALLERY);
                    }
                    if (ActivityCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                        builder.setTitle("Profile Picture");
                        builder.setIcon(R.drawable.logo);
                        builder.setMessage("Take Picture from: ");
                        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doCamera();
                            }
                        });
                        builder.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doGallery();
                            }
                        });
                        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        });
                        AlertDialog a = builder.create();
                        a.show();
                        Button bt1 = a.getButton(DialogInterface.BUTTON_POSITIVE);
                        bt1.setTextColor(parseColor("#008577"));
                        Button bt2 = a.getButton(DialogInterface.BUTTON_NEGATIVE);
                        bt2.setTextColor(parseColor("#008577"));
                        Button bt3 = a.getButton(DialogInterface.BUTTON_NEUTRAL);
                        bt3.setTextColor(Color.RED);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
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





    }
//    public void onRequestPermissionsResult(int requestCode, @NonNull
//            String[] permissions, @NonNull int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
//                    grantResults[0] == PERMISSION_GRANTED) {
//                setLocation();
//                return;
//            }
//        }
//        Log.d(TAG, "onRequestPermissionsResult: No Permission");
//    }

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

    private void addTextListener() {
        edit1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing to do here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int len = s.toString().length();
                String countText = "(" + len + " of " + MAX_CHARS + ")";
                charCountText1.setText(countText);
            }
        });
    }



    public void doCamera() {
        currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    public void doGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                processGallery(data);
                doConvert();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                processCamera();
                doConvert();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }



    private void processCamera() {
        Uri selectedImage = Uri.fromFile(currentImageFile);
        dummy1.setImageURI(selectedImage);
        Bitmap bm = ((BitmapDrawable)dummy1.getDrawable()).getBitmap();
        makeCustomToast(this,
                String.format(Locale.getDefault(), "%,d", bm.getByteCount()),
                Toast.LENGTH_LONG);

        currentImageFile.delete();
    }

    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        dummy1.setImageBitmap(selectedImage);
        makeCustomToast(this,
                String.format(Locale.getDefault(), "%,d", selectedImage.getByteCount()),
                Toast.LENGTH_LONG);

    }
    public static void makeCustomToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, "Image Size: " + message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(100, 50, 100, 50);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    Toast.makeText(getApplicationContext(), "Gallery permissions denied", Toast.LENGTH_SHORT).show();
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case PICK_FROM_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
                }else {
                    Toast.makeText(getApplicationContext(), "Camera permissions denied", Toast.LENGTH_SHORT).show();
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
               break;
        }
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                return;
            }
        }

    }

    public void doConvert() {
        if (dummy1.getDrawable() == null)
            return;

        Bitmap origBitmap = ((BitmapDrawable) dummy1.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 75, bitmapAsByteArrayStream);
        makeCustomToast(this,
                String.format(Locale.getDefault(), "%,d", bitmapAsByteArrayStream.size()),
                Toast.LENGTH_LONG);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d("conv", "doConvert: Image in Base64 size: " + imgString.length());
        convertedImage=imgString;
        Log.d("hello1", " "+imgString);
    }

    public void toast(){
        Toast.makeText(this, "Profile Updated Successfully",
                Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id  = item.getItemId();
        final String user = ((TextView)findViewById(R.id.uname2)).getText().toString();
        final String password = ((EditText)findViewById(R.id.psswd2)).getText().toString();
        final String firstName = ((EditText)findViewById(R.id.fName2)).getText().toString();
      final  String lastName= ((EditText)findViewById(R.id.lName2)).getText().toString();
        final String dept = ((EditText)findViewById(R.id.dep_enter2)).getText().toString();
        final String position = ((EditText)findViewById(R.id.pos_enter2)).getText().toString();
        final String story = ((EditText)findViewById(R.id.content2)).getText().toString();
         admin = "false";
        boolean admin_check = ((CheckBox)findViewById(R.id.checkBox22)).isChecked();
        if(admin_check){
            admin = "true";
        }else{
            admin="false";
        }

        if(id == R.id.saved){
            Log.d("Saving data......", "onOptionsItemSelected: Saved" +firstName + " " + lastName);
            final EditProfile ep=this;

             AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    new UpdateProfileAsync(ep).execute("A20416508",user, password, firstName, lastName,points1+"" ,dept,story,position,admin, loct, convertedImage);

                    /*Intent i = new Intent( EditProfile.this, YourProfile.class);
                    //String l= getPlace(currentLocation);

                    i.putExtra("lastName",lastName);
                    Log.d(TAG, "LN"+lastName);
                    i.putExtra("firstName",firstName);
                    Log.d(TAG, "lastName"+firstName);
                    i.putExtra("username",user);
                    Log.d(TAG, "USER KA NAAAM "+user);
                    i.putExtra("location",loct);
                    Log.d(TAG, "LOCT"+loct);
                    i.putExtra("password",password);
                    Log.d(TAG, "password"+password);
                    i.putExtra("pointsToAward",points1);
                    i.putExtra("department",dept);
                    i.putExtra("position",position);
                    i.putExtra("story",story);
                    i.putExtra("imageBytes",convertedImage);
                    i.putExtra("reward",rewards);
                    Log.d("parthiiiiiii", "onClick: "+convertedImage);
                    startActivity(i);
                    Log.d(TAG,"saved!!!!");
                   //  finish();
                    Log.d(TAG,"Finish working!!!!");
                    //Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();

                    LayoutInflater inflater = getLayoutInflater();

                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.user_ct));
                    Toast toast = new Toast(getApplicationContext());
                    TextView text=(TextView)layout.findViewById(R.id.message);

                    toast.setGravity(Gravity.BOTTOM, 10, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    text.setText("User updated successfully");
                    toast.setView(layout);
                    toast.show();*/
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                }
            });
            builder.setTitle("Save Changes ?");
            builder.setIcon(R.drawable.logo);
            AlertDialog dialog1 = builder.create();
            dialog1.show();

            Button bt1 = dialog1.getButton(DialogInterface.BUTTON_POSITIVE);
            bt1.setTextColor(parseColor("#008577"));
            Button bt2 = dialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
            bt2.setTextColor(parseColor("#008577"));


        }
        if(id==android.R.id.home)
        {
            onBackPressed();
            Log.d(TAG, "Back Working!!! ");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendResults(String s, String sId, String username, String password) {

        Log.d(TAG, "Results sent!!!!");
        if(s=="FAILED")
        {
            Toast.makeText(this, "Failed in updation.", Toast.LENGTH_SHORT).show();
        }
        else{
            new LoginAPI(EditProfile.this).execute(sId, username, password, "2");

        }
    }

    public void result(String s)
    {
        if(s=="FAILED")
        {
            Toast.makeText(this, "Failed in user updation.", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, YourProfile.class);
            Log.d(TAG,"result in edit----------"+s);
            intent.putExtra("json",s);
            startActivity(intent);
            Toast toast = Toast.makeText(this, "User Update Successful", Toast.LENGTH_LONG);
            View toastView = toast.getView(); // This'll return the default View of the Toast.

            /* And now you can get the TextView of the default View of the Toast. */
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(25);
            toastMessage.setTextColor(Color.WHITE);
            toastMessage.setGravity(Gravity.CENTER);
            toastMessage.setCompoundDrawablePadding(16);
            toastView.setBackgroundColor(Color.rgb(0,52,47));
            toast.show();
        }
    }

}