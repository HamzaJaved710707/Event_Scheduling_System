package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;

import java.util.List;


public class Portfolio_Pictures_Adapter extends RecyclerView.Adapter<Portfolio_Pictures_Adapter.ViewHolder>{

   List<String> pictures_values;

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter";

    public Portfolio_Pictures_Adapter(Context mContext, List<String> picValues) {
        this.pictures_values = picValues;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.evnt_org_portfolio_picture_recyc_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is called");
        String current = pictures_values.get(position);
        Glide.with(mContext).load(current).into(holder.image);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: in onbindview holder");
                Toast.makeText(mContext, "Toast", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: is called");
        return pictures_values.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
            image = itemView.findViewById(R.id.pic_imgView);
            layout = itemView.findViewById(R.id.pic_recycler_id);

        }

    }
}
