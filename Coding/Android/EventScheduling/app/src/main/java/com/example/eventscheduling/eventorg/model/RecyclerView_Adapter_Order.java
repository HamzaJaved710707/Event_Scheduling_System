package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.util.OrderValues;

import java.util.ArrayList;

public class RecyclerView_Adapter_Order extends RecyclerView.Adapter<RecyclerView_Adapter_Order.ViewHolder> {

    ArrayList<OrderValues> arrayList = new ArrayList<>();
    Context mcontext;

    public RecyclerView_Adapter_Order(Context context, ArrayList<OrderValues>  orderArray ){
        arrayList = orderArray;
        mcontext = context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      OrderValues current = arrayList.get(position);
      holder.imageResource.setImageResource(current.getImageResource());
      holder.orderHeader.setText(current.getOrderHeader());
      holder.orderDetail.setText(current.getOrderDetail());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
   {
         ImageView imageResource;
         TextView orderHeader;
         TextView orderDetail;

       public ViewHolder(View itemview){
           super(itemview);
           imageResource = itemview.findViewById(R.id.image_id);
           orderHeader = itemView.findViewById(R.id.recycler_text);
           orderDetail = itemview.findViewById(R.id.recycler_text_2);
       }

   };

}
