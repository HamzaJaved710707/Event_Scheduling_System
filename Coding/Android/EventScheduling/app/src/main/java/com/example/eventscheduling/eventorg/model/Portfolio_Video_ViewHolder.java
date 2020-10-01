package com.example.eventscheduling.eventorg.model;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.portfolio_video_values;

public class Portfolio_Video_ViewHolder extends RecyclerView.ViewHolder {
    public  ImageView videoThumbnail;
    public RelativeLayout layout;
    public Portfolio_Video_ViewHolder(@NonNull View itemView) {
        super(itemView);
        videoThumbnail = itemView.findViewById(R.id.evntOrg_portfolio_video_item_video);
        layout = itemView.findViewById(R.id.evntOrg_portfolio_video_item_layout);

    }
}