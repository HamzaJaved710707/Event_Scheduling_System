package com.example.eventscheduling.client.model;

import android.content.Context;
import android.media.Rating;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.ui.client_history;
import com.example.eventscheduling.client.util.Client_Msg_Values;
import com.example.eventscheduling.client.util.history_values;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_history extends RecyclerView.Adapter<Adapter_history.ViewHolder>{

    ArrayList<history_values> history_values_list = new ArrayList<>();

    Context mContext;
    private static final String TAG = "RecyclerView_Adapter";
    private history_adapter_interface historyAdapterInterface;

    public Adapter_history(ArrayList<history_values> history_values_list, Context mContext) {
        this.history_values_list = history_values_list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_history_recycleritem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: is called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(history_values_list.get(position).getImgUrl() == null){
            Glide.with(mContext).load(R.mipmap.account_person).into(holder.image);
        }else{
            Glide.with(mContext).load(history_values_list.get(position).getImgUrl()).into(holder.image);
        }

        holder.name.setText(history_values_list.get(position).getName());

holder.rating.setRating((int)history_values_list.get(position).getRate());
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        historyAdapterInterface.history_itemClick(history_values_list.get(position).getId());
    }
});
    }

    @Override
    public int getItemCount() {
     if(history_values_list.size() != 0){
         return  history_values_list.size();
     }
     return  0;
    }
public interface history_adapter_interface{
       void history_itemClick(String id);
}

public void setHistoryAdapterInterface(history_adapter_interface v){
        historyAdapterInterface = v;
}

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: iscalled");
          image = itemView.findViewById(R.id.image_history);
          name = itemView.findViewById(R.id.value_textHistory);
          rating = itemView.findViewById(R.id.client_history_rating);
        }

    }
}
