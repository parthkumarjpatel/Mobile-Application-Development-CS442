package com.example.inspirationrewards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class InspirationLeaderboard extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    public TextView last,first,points,post, dept;
    public String[]s1;
    private String o,p,q;
    private static String vv;
     String sId, username, password,img1,fn,ln;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_leaderboard);
        setTitle("Inspiration Leaderboard");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView last=(TextView)findViewById(R.id.lNameRV);
        TextView first=(TextView)findViewById(R.id.fNameRV);
        TextView points=(TextView)findViewById(R.id.pointsRV);
        TextView post=(TextView)findViewById(R.id.posRV);
        TextView dept=(TextView)findViewById(R.id.deptRV);

//        v=getIntent().getStringExtra("lastName");
//        w=getIntent().getStringExtra("firstName");
//        x=getIntent().getIntExtra("pointsToAward",0);
//        y=getIntent().getStringExtra("position");
//        z=getIntent().getStringExtra("department");

//        Intent intent = getIntent();
//        y=intent.getStringExtra("studentId");
//        v=intent.getStringExtra("username");
//        w=intent.getStringExtra("password");


        Intent intent = getIntent();
        s1 = intent.getStringArrayExtra("values");
        sId = s1[0];
        username = s1[1];
        password = s1[2];
//        fn=s1[3];
//        ln=s1[4];
        Log.d("parth11", "onCreate: "+s1[1]);
        Log.d(TAG, "CHECK"+(fn+ln));
//        last.setText(v+",");
//        first.setText(w);
//        points.setText(""+x);
//        post.setText(y);
//        dept.setText(z);

        ImageView mm=(ImageView)findViewById(R.id.photoRV);
        if(img1!=null){
            byte[] imageBytes = Base64.decode(img1,Base64.DEFAULT);
            Log.d("edit profile","image updated");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Log.d("Inspiration LeaderBoard", "image working" + bitmap);
            mm.setImageBitmap(bitmap);
        }


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems=new ArrayList<>();
        adapter=new AdapterLeaderboard(this,listItems,username);

        recyclerView.setAdapter(adapter);
        Log.d("parth", "onCreate: "+s1[0]+s1[2]);
        new AllProfiles(this).execute(s1[0],s1[1],s1[2]);
    }
    public void onClick(View view)
    {
        JSONObject store = new JSONObject();
        int pos = recyclerView.getChildLayoutPosition(view);
        ListItem s = listItems.get(pos);
        Log.d(TAG,"Inside onCLick"+s.getFnRV());
        Log.d(TAG, "CURIE-----"+username);
        Log.d(TAG, "CURIE2------"+s.getUsr());
        if(s.getUsr().equals(username)){
            Toast toast=Toast.makeText(getApplicationContext(),"You cannot reward yourself",Toast.LENGTH_SHORT);
           // toast.setMargin(50,50);
            toast.show();
            return;
        }
        try{
            Log.d(TAG,"check result"+vv);
            JSONArray j = new JSONArray(vv);
            for(int i=0; i<j.length(); i++)
            {
                JSONObject obj = j.getJSONObject(i);
                if(obj.getString("username").equals(s.getUsr()))
                {
                    store = obj;
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
//        String []s1 = new String[4];
//        s1[0] = o;
//        s1[1] = p;
//        Log.d("aa22", "onClick: "+p);
//        s1[2] = q;
//        s1[3] = store.toString();
        Intent i = new Intent(InspirationLeaderboard.this,AwardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("studentId",sId);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("json",store.toString());
        startActivity(i);
    }

    public void sendResults(String s) {
        try{
            vv = s;
            Log.d(TAG,"updating##############"+vv);
            JSONArray jsonArray = new JSONArray(s);
            ArrayList<ListItem> li = new ArrayList<>();
            Log.d(TAG,"length of json-----------"+jsonArray.length());
            for(int i=0; i<jsonArray.length(); i++)
            {
//                Log.d(TAG,"JSON working"+listItems.size());
                ListItem l = new ListItem();
                JSONObject j = jsonArray.getJSONObject(i);
                l.setLnRV(j.getString("lastName")+ ",");

                Log.d("parth3", "sendResults: "+j.getString("lastName"));
                l.setFnRV(j.getString("firstName"));
                l.setPositionRV(j.getString("position")+",");
                l.setDepartmentRV(j.getString("department"));
                //l.setPointsAwardedRV(j.getInt("pointsToAward"));
                l.setImage(j.getString("imageBytes"));
                l.setUsr(j.getString("username"));

                int sum = 0;
                if(j.getString("rewards")!="null")
                {
                    JSONArray j1 = new JSONArray(j.getString("rewards"));
                    if(j1.length()>0)
                    {
                        for(int k = 0; k < j1.length(); k++)
                        {
                            JSONObject kj = j1.getJSONObject(k);
                            sum = sum + Integer.parseInt(kj.getString("value"));
                        }
                    }
                    Log.d(TAG,"vlue------------"+sum);
                    l.setPointsAwardedRV(sum);
                }




                li.add(l);

                settingAdapter(li);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
     public void settingAdapter(ArrayList<ListItem> li){


             listItems.clear();

             listItems.addAll(li);
         Collections.sort(listItems,new Compare());
             adapter.notifyDataSetChanged();
             //adapter.Refresh(listItems);


     }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG,"callback---------------------");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
