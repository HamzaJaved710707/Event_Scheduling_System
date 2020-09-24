package com.example.eventscheduling.client.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class client_send_package_adapter extends RecyclerView.Adapter<client_send_package_adapter.client_package_send_holder> {


    List<client_friendList_values> friendValues = new ArrayList<>();
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private OnItemClicked onClick;
    private String id;

    public client_send_package_adapter(Context context, List<client_friendList_values> list) {

        this.context = context;
        friendValues = list;
    }

    @NonNull
    @Override
    public client_package_send_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_send_package_friendlist, parent, false);
        return new client_send_package_adapter.client_package_send_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull client_package_send_holder holder, int position) {


        if (currentUser.getUid().equals(friendValues.get(position).getId())) {
            holder.item_layout.setVisibility(View.INVISIBLE);
        } else {
            holder.name.setText(friendValues.get(position).getName());
            Glide.with(context).load(friendValues.get(position).getImgUrl()).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onItemClick(friendValues.get(position).getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return friendValues.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;


    }


    // method to be called from activity which implements this adapter... In this case FriendList.java class


    // Interface to implement onClickListener for recyclerView

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(String id);
    }

    class client_package_send_holder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView img;
        CardView item_layout;

        public client_package_send_holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_package_send_txt);
            img = itemView.findViewById(R.id.client_package_send_imgId);
            item_layout = itemView.findViewById(R.id.client_send_package_layout);


        }
    }
}
