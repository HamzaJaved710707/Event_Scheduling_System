package com.example.eventscheduling.client.model;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.client.util.client_msgDetail_values;
import com.example.eventscheduling.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class client_msgDetail_adapter extends FirestoreRecyclerAdapter<client_msgDetail_values, client_msgDetail_adapter.client_msgDetail_holder> {


    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public client_msgDetail_adapter(Context context, @NonNull FirestoreRecyclerOptions<client_msgDetail_values> options) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull client_msgDetail_adapter.client_msgDetail_holder holder, int position, @NonNull client_msgDetail_values model) {
        String userID = "";
        if(mAuth != null){
            FirebaseUser current_user = mAuth.getCurrentUser();
            userID  = current_user.getUid();
        }

        String model_user = model.getFrom();
        if (model_user.equals(userID)) {
            holder.msgText.setText(model.getMessage());
            if(model.getImgUrl() != null){
                Glide.with(context).load(model.getImgUrl()).into(holder.userImg);
            }
            else{
                Glide.with(context).load(R.mipmap.account_person).into(holder.userImg);
            }

        } else {
            holder.msgText.setText(model.getMessage());
            if(model.getImgUrl() != null){
                Glide.with(context).load(model.getImgUrl()).into(holder.userImg);
            }
            else{
                Glide.with(context).load(R.drawable.ic_person).into(holder.userImg);
            }
        
            //holder.msgText.setBackgroundColor(Color.BLACK);
            // holder.msgText.setTextColor(Color.BLUE);

            holder.layout.setHorizontalGravity(Gravity.RIGHT);
            holder.layout.setBackgroundColor(Color.MAGENTA);
        }
    }

    @NonNull
    @Override
    public client_msgDetail_adapter.client_msgDetail_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msgdetail_item, parent, false);
        return new client_msgDetail_adapter.client_msgDetail_holder(view);
    }

    class client_msgDetail_holder extends RecyclerView.ViewHolder {
        TextView msgText;
        ImageView userImg;
        RelativeLayout layout;

        public client_msgDetail_holder(@NonNull View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.msgText_recyclerView);
            userImg = itemView.findViewById(R.id.img_msg_detail);
            layout = itemView.findViewById(R.id.msgdetail_layout);
        }

    }

}
