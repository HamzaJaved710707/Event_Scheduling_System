package com.example.eventscheduling.client.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.create_venue_package_values;
import com.example.eventscheduling.eventorg.model.DragData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class create_venue_package_adapter extends FirestoreRecyclerAdapter<create_venue_package_values, create_venue_package_adapter.create_venue_package_viewHolder> {

    // Variables
    private static final String TAG = "create_venue_package_ad";
    private Context context;

    //Constructor
    public create_venue_package_adapter(Context context, @NonNull FirestoreRecyclerOptions<create_venue_package_values> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull create_venue_package_viewHolder holder, int position, @NonNull create_venue_package_values model) {
        holder.itemName.setText(model.getName());
        Glide.with(context).load(model.getImgUrl()).into(holder.imamgeView);
        final View shape = holder.imamgeView;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: " + model.getName());

                DragData state = new DragData(shape.getWidth(), shape.getHeight(), model.getName(), model.getImgUrl(), 2);
                final View.DragShadowBuilder shadow = new View.DragShadowBuilder(shape);
                ViewCompat.startDragAndDrop(shape, null, shadow, state, 0);
                return true;
            }
        });
    }

    @NonNull
    @Override
    public create_venue_package_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_package_item_layout, parent, false);
        return new create_venue_package_viewHolder(view);
    }

    static class create_venue_package_viewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        CircleImageView imamgeView;

        public create_venue_package_viewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.create_package_item_txt);
            imamgeView = itemView.findViewById(R.id.create_package_item_img);
        }


    }
}
