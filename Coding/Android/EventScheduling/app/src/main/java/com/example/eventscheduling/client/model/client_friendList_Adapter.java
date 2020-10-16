package com.example.eventscheduling.client.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class client_friendList_Adapter extends RecyclerView.Adapter<client_friendList_Adapter.client_friendList_Holder> {


    List<client_friendList_values> friendValues = new ArrayList<>();
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private OnItemClicked onClick;
    private String id;

    public client_friendList_Adapter(Context context, List<client_friendList_values> list) {

        this.context = context;
        friendValues = list;
    }

    @NonNull
    @Override
    public client_friendList_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_item, parent, false);
        return new client_friendList_Adapter.client_friendList_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull client_friendList_Holder holder, int position) {


        if (currentUser.getUid().equals(friendValues.get(position).getId())) {
            holder.item_layout.setVisibility(View.INVISIBLE);
        } else {
            holder.name.setText(friendValues.get(position).getName());
            if(friendValues.get(position).getImgUrl() == null){
                Glide.with(context).load(R.mipmap.account_person).into(holder.img);
            }else{
                Glide.with(context).load(friendValues.get(position).getImgUrl()).into(holder.img);
            }

            holder.msgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onMsgIconClick(position, friendValues.get(position).getId());
                }
            });
            holder.friendIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onFriendIconClick(position, friendValues.get(position).getId());

                }
            });

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onClick.onLayoutClick(friendValues.get(position).getId(), friendValues.get(position).getType());
            }
        });


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
        void onMsgIconClick(int position, String id);

        void onFriendIconClick(int position, String id);
        void onLayoutClick(String id, long type);
    }

    class client_friendList_Holder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView img;
        CardView item_layout;
        ImageView msgIcon;
        ImageView friendIcon;

        public client_friendList_Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recycler_text_friendList);
            img = itemView.findViewById(R.id.image_id_friendList);
            item_layout = itemView.findViewById(R.id.friendlist_item_id);
            msgIcon = itemView.findViewById(R.id.client_msgIcon_friendList);
            friendIcon = itemView.findViewById(R.id.client_friendIcon_friendList);

        }
    }
    public void deleteData(){
        friendValues.clear();
        notifyDataSetChanged();
    }
}
