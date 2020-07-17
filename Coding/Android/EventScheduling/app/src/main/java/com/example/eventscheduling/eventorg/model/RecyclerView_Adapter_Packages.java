package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.PackagesValues;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecyclerView_Adapter_Packages extends FirestoreRecyclerAdapter<PackagesValues, RecyclerView_Adapter_Packages.ViewHolder> {
    // Variables
    Context context;
// Constructor
    public RecyclerView_Adapter_Packages(Context mContext, FirestoreRecyclerOptions<PackagesValues> options) {
        super(options);
        context = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView_Adapter_Packages.ViewHolder holder, int position, @NonNull PackagesValues model) {
        Glide.with(context).load(model.getImageAdr()).into(holder.imageAdr);
        holder.packageName.setText(model.getPackageName());
        holder.Price.setText(model.getPrice());
        holder.venue.setText(model.getVenue());
        holder.ratingBar.setNumStars(model.getRating());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evnt_org_package_recycler_item, parent, false);
       return new ViewHolder(view);
    }

    // Viewholder Class
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageAdr;
        private TextView packageName;
        private TextView Price;
        private TextView venue;
        private RatingBar ratingBar;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAdr = itemView.findViewById(R.id.image_id);
            packageName = itemView.findViewById(R.id.pakage_name_text);
            Price = itemView.findViewById(R.id.price_text);
            venue = itemView.findViewById(R.id.location_text);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            constraintLayout = itemView.findViewById(R.id.recycler_constriantLayout);


        }
    }
}
