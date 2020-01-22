package com.example.inspirationrewards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AdapterLeaderboard extends RecyclerView.Adapter<AdapterLeaderboard.ViewHolder> {

    private List<ListItem> listRV;
    private InspirationLeaderboard context;
    private String username = null;




    public AdapterLeaderboard(InspirationLeaderboard is1,List<ListItem> listRV, String username) {

        this.context = is1;
        this.listRV = listRV;
        this.username=username;
    }
    public AdapterLeaderboard()
    {

    }


    public void Refreshadapter(List<ListItem> list)
    {
        this.listRV = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        v1.setOnClickListener(context);
        return new ViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       //String u1=m.
       // Log.d("adapter", "value of u1 "+u1);
        ListItem l=listRV.get(position);
        holder.a.setText(l.getLnRV());
        holder.b.setText(l.getFnRV());
        holder.c.setText(""+l.getPonitsAwardedRV());
        holder.d.setText(l.getPositionRV());
        holder.e.setText(l.getDepartmentRV());
        holder.f.setImageBitmap(getBitmap(l.getImage()));
        String u2=l.getUsr();
        Log.d("Adapter", "value of u2"+u2);
        if(u2.equals(username)){
            holder.a.setTextColor(Color.parseColor("#008577"));
           holder.b.setTextColor(Color.parseColor("#008577"));
           holder.c.setTextColor(Color.parseColor("#008577"));
           holder.d.setTextColor(Color.parseColor("#008577"));
           holder.e.setTextColor(Color.parseColor("#008577"));

       }

    }

    @Override
    public int getItemCount() {
        return listRV.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView a,b,c,d,e;
        public ImageView f;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            a=(TextView)itemView.findViewById(R.id.lNameRV);
            b=(TextView)itemView.findViewById(R.id.fNameRV);
            c=(TextView)itemView.findViewById(R.id.pointsRV);
            d=(TextView)itemView.findViewById(R.id.posRV);
            e=(TextView)itemView.findViewById(R.id.deptRV);
            f=(ImageView)itemView.findViewById(R.id.photoRV);



        }

    }
    public Bitmap getBitmap(String img1) {
        byte[] imageBytes = Base64.decode(img1, Base64.DEFAULT);
        Log.d("conv", "doConvert: Image byte array length: " + img1.length());

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Log.d("conv", ""+bitmap);

        return bitmap;
}

}
