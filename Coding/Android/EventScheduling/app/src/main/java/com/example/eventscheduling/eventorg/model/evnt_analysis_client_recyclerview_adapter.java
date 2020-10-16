package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.evnt_analysis_client_recyc;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class evnt_analysis_client_recyclerview_adapter extends RecyclerView.Adapter<evnt_analysis_client_recyclerview_adapter.evnt_analysis_ViewHolder> {
    private static final String TAG = "evnt_analysis_client";
    private Context mContext;
    private List<evnt_analysis_client_recyc> recycler_values = new ArrayList<>();

    // Constructor
    public evnt_analysis_client_recyclerview_adapter(Context context, List<evnt_analysis_client_recyc> list) {
        mContext = context;
        recycler_values = list;

    }

    @NonNull
    @Override
    public evnt_analysis_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.evntorg_analysis_client_item, parent, false);
        evnt_analysis_client_recyclerview_adapter.evnt_analysis_ViewHolder holder = new evnt_analysis_client_recyclerview_adapter.evnt_analysis_ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull evnt_analysis_ViewHolder holder, int position) {
        holder.name.setText(recycler_values.get(position).getName());
        Glide.with(mContext).load(recycler_values.get(position).getImgUrl()).into(holder.img);
        holder.count.setText(String.valueOf(recycler_values.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        if (recycler_values.size() != 0) {
            return recycler_values.size();
        }
        return 0;
    }

    public class evnt_analysis_ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView name;
        CircleImageView img;
        MaterialTextView count;

        public evnt_analysis_ViewHolder(View viewItem) {
            super(viewItem);
            name = viewItem.findViewById(R.id.recyclerView_evnt_analysis_name);
            img = viewItem.findViewById(R.id.image_evnt_analysis_client);
            count = viewItem.findViewById(R.id.evnt_analysis_count);
        }
    }
}
