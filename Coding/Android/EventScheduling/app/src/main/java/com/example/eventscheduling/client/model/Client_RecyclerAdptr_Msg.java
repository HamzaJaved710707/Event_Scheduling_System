package com.example.eventscheduling.client.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventscheduling.client.util.Client_Msg_Values;
import com.example.eventscheduling.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Client_RecyclerAdptr_Msg extends RecyclerView.Adapter<Client_RecyclerAdptr_Msg.ViewHolder> {

    ArrayList<Client_Msg_Values> messageValues = new ArrayList<>();

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter";

    public Client_RecyclerAdptr_Msg(Context mContext, ArrayList<Client_Msg_Values> msgValue) {
        this.messageValues = msgValue;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recyclerview_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is called");
        Client_Msg_Values current = messageValues.get(position);
        holder.image.setImageResource(current.getImageResource());
        holder.messageHeader.setText(current.getMessageHeader());
        holder.messageDetail.setText(current.getMessageDetail());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: in onbindview holder");
                Toast.makeText(mContext, "Toast", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: is called");
        return messageValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView messageHeader;
        TextView messageDetail;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
            image = itemView.findViewById(R.id.client_order_item_img);
            messageHeader = itemView.findViewById(R.id.client_order_txt_name);
            messageDetail = itemView.findViewById(R.id.client_orders_txt2);
            layout = itemView.findViewById(R.id.recycler_layout);

        }

    }
}
