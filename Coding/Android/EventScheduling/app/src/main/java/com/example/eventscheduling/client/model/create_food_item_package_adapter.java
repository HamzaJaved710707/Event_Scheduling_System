package com.example.eventscheduling.client.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.create_food_item_values;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class create_food_item_package_adapter extends FirestoreRecyclerAdapter<create_food_item_values,create_food_item_package_adapter.create_food_item_package_holder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public create_food_item_package_adapter(@NonNull FirestoreRecyclerOptions<create_food_item_values> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull create_food_item_package_holder holder, int position, @NonNull create_food_item_values model) {

    }

    @NonNull
    @Override
    public create_food_item_package_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_item, parent, false);
        return new create_food_item_package_adapter.create_food_item_package_holder(view);
    }

    class create_food_item_package_holder extends RecyclerView.ViewHolder{
        TextView itemName;
        ImageView imamgeView;

        public create_food_item_package_holder(@NonNull View itemView) {
            super(itemView);

        }
    }
    }

