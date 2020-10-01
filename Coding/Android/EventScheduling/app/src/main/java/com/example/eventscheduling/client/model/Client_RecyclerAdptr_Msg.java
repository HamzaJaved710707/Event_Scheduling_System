package com.example.eventscheduling.client.model;

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
import com.example.eventscheduling.client.util.Client_Msg_Values;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class Client_RecyclerAdptr_Msg extends FirestoreRecyclerAdapter<Client_Msg_Values, Client_RecyclerAdptr_Msg.ViewHolder> {

    private static final String TAG = "RecyclerView_Adapter";
    private RecyclerView_Adapter_Message.itemClickListenerMsgDetail listener;
    private Context mContext;


    public Client_RecyclerAdptr_Msg(Context context, @NonNull FirestoreRecyclerOptions<Client_Msg_Values> options) {
        super(options);
        mContext = context;
    }


    @NonNull
    @Override
    public Client_RecyclerAdptr_Msg.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recyclerview_item, parent, false);
        return new Client_RecyclerAdptr_Msg.ViewHolder(view);
    }




    public void onItemClickListner(RecyclerView_Adapter_Message.itemClickListenerMsgDetail listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Client_Msg_Values model) {
        holder.userName.setText(model.getName());
        Glide.with(mContext).load(model.getImageResource()).into(holder.image);
    }

    public interface itemClickListenerMsgDetail {
        void itemClickListener(DocumentSnapshot documentSnapshot, int position);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView userName;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
            image = itemView.findViewById(R.id.image_id_msg);
            userName = itemView.findViewById(R.id.message_item_name);

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
