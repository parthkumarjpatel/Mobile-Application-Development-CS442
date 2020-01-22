package com.example.inspirationrewards;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import java.io.InputStream;
import java.util.Locale;
import static android.content.ContentValues.TAG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.parseColor;

public class ProfileActivity extends AppCompatActivity {
    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_CAMERA =100;


    private EditText editText;
    private TextView charCountText;
   // String l;
    public String image;

    public static int MAX_CHARS = 360;


    private File currentImageFile;
    ImageView dummy;
    ImageView addImage;
     AlertDialog.Builder dialog;
    private String loct;
    private String admin;
    private String imageString,rewardRecords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  loct=i.getStringExtra("location");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Create Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setLogo(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addImage = (ImageView)findViewById(R.id.imageView4);
        dummy = (ImageView) findViewById(R.id.person);
        Log.d(TAG,"printing.......");
        loct = getIntent().getStringExtra("location");
        Log.d(TAG,"printing.......2");
        Log.d("location"," "+loct);
        editText = findViewById(R.id.content);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        charCountText = findViewById(R.id.charCount);
        addTextListener();


       addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PICK_FROM_GALLERY);
                    }

                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setTitle("Profile Picture");
                        builder.setIcon(R.drawable.logo);
                        builder.setMessage("Take Picture from: ");
                                builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        doCamera();
                                        addImage.setImageResource(android.R.color.transparent);
                                    }
                                });
                                builder.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doGallery();
                                addImage.setImageResource(android.R.color.transparent);
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




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case PICK_FROM_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
                }

        }

    }


    private void addTextListener() {
        editText.addTextChangedListener(new TextWatcher() {

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
                charCountText.setText(countText);
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
        dummy.setImageURI(selectedImage);
        Bitmap bm = ((BitmapDrawable) dummy.getDrawable()).getBitmap();
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
        Bitmap select = getResizedBitmap(selectedImage,100);
        dummy.setImageBitmap(select);
        makeCustomToast(this,
                String.format(Locale.getDefault(), "%,d", selectedImage.getByteCount()),
                Toast.LENGTH_LONG);

    }
    public void doConvert() {
        if (dummy.getDrawable() == null)
            return;

//        int jpgQuality = ((SeekBar) findViewById(R.id.seekBar)).getProgress();


        Bitmap origBitmap = ((BitmapDrawable) dummy.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapAsByteArrayStream);
        makeCustomToast(this,
                String.format(Locale.getDefault(), "%,d", bitmapAsByteArrayStream.size()),
                Toast.LENGTH_LONG);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d("conv", "doConvert: Image in Base64 size: " + imgString.length());
        image=imgString;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id  = item.getItemId();
        final String user = ((EditText)findViewById(R.id.uname)).getText().toString();
        final String password = ((EditText)findViewById(R.id.psswd)).getText().toString();
        final String firstName = ((EditText)findViewById(R.id.fName)).getText().toString();
        final String lastName= ((EditText)findViewById(R.id.lName)).getText().toString();
        final String dept = ((EditText)findViewById(R.id.dep_enter)).getText().toString();
        final String position = ((EditText)findViewById(R.id.pos_enter)).getText().toString();
        final String story = ((EditText)findViewById(R.id.content)).getText().toString();
        admin = "false";
        boolean admin_check = ((CheckBox)findViewById(R.id.checkBox2)).isChecked();

         // String image = "";
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)dummy.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        image = imageString;

         rewardRecords = "[]";

        if(admin_check){
            admin = "true";
        }else{
            admin="false";
        }

        if(id == R.id.saved) {
            if (user.equals("")|| password.equals("")||firstName.equals("")||lastName.equals("")||dept.equals("")||position.equals("")||story.equals("")) {
                Toast t = Toast.makeText(getApplicationContext(), "Please enter all the fields", Toast.LENGTH_LONG);
                t.show();
            } else {


                Log.d("Saving data......", "onOptionsItemSelected: Saved" + user + " " + lastName);
                //  if((user || password||firstName||lastName||dept||position||story||image))

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final ProfileActivity pa = this;
                //  new CreatingProfile(this).execute("A20416508",user, password, firstName, lastName, "1000",dept,position,story,admin, loct, image, rewardRecords);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new CreatingProfile(pa).execute("A20416508", user, password, firstName, lastName, "1000", dept, position, story, admin, loct, image, rewardRecords);


                        Intent i = new Intent(ProfileActivity.this, YourProfile.class);
                        //String l= getPlace(currentLocation);

                        i.putExtra("lastName", lastName);
                        i.putExtra("firstName", firstName);
                        i.putExtra("username", user);
                        i.putExtra("password", password);
                        i.putExtra("location", loct);
                        i.putExtra("pointsToAward", 1000);
                        i.putExtra("department", dept);
                        i.putExtra("position", position);
                        i.putExtra("story", story);
                        Log.d(TAG, "story printing.... " + story);
                        i.putExtra("imageBytes", image);

                        startActivity(i);
                        Log.d(TAG, "saved!!!!");
                        String message = "";
                        //Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
                        LayoutInflater inflater = getLayoutInflater();

                        View layout = inflater.inflate(R.layout.custom_toast,
                                (ViewGroup) findViewById(R.id.user_ct));
                        Toast toast = new Toast(getApplicationContext());
                        TextView text=(TextView)layout.findViewById(R.id.message);

                        toast.setGravity(Gravity.BOTTOM, 10, 50);
                        toast.setDuration(Toast.LENGTH_LONG);
                        text.setText("User created successfully");
                        toast.setView(layout);
                        toast.show();

// set a message
//                    TextView text = (TextView) layout.findViewById(R.id.text);
//                    text.setText("User created Successfully!");
//                    text.setTextColor(WHITE);
//
//
//// Toast...
//                    Toast toast = new Toast(getApplicationContext());
//                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.setView(layout);
//                    toast.show();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendResults(String s, String response) {

        Log.d(TAG, "sendResults: "+response);
    }

}
