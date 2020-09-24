package com.example.eventscheduling.client.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.client_orders_values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class client_orders_adapter extends RecyclerView.Adapter<client_orders_adapter.client_order_adapter_holder> {
    List<client_orders_values> order_values = new ArrayList<>();
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private client_orders_adapter.OnItemClicked onClick;
    private String id;

    public client_orders_adapter(Context context, List<client_orders_values> list) {

        this.context = context;
        order_values = list;
    }

    @NonNull
    @Override
    public client_order_adapter_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evnt_org_order_recycler_item, parent, false);
        return new client_orders_adapter.client_order_adapter_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull client_order_adapter_holder holder, int position) {

        if (currentUser.getUid().equals(order_values.get(position).getId())) {
            holder.item_layout.setVisibility(View.INVISIBLE);
        } else {
            holder.name.setText(order_values.get(position).getName());
            Glide.with(context).load(order_values.get(position).getImgUrl()).into(holder.img);
            holder.detailTxt.setText("Click here for detail");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.itemClick(order_values.get(position).getId());
                }
            });
        }
    }
    public void setOnClick(client_orders_adapter.OnItemClicked onClick) {
        this.onClick = onClick;
    }


    @Override
    public int getItemCount() {
        return order_values.size();
    }

    public interface OnItemClicked {
      void itemClick(String id);
    }

    class client_order_adapter_holder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView img;
        CardView item_layout;
        TextView detailTxt;


        public client_order_adapter_holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_order_txt_name);
            img = itemView.findViewById(R.id.client_order_item_img);
            item_layout = itemView.findViewById(R.id.client_order_cardView);
            detailTxt = itemView.findViewById(R.id.client_orders_txt2);

        }
    }

}