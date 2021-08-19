package com.example.eventscheduling.eventorg.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;

public class Portfolio_Video_ViewHolder extends RecyclerView.ViewHolder {
    public  ImageView videoThumbnail;
    public RelativeLayout layout;
    public Portfolio_Video_ViewHolder(@NonNull View itemView) {
        super(itemView);
        videoThumbnail = itemView.findViewById(R.id.evntOrg_portfolio_video_item_video);
        layout = itemView.findViewById(R.id.evntOrg_portfolio_video_item_layout);

    }
}