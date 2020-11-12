package com.example.eventscheduling.eventorg.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.util.client_orders_data;
import com.example.eventscheduling.eventorg.util.OrderValues;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Adapter_Order extends RecyclerView.Adapter<RecyclerView_Adapter_Order.ViewHolder> {

    List<OrderValues> arrayList = new ArrayList<>();

    Context mcontext;
    private List<client_orders_data> client_orders_data_list = new ArrayList<>();
    private OnItemClicked onClick;


    public RecyclerView_Adapter_Order(Context context, List<OrderValues>  orderArray ){
        arrayList = orderArray;
        mcontext = context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evnt_org_order_recycler_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      OrderValues current = arrayList.get(position);
      if(current.getImgUrl() == null){
          Glide.with(mcontext).load(R.mipmap.account_person).into(holder.imageResource);
      }else{
          Glide.with(mcontext).load(current.getImgUrl()).into(holder.imageResource);
      }

      holder.orderHeader.setText(current.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.itemClick(current.getId(),client_orders_data_list.get(position).getPackageId(), client_orders_data_list.get(position).getTo(), client_orders_data_list.get(position).getOrderId(), client_orders_data_list.get(position).getFrom());
            }
        });

    }
    public interface OnItemClicked {
        void itemClick(String id,String from, String packageUserString, String orderId,String from_id);
    }
    public void setOnClick(RecyclerView_Adapter_Order.OnItemClicked onClick) {
        this.onClick = onClick;
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
           imageResource = itemview.findViewById(R.id.evntOrg_order_item_img);
           orderHeader = itemView.findViewById(R.id.evnt_order_name);

       }

   }
   public void getValues(List<client_orders_data> data){
       client_orders_data_list = data;
   }

}
