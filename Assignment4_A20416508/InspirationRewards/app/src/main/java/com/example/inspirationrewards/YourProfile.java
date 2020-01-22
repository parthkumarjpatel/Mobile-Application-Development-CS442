package com.example.inspirationrewards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class YourProfile extends AppCompatActivity {
    private static final String TAG="Your Profile";
    private TextView fName, lName, user,points, dep, post, story,loc,awa,rh;
    private RecyclerView r;
    private RecyclerView.Adapter a;
    private String ln2,un2,fn2,loc3,dpt2,stry1,pos3;
    private String answer;
    private String user_sort, pass_sort, id_sort;

    private List<RewardHistory> rewardHistories;
    private String img1;
    private ImageView image;
    public  int result;
    private String rewards;
    private String EDIT_FLAG;
    private boolean isSave=false;
    private int p2a2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);
        setTitle("Your Profile");

        Log.d(TAG, "answer1 "+answer);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent i=getIntent();
        answer = i.getStringExtra("json");
        Log.d(TAG, "answer1 "+answer);
//        if(savedInstanceState!=null)
//        {
            Log.d("your profile","Getting JSON");
//            username = i.getStringExtra("username");
//            firstName = i.getStringExtra("firstName");
//            lastName = i.getStringExtra("lastName");
//            dept = i.getStringExtra("department");
//            Log.d(TAG, "******************* "+dept);
//            pos = i.getStringExtra("position");
//            storyQ = i.getStringExtra("story");
//            imageQ = i.getStringExtra("imageBytes");
//            location =i.getStringExtra("location");
//            pointsQ = i.getStringExtra("pointsToAward");
//            password =i.getStringExtra("password");
//        }
//        else{
//            Log.d("your profile","ttttttttttttt");
//            //Intent intent = getIntent();
//            //answer = i.getStringExtra("json");
//            Log.d("your profile","ttttttttttttt"+answer);
//        }
        lName =(TextView) findViewById(R.id.lastName3);
        fName =(TextView) findViewById(R.id.firstName3);
        user=(TextView) findViewById(R.id.userName3);
        loc=(TextView)findViewById(R.id.location3);
        awa=(TextView) findViewById(R.id.awards3);
        dep=(TextView) findViewById(R.id.department3);
        post=(TextView) findViewById(R.id.position3);
        points=(TextView) findViewById(R.id.pointsToGIve3);
        story =(TextView)findViewById(R.id.textView163);
        rh=(TextView)findViewById(R.id.textView173);
        image = findViewById(R.id.profilePhoto3);

        try{
            user_sort=getIntent().getStringExtra("username");
            pass_sort=getIntent().getStringExtra("password");
            Log.d("parth22", "onCreate: "+pass_sort);
            id_sort=getIntent().getStringExtra("studentId");
            ln2= getIntent().getStringExtra("lastName");
            lName.setText(ln2+",");
            fn2=getIntent().getStringExtra("firstName");
            fName.setText(fn2);
            un2=getIntent().getStringExtra("username");
            user.setText("("+user_sort+")");
            loc3= getIntent().getStringExtra("location");
            loc.setText(loc3);
            result=getIntent().getIntExtra("pointsToAward", 1000);
            points.setText(""+result);
            awa.setText("0");
            dpt2=getIntent().getStringExtra("department");
            dep.setText(dpt2);
            pos3=getIntent().getStringExtra("position");
            post.setText(pos3);
            stry1=getIntent().getStringExtra("story");
            story.setText(stry1);
            rewards=getIntent().getStringExtra("rewards");
           img1=getIntent().getStringExtra("imageBytes");
            Log.d("Image checking", "onCreate: "+ user_sort);
            if(img1!=null){
                byte[] imageBytes = Base64.decode(img1,Base64.DEFAULT);
                Log.d("edit profile","image updated");
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Log.d("TA", "" + bitmap);
                image.setImageBitmap(bitmap);
            }

            r=(RecyclerView)findViewById(R.id.recyclerViewYP);
            r.setHasFixedSize(true);
            r.setLayoutManager(new LinearLayoutManager(this));
            rewardHistories=new ArrayList<>();
            a=new AdapterRewardHistory( this,rewardHistories);
            r.setAdapter(a);
            showRecords(answer);






        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,"IN EXCCEPTION YOUR PROFILE USERNAME "+ getIntent().getStringExtra("username") + " password " + getIntent().getStringExtra("password"));
            //new LoginAPI(this).execute("A20428066",getIntent().getStringExtra("username"), getIntent().getStringExtra("password"));

           // e.printStackTrace();
            // Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editing, menu);
        return true;

           }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("json", answer);
    }

    public void showRecords(String jsonResult)
    {
        try{
            Log.d(TAG,"parthiiiiiiii---######---"+jsonResult);
            JSONObject js = new JSONObject(jsonResult);
            //JSONObject jsonResponse = js.getJSONObject("response");
            Log.d(TAG,"parthiiiiiiii"+js.getString("lastName")+", "+js);
            user_sort = js.getString("username");
            pass_sort = js.getString("password");
            id_sort = js.getString("studentId");
            ln2=js.getString("lastName");
            lName.setText(ln2+",");
            fn2=js.getString("firstName");
            fName.setText(fn2);
            user.setText("("+user_sort+")");
           // points.setText("0");
            dpt2=js.getString("department");
            Log.d(TAG, "DDDDDDDD"+dpt2);
            dep.setText(dpt2);
            result=js.getInt("pointsToAward");
            Log.d(TAG, "RRRRRRRRR"+result);
            points.setText(""+result);
            awa.setText("0");
            pos3=js.getString("position");
            Log.d(TAG, "POSITION of position "+pos3);
            post.setText(pos3);
            stry1=js.getString("story");
            story.setText(stry1);
            loc3=js.getString("location");
            loc.setText(loc3);

            if(js.getString("imageBytes")!="null" || !js.getString("imageBytes").isEmpty())
            {
                byte[] imageBytes = Base64.decode(js.getString("imageBytes"),  Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Log.d(TAG,"image-----------"+bitmap);
                image.setImageBitmap(bitmap);
            }
            doConvert();

            int sum = 0;
            String rewards = js.getString("rewards");
            JSONArray store = new JSONArray();
            Log.d(TAG,"Send Rewards"+rewards);
            rewardHistories.clear();
            if(rewards!="null")
            {
                JSONArray j = new JSONArray(rewards);
                JSONArray ne = new JSONArray();
                if(j.length()>0)
                {

                    for(int k = 0; k < j.length(); k++)
                    {
                        JSONObject ab = j.getJSONObject(k);
                        ab.put("name",ab.getString("name"));
                        ab.put("date", ab.getString("date"));
                        ab.put("value",ab.getString("value"));
                        ab.put("comments",ab.getString("notes"));
                        sum = sum + Integer.parseInt(ab.getString("value"));
                        ne.put(ab);
                    }
                    changedAdapter(ne);
                }
            }
            awa.setText(Integer.toString(sum));
        }catch (Exception e)
        {
            e.printStackTrace();
           // Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
        }

      }


    public void changedAdapter(JSONArray js1)
    {
        try{
            for(int i=0; i<js1.length(); i++)
            {
                JSONObject js = js1.getJSONObject(i);
                RewardHistory re = new RewardHistory();
                re.setSdate(String.valueOf(js.getString("date")));
                re.setName(js.getString("name"));
                re.setPointsrr(js.getInt("value"));
                re.setComments(js.getString("comments"));
                rewardHistories.add(re);
            }
            rh.setText("Reward History " + "("+js1.length()+"):");
            Collections.sort(rewardHistories,new RewardHistory());
            a.notifyDataSetChanged();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setPointsrr(String rewards)
    {
        try{
            Log.d(TAG,"getting rewards"+rewards);
            if(rewards != null && !rewards.isEmpty())
            {
                Log.d(TAG,"setting points rewards"+rewards);
                JSONArray jarray = new JSONArray(rewards);
                if(jarray.length()>0 && jarray!=null)
                {
                    int sum = 0;
                    for(int i=0; i<jarray.length(); i++)
                    {
                        JSONObject n = jarray.getJSONObject(i);
                        String value = n.getString("value");
                        sum = sum + Integer.parseInt(value);
                    }

                }
                else {

                }
            }
            else {

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void doConvert() {
        if (image.getDrawable() == null)
            return;

        Bitmap origBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 75, bitmapAsByteArrayStream);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d("conv", "doConvert: Image in Base64 size: " + imgString.length());
        img1=imgString;
        Log.d("hello1", " "+imgString);
    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK) {
//                ln2= data.getStringExtra("lastName");
//                fn2=data.getStringExtra("firstName");
//                un2=data.getStringExtra("username");
//                loc3= data.getStringExtra("location");
//                result=data.getIntExtra("pointsToAward", 0);
//                dpt2=data.getStringExtra("department");
//                pos3=data.getStringExtra("position");
//                stry1=data.getStringExtra("story");
//                img1=data.getStringExtra("imageBytes");
//
//                lName .setText(ln2+",,");
//                fName.setText(fn2);
//                user.setText("("+un2+")");
//                loc.setText(loc3);
//                awa.setText(""+result);
//                dep.setText(dpt2);
//                post.setText(pos3);
//                points.setText(""+(1000-result));
//                story.setText(stry1);
//            }
//        }
//
//
//    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {

            Intent i= new Intent(YourProfile.this,EditProfile.class);
           // i.putExtra("json",answer);
            i.putExtra("username",user_sort);
            i.putExtra("password",pass_sort);
            i.putExtra("lastName",ln2);
            Log.d(TAG, "LAST"+ln2);
            i.putExtra("firstName",fn2);
            Log.d(TAG, "FIRST"+fn2);
            i.putExtra("location",loc3);
            Log.d(TAG, "onOptionsItemSelected: "+fn2);

            i.putExtra("department",dpt2);
            i.putExtra("position",pos3);
            Log.d(TAG, "uuuuuuuu: "+pos3);
            i.putExtra("story",stry1);

            i.putExtra("imageBytes",img1);
            Log.d("what2", "onOptionsItemSelected: "+img1);
            i.putExtra("pointsToAward",result);
            i.putExtra("rewards",rewards);

            List<RewardHistory> rl = new ArrayList<>();
            //i.putExtra("rewards", (Serializable) rl);
            startActivity(i);


            Log.d("Your Profile: ", "Intent of Your Profile");
        }
        if(id==R.id.sorting){

                String []val = new String[3];
                val[0] = id_sort;
                val[1] = user_sort;
                val[2] = pass_sort;
            Log.d("parth1", "onOptionsItemSelected: "+id_sort+user_sort+pass_sort);
                Intent intent = new Intent(YourProfile.this,InspirationLeaderboard.class);
                intent.putExtra("values",val);
                startActivity(intent);
            Log.d("Your Profile ", "onOptionsItemSelected----sorting running: ");


//            Intent intent = new Intent(YourProfile.this,InspirationLeaderboard.class);
//            intent.putExtra("lastName",ln2);
//            intent.putExtra("firstName",fn2);
//            intent.putExtra("pointsToAward",1000);
//            intent.putExtra("position",pos3);
//            intent.putExtra("department",dpt2);
//                    startActivity(intent);
//            Log.d("Your Profile ", "onOptionsItemSelected----sorting running: ");
        }
     return super.onOptionsItemSelected(item);
    }


}
