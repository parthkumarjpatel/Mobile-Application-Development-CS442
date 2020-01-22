package com.example.inspirationrewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.parseColor;

public class AwardActivity extends AppCompatActivity {
    private EditText editText, send_points;
    private InspirationLeaderboard inspirationLeaderboard;
    private TextView lName, fName, points, department, position, story;
    private TextView charCountText;
    public static int MAX_CHARS = 80;
    private ImageView img;
    private String[] tr;
    private String stud_id, user_m, pass_m,ln,fn,p,d,s;
    int blank;
    private String cc;


    public String v, w, x, y, z, img1;
    public int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
       // setTitle("FN+LN");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

      //  Intent i = getIntent();

        Log.d(TAG, "onCreate: Elements json se coming....." + tr);

        stud_id = getIntent().getStringExtra("studentId");
        user_m = getIntent().getStringExtra("username");;
        Log.d("aa22", "onCreate: "+user_m);
        pass_m = getIntent().getStringExtra("password");
        cc=getIntent().getStringExtra("json");
        send_points = (EditText) findViewById(R.id.editText);
        img = (ImageView) findViewById(R.id.profilePhotoAA);
//        cc = tr[3];
        editText = findViewById(R.id.commentsAA);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        charCountText = findViewById(R.id.charCountAA);
        addTextListener();

        lName = (TextView) findViewById(R.id.lastNameAA);
        fName = (TextView) findViewById(R.id.firstNameAA);
        points = (TextView) findViewById(R.id.awardsAA);
        department = (TextView) findViewById(R.id.departmentAA);
        position = (TextView) findViewById(R.id.positionAA);
        story = (TextView) findViewById(R.id.storyAA);



        try{
            JSONObject jsonObject=new JSONObject(getIntent().getStringExtra("json"));
            ln=jsonObject.getString("lastName");
            lName.setText(ln + ", ");
            fn=jsonObject.getString("firstName");
            fName.setText(fn);
            setTitle(ln+" "+ fn);
            p=jsonObject.getString("position");
            position.setText(p);
            d=jsonObject.getString("department");
            department.setText(d);
            s=jsonObject.getString("story");
            story.setText(s);
            img1=jsonObject.getString("imageBytes");
            Log.d("Image checking", "onCreate: "+ img1);


            if(img1!=null){
                byte[] imageBytes = Base64.decode(img1,Base64.DEFAULT);
                Log.d("edit profile","image updated");
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Log.d("TA", "" + bitmap);
                img.setImageBitmap(bitmap);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
        }
        display(cc);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Log.d(TAG, "saveRewards-----------00");
            String pts = send_points.getText().toString();
            String cts = editText.getText().toString();
            Intent i = new Intent(AwardActivity.this, InspirationLeaderboard.class);
            try {
                JSONObject js = new JSONObject(cc);
                String dest_studId = js.getString("studentId");
                String dest_user = js.getString("username");
                String dest_name = js.getString("firstName") + " " + js.getString("lastName");
                Log.d(TAG, "DESTINATION"+dest_name);
                SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy");
                String dest_date = s.format(new Date());
                new RewardsAPI(AwardActivity.this).execute(dest_studId, dest_user, dest_name, dest_date, pts, cts, stud_id, user_m, pass_m);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onOptionsItemSelected: adding awards");
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                }
            });
            builder.setTitle("Add Reward Points ?");
            builder.setIcon(R.drawable.logo);
            builder.setMessage("Add Reward Points for "+ln+" "+fn+"?");
            AlertDialog dialog1 = builder.create();
            dialog1.show();
            Button bt1 = dialog1.getButton(DialogInterface.BUTTON_POSITIVE);
            bt1.setTextColor(parseColor("#008577"));
            Button bt2 = dialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
            bt2.setTextColor(parseColor("#008577"));

        }

        return super.onOptionsItemSelected(item);
    }

    public void sendResults(String s, String response) {

        if(s=="FAILED")
        {
            Toast.makeText(this, "Insufficient Reward Points", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d(TAG,"GET PROFILE BACK");
            String []sm = new String[3];
            sm[0] =  stud_id;
            sm[1] =  user_m;
            sm[2] = pass_m;
            Intent intent = new Intent(this, InspirationLeaderboard.class);
            intent.putExtra("values",sm);
            LayoutInflater inflater = getLayoutInflater();

            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.user_ct));
            Toast toast = new Toast(getApplicationContext());
            TextView text=(TextView)layout.findViewById(R.id.message);

            toast.setGravity(Gravity.BOTTOM, 10, 50);
            toast.setDuration(Toast.LENGTH_LONG);
            text.setText("Add reward successfully");
            toast.setView(layout);
            toast.show();
            startActivity(intent);
        }
    }

    public void display(String rd) {

        try {
            JSONObject j = new JSONObject(rd);
            setTitle(j.getString("firstName") + " " + j.getString("lastName"));
            lName.setText(j.getString("lastName") + ", ");
            fName.setText(j.getString("firstName"));
            position.setText(j.getString("position"));
            department.setText(j.getString("department"));
            Log.d(TAG, "rewards---------" + j.getString("rewards"));
            story.setText(j.getString("story"));
            String send_p = j.getString("rewards");
            if (j.getString("imageBytes") != "null" || !j.getString("imageBytes").isEmpty()) {
                byte[] imageBytes = Base64.decode(j.getString("imageBytes"), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                img.setImageBitmap(bitmap);
            }
            if (send_p != "null") {
                setPoints(send_p);
            } else {
                points.setText("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPoints(String r)
    {
        try{
            Log.d(TAG,"to set points"+r);
            if(r != null && !r.isEmpty())
            {
                Log.d(TAG,"rewards######"+r);
                JSONArray jarray = new JSONArray(r);
                if(jarray.length()>0 && jarray!=null)
                {
                    int sum = 0;
                    for(int i=0; i<jarray.length(); i++)
                    {
                        JSONObject n = jarray.getJSONObject(i);
                        String value = n.getString("value");
                        sum = sum + Integer.parseInt(value);
                    }
                    points.setText(Integer.toString(sum));
                }
                else {
                    points.setText("0");
                }
            }
            else {
                points.setText("0");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    public void saveRewards()
//    {
//        Log.d(TAG,"saveRewards-----------00");
//        String points = points_to_award.getText().toString();
//        String comments = comment.getText().toString();
//        try{
//            JSONObject js = new JSONObject(output_json);
//            String target_id = js.getString("studentId");
//            String target_username = js.getString("username");
//            String target_name = js.getString("firstName") + " " + js.getString("lastName");
//            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//            String target_currentDateandTime = sdf.format(new Date());
//            new AddRewardsAPICall(RewardProfile.this).execute(target_id, target_username, target_name, target_currentDateandTime, points, comments, sId, username, password);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }

}
