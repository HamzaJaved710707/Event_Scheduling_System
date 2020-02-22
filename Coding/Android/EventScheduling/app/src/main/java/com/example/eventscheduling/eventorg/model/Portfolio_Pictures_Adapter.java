package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.MessageValues;
import com.example.eventscheduling.eventorg.util.portfolio_pictures_values;

import java.util.ArrayList;


public class Portfolio_Pictures_Adapter extends RecyclerView.Adapter<Portfolio_Pictures_Adapter.ViewHolder>{

    ArrayList<portfolio_pictures_values> pictures_values;

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter";

    public Portfolio_Pictures_Adapter(Context mContext, ArrayList<portfolio_pictures_values> picValues) {
        this.pictures_values = picValues;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.portfolio_pictures_recycler_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is called");
        portfolio_pictures_values current = pictures_values.get(position);
        holder.image.setImageResource(current.getImageResource());
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
