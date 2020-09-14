package com.example.eventscheduling.eventorg.model;

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
import com.example.eventscheduling.eventorg.util.friendList_values;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class friendList_Adapter extends FirestoreRecyclerAdapter<friendList_values, friendList_Adapter.friendList_Holder> {
    private onitemClickListener listener;
    private Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private String currentUserID;
    public friendList_Adapter(Context context, @NonNull FirestoreRecyclerOptions options) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull friendList_Holder holder, int position, @NonNull friendList_values model) {

        if(currentUser != null){
        if((currentUser.getEmail()).equals(model.getEmail())){
            holder.item_layout.setVisibility(View.INVISIBLE);
            return;
        }
        else{

            holder.name.setText(model.getName());
            holder.img.setImageResource(model.getImgUrl());
        }
        }

    }



    @NonNull
    @Override
    public friendList_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_item, parent, false);
        return new friendList_Holder(view);
    }

    // method to be called from activity which implements this adapter... In this case FriendList.java class
    public void setitemOnclickListener(onitemClickListener listener) {
        this.listener = listener;
    }

    // Interface to implement onClickListener for recyclerView
    public interface onitemClickListener {
        void itemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class friendList_Holder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;
        CardView  item_layout;

        public friendList_Holder(@NonNull View itemView) {
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
