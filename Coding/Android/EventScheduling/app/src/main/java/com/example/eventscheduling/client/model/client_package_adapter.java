package com.example.eventscheduling.client.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.client_package_values;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class client_package_adapter extends FirestoreRecyclerAdapter<client_package_values, client_package_adapter.ViewHolder> {
    private Context mContext;


    public client_package_adapter(@NonNull FirestoreRecyclerOptions<client_package_values> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull client_package_adapter.ViewHolder holder, int position, @NonNull client_package_values model) {
        Glide.with(mContext).load(model.getImageAdr()).into(holder.imageAdr);
        holder.packageName.setText(model.getPackageName());
        holder.Price.setText(model.getPrice());
        holder.venue.setText(model.getVenue());
        holder.ratingBar.setNumStars(model.getRating());
    }

    @NonNull
    @Override
    public client_package_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_package_frag_recycler_item,parent,false);
       return new ViewHolder(view);
    }

    // ViewHolder class implementation
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageAdr;
        private TextView packageName;
        private TextView Price;
        private TextView venue;
        private RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageAdr = itemView.findViewById(R.id.image_id_client_package);
            packageName = itemView.findViewById(R.id.pakage_name_text_client_package);
            Price = itemView.findViewById(R.id.price_text_client_package);
            venue = itemView.findViewById(R.id.location_text_client_package);
            ratingBar = itemView.findViewById(R.id.rating_bar_client_package);
        }

    }
}
