package com.example.inspirationrewards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterRewardHistory extends RecyclerView.Adapter<AdapterRewardHistory.ViewHolder> {
    private List<RewardHistory> rewardHistories;
    private YourProfile yourProfile;
   // private Context context;

    public AdapterRewardHistory( YourProfile yourProfile,List<RewardHistory> rewardHistories) {
        this.yourProfile = yourProfile;
        this.rewardHistories = rewardHistories;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_history,parent,false);
        return new AdapterRewardHistory.ViewHolder(v1);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RewardHistory rh=rewardHistories.get(position);
        holder.a.setText(rh.getSdate());
        holder.b.setText(rh.getName());
        holder.c.setText(""+rh.getPointsrr());
        holder.d.setText(rh.getComments());


    }

    @Override
    public int getItemCount() {
        return rewardHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView a,b,c,d;

        public ViewHolder(View itemView) {
            super(itemView);
            a=itemView.findViewById(R.id.date);
            b=itemView.findViewById(R.id.NameRH);
            c=itemView.findViewById(R.id.pointsRH);
            d=itemView.findViewById(R.id.commentsRH);
        }
    }
}
