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
import com.example.eventscheduling.client.util.client_friendList_values;
import com.example.eventscheduling.client.util.client_package_values;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Packages;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class client_package_adapter extends RecyclerView.Adapter<client_package_adapter.ViewHolder> {
    private Context mContext;
    List<client_package_values>  packages_values = new ArrayList<>();

    private client_package_adapter.onItemClickListner OnclickListener;

    public client_package_adapter(Context context, List<client_package_values> list) {

        mContext = context;
        packages_values = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_package_frag_recycler_item, parent, false);
        return new client_package_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(packages_values.get(position).getImageAdr()).into(holder.imageAdr);
        holder.packageName.setText(packages_values.get(position).getPackageName());
        holder.Price.setText(packages_values.get(position).getPrice());
        holder.businessName.setText(packages_values.get(position).getBusinessName());
        holder.detailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnclickListener.onDetailButtonClick();
            }
        });
    }

    @Override
    public int getItemCount() {
     return packages_values.size();
    }


    // ViewHolder class implementation
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageAdr;
        private TextView packageName;
        private TextView Price;
        private TextView businessName;
        private RatingBar ratingBar;
       private TextView detailTxt;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageAdr = itemView.findViewById(R.id.image_id_client_package);
            packageName = itemView.findViewById(R.id.pakage_name_text_client_package);
            Price = itemView.findViewById(R.id.price_text_client_package);
            businessName = itemView.findViewById(R.id.location_text_client_package);
            ratingBar = itemView.findViewById(R.id.rating_bar_client_package);
            detailTxt = itemView.findViewById(R.id.view_detail_about_package_client_package);
        }

    }
    public interface onItemClickListner{
        void onDetailButtonClick();
    }

    public void setOnClick(client_package_adapter.onItemClickListner clickListner){
        this.OnclickListener = clickListner;
    }
}
