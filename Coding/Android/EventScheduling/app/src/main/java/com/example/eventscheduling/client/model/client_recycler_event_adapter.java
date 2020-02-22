package com.example.eventscheduling.client.model;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.home_event_values;

import java.util.ArrayList;


public class client_recycler_event_adapter extends RecyclerView.Adapter<client_recycler_event_adapter.ViewHolder> {

    ArrayList<home_event_values> text_value;

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter";
    private CardView cardView;

    public client_recycler_event_adapter(Context mContext, ArrayList<home_event_values> picValues) {
        this.text_value = picValues;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.client_home_recycler_event, parent, false);
        ViewHolder holder = new ViewHolder(view);
        cardView = view.findViewById(R.id.recyler_event_cardView);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is called");
        home_event_values current = text_value.get(position);
        holder.textValue.setText(current.gettextValue());
        Log.d(TAG, "onBindViewHolder: value of position " + position);
      /*  if(position == 3){
            holder.layout.setBackground( new ColorDrawable( R.drawable.gradient_recycler5));
        }
        else if(position == 2){
            holder.layout.setBackground( new ColorDrawable( R.drawable.gradient_recycler3));
        }
        else if(position == 1){
            holder.layout.setBackground( new ColorDrawable( R.drawable.gradient_recycler4));
        }
        else if(position == 4){
            holder.layout.setBackground( new ColorDrawable( R.drawable.gradient_recycler3));

        }*/
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
        return text_value.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textValue;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
            textValue = itemView.findViewById(R.id.text_client_home_event);
            layout = itemView.findViewById(R.id.recycler_layout_client_event);

        }

    }
}
