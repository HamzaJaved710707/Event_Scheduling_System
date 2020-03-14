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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.Client_Msg_Values;
import com.example.eventscheduling.client.util.history_values;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_history extends RecyclerView.Adapter<Adapter_history.ViewHolder> {

    ArrayList<history_values> messageValues = new ArrayList<>();

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter";

    public Adapter_history(Context mContext, ArrayList<history_values> msgValue) {
        this.messageValues = msgValue;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_history_recycleritem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is called");
        history_values current = messageValues.get(position);
        holder.image.setImageResource(current.getImageResource());
        holder.value.setText(current.getM_name());
        holder.priceTag.setText(current.getM_priceTag());
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
        TextView value;
        TextView priceTag;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
            image = itemView.findViewById(R.id.image_history);
            value = itemView.findViewById(R.id.value_textHistory);
            priceTag = itemView.findViewById(R.id.price_textHistory);
            layout = itemView.findViewById(R.id.history_recyclerLayout);

        }

    }
}
