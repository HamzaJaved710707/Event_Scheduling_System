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
import com.example.eventscheduling.client.util.create_food_item_values;
import com.example.eventscheduling.eventorg.model.DragData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class create_food_item_package_adapter extends FirestoreRecyclerAdapter<create_food_item_values,create_food_item_package_adapter.create_food_item_package_holder>
           {

private Context context;
               private static final String TAG = "create_food_item_packag";

   // private OnStartDragListener mDragStartListener;
   // private OnCustomerListChangedListener mListChangedListener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public create_food_item_package_adapter(Context context,@NonNull FirestoreRecyclerOptions<create_food_item_values> options
                                        ) {
        super(options);
        this.context = context;

     //   mDragStartListener = dragLlistener;
       // mListChangedListener = listChangedListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull create_food_item_package_holder holder, int position, @NonNull create_food_item_values model) {


        holder.itemName.setText(model.getName());
        Glide.with(context).load(model.getImgUrl()).into(holder.imamgeView);
        final View shape = holder.imamgeView;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: " + model.getName());

                DragData state = new DragData(shape.getWidth(), shape.getHeight(), model.getName(), model.getImgUrl(), 0);
                final View.DragShadowBuilder shadow = new View.DragShadowBuilder(shape);
                ViewCompat.startDragAndDrop(shape, null, shadow, state, 0);
                return true;
            }
        });

    }

    @NonNull
    @Override
    public create_food_item_package_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_package_item_layout, parent, false);
        return new create_food_item_package_adapter.create_food_item_package_holder(view);
    }


// Itemtouch helper interface methods


    class create_food_item_package_holder extends RecyclerView.ViewHolder {
        TextView itemName;
        CircleImageView imamgeView;

        public create_food_item_package_holder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.create_package_item_txt);
            imamgeView = itemView.findViewById(R.id.create_package_item_img);
        }


    }
    }

