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
    private onItemClickListner OnclickListener;
// Constructor
    public RecyclerView_Adapter_Packages(Context mContext, FirestoreRecyclerOptions<PackagesValues> options) {
        super(options);
        context = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView_Adapter_Packages.ViewHolder holder, int position, @NonNull PackagesValues model) {
        if(model.getImage() == null){
            Glide.with(context).load(R.mipmap.package_icon).into(holder.image);
        }else{
            Glide.with(context).load(model.getImage()).into(holder.image);
        }

        holder.   PackageName.setText(model.getPackageName());
        holder.Price.setText(model.getPrice());
        holder.BusinessName.setText(model.getBusinessName());
        holder.detailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnclickListener.onDetailButtonClick(model.getUserId(), model.getId());
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evnt_org_package_recycler_item, parent, false);
      return new ViewHolder(view);

    }

    // Viewholder Class
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView    PackageName;;
        private TextView Price;
        private TextView BusinessName;
        private TextView detailText;
        private RatingBar ratingBar;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.evnt_order_item_img);
            PackageName = itemView.findViewById(R.id.pakage_name_text);
            Price = itemView.findViewById(R.id.price_text);
            BusinessName = itemView.findViewById(R.id.location_text_evnt_package);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            constraintLayout = itemView.findViewById(R.id.recycler_constriantLayout);
            detailText = itemView.findViewById(R.id.view_detail_about_package);


        }
    }
    public interface onItemClickListner{
        void onDetailButtonClick(String userId , String packageId);
        }

        public void setOnClick(onItemClickListner clickListner){
        this.OnclickListener = clickListner;
        }
}
