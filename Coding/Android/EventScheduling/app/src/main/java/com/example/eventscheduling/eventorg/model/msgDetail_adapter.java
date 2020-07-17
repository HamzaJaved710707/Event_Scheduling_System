package com.example.eventscheduling.eventorg.model;

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
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.msgDetail_values;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class msgDetail_adapter extends FirestoreRecyclerAdapter<msgDetail_values, msgDetail_adapter.msgDetail_holder> {

    Context context;
   FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public msgDetail_adapter(Context context, @NonNull FirestoreRecyclerOptions<msgDetail_values> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull msgDetail_holder holder, int position, @NonNull msgDetail_values model) {
        String userID = "";
        if(mAuth != null){
            FirebaseUser current_user = mAuth.getCurrentUser();
           userID  = current_user.getUid();
        }

        String model_user = model.getFrom();
        if (model_user.equals(userID)) {
            holder.msgText.setText(model.getMessage());
            Glide.with(context).load(model.getImgUrl()).into(holder.userImg);
        } else {
            holder.msgText.setText(model.getMessage());
            Glide.with(context).load(model.getImgUrl()).into(holder.userImg);
            holder.msgText.setBackgroundColor(Color.BLACK);
            holder.msgText.setTextColor(Color.BLUE);
            holder.layout.setHorizontalGravity(Gravity.RIGHT);

        }
    }

    @NonNull
    @Override
    public msgDetail_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msgdetail_item, parent, false);
        return new msgDetail_holder(view);
    }

    class msgDetail_holder extends RecyclerView.ViewHolder {
        TextView msgText;
        ImageView userImg;
        RelativeLayout layout;

        public msgDetail_holder(@NonNull View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.msgText_recyclerView);
            userImg = itemView.findViewById(R.id.img_msg_detail);
            layout = itemView.findViewById(R.id.msgdetail_layout);
        }
    }
}
