package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.MessageValues;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerView_Adapter_Message extends FirestoreRecyclerAdapter<MessageValues, RecyclerView_Adapter_Message.ViewHolder> {

    private static final String TAG = "RecyclerView_Adapter";
    private itemClickListenerMsgDetail listener;
    private Context mContext;


    public RecyclerView_Adapter_Message(Context context, @NonNull FirestoreRecyclerOptions<MessageValues> options) {
        super(options);
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MessageValues model) {
        Log.d(TAG, "onBindViewHolder: is called");
        holder.userName.setText(model.getName());
        holder.message.setText(model.getMessage());
      //  Glide.with(mContext).load(model.getImageResource()).into(holder.image);
    }


    public void onItemClickListner(itemClickListenerMsgDetail listener) {
        this.listener = listener;
    }

    public interface itemClickListenerMsgDetail {
        void itemClickListener(DocumentSnapshot documentSnapshot, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView userName;
        TextView message;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: is called");
            image = itemView.findViewById(R.id.image_id);
            userName = itemView.findViewById(R.id.recycler_text);
            message = itemView.findViewById(R.id.recycler_text_2);
            layout = itemView.findViewById(R.id.recycler_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.itemClickListener(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

    }
}
