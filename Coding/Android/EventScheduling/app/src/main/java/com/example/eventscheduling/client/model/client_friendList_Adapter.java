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

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

class client_friendList_Adapter extends FirestoreRecyclerAdapter<client_friendList_values, client_friendList_Adapter.client_friendList_Holder> {
    private onitemClickListener listener;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    public client_friendList_Adapter(Context context, @NonNull FirestoreRecyclerOptions options) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull client_friendList_Holder holder, int position, @NonNull client_friendList_values model) {

        if (currentUser != null) {
            if ((currentUser.getEmail()).equals(model.getEmail())) {
                holder.item_layout.setVisibility(View.GONE);


            } else {

                holder.name.setText(model.getName());
                holder.img.setImageResource(model.getImgUrl());

            }
        }


    }

    @NonNull
    @Override
    public client_friendList_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_item, parent, false);
        return new client_friendList_Adapter.client_friendList_Holder(view);
    }

    // method to be called from activity which implements this adapter... In this case FriendList.java class
    public void setitemOnclickListener(onitemClickListener listener) {
        this.listener = listener;
    }

    // Interface to implement onClickListener for recyclerView
    public interface onitemClickListener {
        void itemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class client_friendList_Holder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;
        CardView item_layout;
        public client_friendList_Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recycler_text_friendList);
            img = itemView.findViewById(R.id.image_id_friendList);
            item_layout = itemView.findViewById(R.id.friendlist_item_id);
            // Add click listener to each of the recyclerview item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();
                    //Check weather item postion is null
                    if (postion != RecyclerView.NO_POSITION && listener != null) {
                        // Calling method of interface declared above
                        listener.itemClick(getSnapshots().getSnapshot(postion), postion);
                    }
                }
            });
        }
    }
}
