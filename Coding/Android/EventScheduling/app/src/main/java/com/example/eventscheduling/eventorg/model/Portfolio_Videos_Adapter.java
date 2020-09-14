package com.example.eventscheduling.eventorg.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.portfolio_video_values;

import java.util.ArrayList;

public class Portfolio_Videos_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    private ArrayList<portfolio_video_values> mediaObjects;
    private RequestManager requestManager;


    private static final String TAG = "RecyclerView_Adapter";

    public Portfolio_Videos_Adapter(ArrayList<portfolio_video_values> mediaObjects, RequestManager requestManager) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        return new Portfolio_Video_ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evnt_org_portfolio_video_recyc_item,viewGroup,false));

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((Portfolio_Video_ViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);
        }

        @Override
        public int getItemCount() {
            return mediaObjects.size();
        }

    }






