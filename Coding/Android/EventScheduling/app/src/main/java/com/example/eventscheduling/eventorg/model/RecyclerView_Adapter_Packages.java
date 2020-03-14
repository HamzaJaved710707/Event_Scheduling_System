package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.PackagesValues;


import java.util.ArrayList;

public class RecyclerView_Adapter_Packages extends RecyclerView.Adapter<RecyclerView_Adapter_Packages.ViewHolder>  {
    ArrayList<PackagesValues> packagesValues = new ArrayList<>();

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter_P";

    public RecyclerView_Adapter_Packages(Context mContext, ArrayList<PackagesValues> msgValue) {
        this.packagesValues = msgValue;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView_Adapter_Packages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evnt_org_package_recycler_item,parent,false);
        RecyclerView_Adapter_Packages.ViewHolder holder = new RecyclerView_Adapter_Packages.ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_Adapter_Packages.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is called");
        PackagesValues current = packagesValues.get(position);
        holder.image.setImageResource(current.getImageResource());
        holder.packageName.setText(current.getPackageName());
        holder.price.setText(current.getPackagePrice());
        holder.location.setText(current.getLocation());
        holder.rating.setRating(current.getRating());
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
        return packagesValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView packageName;
        TextView location;
        TextView price;
        RatingBar rating;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
            image = itemView.findViewById(R.id.image_id);
            packageName = itemView.findViewById(R.id.pakage_name_text);
            location = itemView.findViewById(R.id.location_text);
            price = itemView.findViewById(R.id.price_text);
            rating = itemView.findViewById(R.id.rating_bar);
            layout = itemView.findViewById(R.id.recycler_layout);

        }

    }
}
