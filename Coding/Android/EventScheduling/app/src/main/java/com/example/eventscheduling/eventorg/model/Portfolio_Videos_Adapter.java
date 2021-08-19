package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.portfolio_video_values;

import java.util.ArrayList;

public class Portfolio_Videos_Adapter extends RecyclerView.Adapter<Portfolio_Video_ViewHolder> {

    private Context context;
    private ArrayList<portfolio_video_values> mediaObjects;
    private Bitmap thumb;
    private itemClickInterface itemInterface;

    public Portfolio_Videos_Adapter(Context mContext, ArrayList<portfolio_video_values> objects){

        mediaObjects = objects;
        context = mContext;
    }

    @NonNull
    @Override
    public Portfolio_Video_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.evnt_org_portfolio_video_recyc_item,parent, false);
       return new Portfolio_Video_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Portfolio_Video_ViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(mediaObjects.get(position).getLink()).into(holder.videoThumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInterface.itemClick(mediaObjects.get(position).getLink());
            }
        });
        /*thumb= ThumbnailUtils.createVideoThumbnail(mediaObjects.get(position).toString(), MediaStore.Images.Thumbnails.MICRO_KIND);
        img_tumbnail.setImageBitmap(thumb);*/
    }

    @Override
    public int getItemCount() {
      return mediaObjects.size();
    }
    public interface itemClickInterface{
        void itemClick(String url);
    }
    public void setListener(itemClickInterface listener){
        itemInterface = listener;
    }
}






